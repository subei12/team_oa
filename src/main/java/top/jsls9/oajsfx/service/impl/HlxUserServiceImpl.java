package top.jsls9.oajsfx.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jsls9.oajsfx.dao.PostLogDao;
import top.jsls9.oajsfx.dao.PostLogicDao;
import top.jsls9.oajsfx.dao.SendScoreLogDao;
import top.jsls9.oajsfx.enums.PostLogicVariable;
import top.jsls9.oajsfx.hlxPojo.Post;
import top.jsls9.oajsfx.hlxPojo.Posts;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.model.PostLog;
import top.jsls9.oajsfx.model.PostLogic;
import top.jsls9.oajsfx.model.SendScoreLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.HlxService;
import top.jsls9.oajsfx.service.PostLogicService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.HttpUtils;
import top.jsls9.oajsfx.utils.JsonUtiles;
import top.jsls9.oajsfx.utils.RespBean;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author bSu
 * @date 2021/5/15 - 18:00
 */
@Service
public class HlxUserServiceImpl implements HlxService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HlxUtils hlxUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private PostLogDao postLogDao;
    @Autowired
    private SendScoreLogDao sendScoreLogDao;
    @Autowired
    private PostLogicDao postLogicDao;

    private static final String SUCCESS = "success";


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 结算帖子奖励
     * @param hlxUserId 登录用户的社区用户id
     * @param postId    社区帖子id
     * @return
     */
    @Override
    public Object settlement (String hlxUserId, String postId, Integer type) throws ParseException, IOException {
        try {
            //结算前置逻辑判断
            String logic = postLogic(postId);
            if(!SUCCESS.equals(logic)){
                return RespBean.error("结算失败；" + logic);
            }
            //查询是否为团队成员帖子，并且在结算日期内（2021.5.1之后为可结算）
            PostsJsonRootBean postDetails = HlxUtils.getPostDetails(postId);
            //模拟数据 TODO 记得去掉
            //postDetails.getPost().setCommentCount(230);
            if(postDetails==null){
                return RespBean.error("结算失败，帖子查询不到。");
            }
            //判断是是否为团队成员
            long userID = postDetails.getPost().getUser().getUserID();
            User user = userService.queryUserByHlxUserId(String.valueOf(userID));
            if(user==null){
                return RespBean.error("结算失败，此用户不是团队成员。", Collections.emptyList());
            }
            //判断帖子是否可结算
            long createTime = postDetails.getPost().getCreateTime();
            Date parse = sdf.parse("2021-05-01");
            if(createTime<=parse.getTime()){
                return RespBean.error("结算失败，此贴不在自助结算时间范围内。");
            }
            //判断是否为技术分享板块的帖子
            if(StringUtils.isBlank(String.valueOf((postDetails.getPost().getCategory().getCategoryID()))) || !String.valueOf((postDetails.getPost().getCategory().getCategoryID())).equals("96") ){
                return RespBean.error("结算失败，此贴非技术分享帖子（你别跟我整什么花里胡哨的）。");
            }
            //判断是否为三天前的帖子
            if(!(new Date().getTime() - postDetails.getPost().getCreateTime()>= 1000 * 60 * 60 * 24 *3)){
                return RespBean.error("结算失败，新帖请在三天后再结算。");
            }

            //查询是否已结算过，结算的等级
            PostLog postLogByHlxPostId = postLogDao.getPostLogByHlxPostId(postId);
            Integer grade = 0;
            if(postLogByHlxPostId!=null){
                grade = postLogByHlxPostId.getGrade();
            }
            if(grade>=3){
                return RespBean.error("结算失败，此贴已结算完毕。");
            }
            //获得此贴当前可结算的最高奖励
            int level = 1;//初始1
            if(postDetails.getPost().getCommentCount()>=200){
                level=2;//热贴2
            }
            if(postDetails.getPost().getIsGood()==1){
                level=3;//精帖3
            }
            if(grade>=level){
                return RespBean.error("结算失败，此贴已结算过当前可结算最高奖励。");
            }
            //根据等级给帖子结算葫芦
            Integer integer = sendSourceByLevel(postDetails.getPost(), level, hlxUserId,postLogByHlxPostId,type);
            return RespBean.success("结算成功。");
        }catch (IOException e){
            logger.error("获取帖子详情报错",e.getMessage());
            return RespBean.error("结算失败，获取帖子详情报错。");
        }catch (ScriptException e){
            logger.error("逻辑运算出错",e.getMessage());
            return RespBean.error("结算失败，逻辑运算出错，请联系管理员。");
        }catch (Exception e){
            logger.error("帖子结算失败，帖子id：【"+postId+"】；"+"原因："+e.getMessage());
            e.printStackTrace();
            return RespBean.error("结算失败，"+e.getMessage());
        }
    }

    /**
     * 帖子结算前置逻辑判断
     * @param postId
     * @return
     */
    public String postLogic(String postId) throws IOException, ScriptException {
        //运算
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        String postDetailsToJson = HlxUtils.getPostDetailsToJson(postId);
        //给所有变量赋值
        for (PostLogicVariable p : PostLogicVariable.values()) {
            engine.put("$"+p.getName(), JsonUtiles.getJsonString(postDetailsToJson, p.getValue()));
        }
        //获取所有逻辑
        List<PostLogic> postLogics = postLogicDao.queryPostLogicList();
        for(PostLogic postLogic : postLogics){
            if(postLogic.getState() != 0){
                continue;
            }
            if(StringUtils.isBlank(postLogic.getLogic())){
                continue;
            }
            //判断逻辑是为true
            Object result = engine.eval(postLogic.getLogic());
            if("java.lang.Boolean".equals(result.getClass().getName()) && (Boolean) result){
                continue;
            }
            return postLogic.getPrompt();
        }
        return SUCCESS;
    }

    /**
     * 通过帖子id和等级给帖子结算葫芦，直接赠送到帖子上
     * @param post  结算帖子详情
     * @param level 本次结算的等级
     * @param hlxUserId 登录用户的葫芦侠id
     * @param type 结算类型，0-结算到帖子，1-结算到楼主oa账户上
     * @return  当前次结算等级
     * @throws IOException
     */
    private Integer sendSourceByLevel(Post post, int level, String hlxUserId, PostLog postLog,Integer type) throws IOException {
        int source = 0;
        //积分
        int integral = 0;
        switch (level){
            case 1:
                source=25;
                integral=1;
                break;
            case 2:
                source=100;
                integral=3;
                break;
            case 3:
                source=180;
                integral=5;
                break;
        }
        //获得上次结算的等级
        //要看是不是为空，待办，没电了拜拜，什么垃圾代码;TODO 结算功能待重构
        int gradeSource = 0;
        //积分临时变量，用作计算
        int tempIntegral = 0;
        switch (postLog==null?0:postLog.getGrade()){
            case 0://可能未结算过，勾除的上次结算奖励即为0
                gradeSource=0;
                //用这么多会方便计算吗？只是会加代码行数罢了
                tempIntegral=0;
                break;
            case 1:
                gradeSource=25;
                tempIntegral=1;
                break;
            case 2:
                gradeSource=100;
                tempIntegral=3;
                break;
            case 3:
                gradeSource=180;
                tempIntegral=5;
                break;
        }
        source = source-gradeSource;
        SendScoreLog sendScoreLog = new SendScoreLog();
        sendScoreLog.setHlxPostId(String.valueOf(post.getPostID()));
        //结算后所得葫芦的用户id，即帖子楼主
        sendScoreLog.setHlxUserId(String.valueOf(post.getUser().getUserID()));
        //结算操作人，登录用户的
        sendScoreLog.setUserName(hlxUserId);
        try {
            User user = userService.queryUserByHlxUserId(String.valueOf(post.getUser().getUserID()));
            switch (type){
                case 0://结算到帖子
                    hlxUtils.sendSorce("1",String.valueOf(post.getPostID()),"奖励结算，当前结算等级："+level+"；少侠辛苦了。",String.valueOf(source));
                    sendScoreLog.setState(0);
                    sendScoreLog.setSourceNumber(source);
                    //葫芦赠送日志
                    sendScoreLogDao.insert(sendScoreLog);
                    break;
                case 1://结算到楼主oa账户上
                    user.setGourd(source);
                    userService.updateGourdByHlxUserId(user);
                    break;
            }

            logger.info("奖励结算，当前结算等级："+level+"；少侠辛苦了。获得奖励："+source+"。上次结算等级："+String.valueOf(postLog==null?0:postLog.getGrade()));

            //addOrUpdate帖子结算日志
            //TODO 帖子结算后没有录入结算日志 待测试
            if(postLog==null){
                postLog = new PostLog();
                //add
                postLog.setCreateState("0");
                postLog.setGrade(level);
                postLog.setHlxPostId(String.valueOf(post.getPostID()));
                postLog.setState("0");
                postLog.setHlxUserId(hlxUserId);
                //申请人的用户id
                postLog.setOperationHlxUserId(hlxUserId);
                postLogDao.insert(postLog);
            }else{
                //update
                postLog.setGrade(level);
                postLogDao.updateByPrimaryKey(postLog);
            }
            //增加积分
            //hlxUserId只是登录用户的，应该给结算帖子的楼主增加积分
            user.setIntegral(integral-tempIntegral);
            userService.updateIntegral(user);

            return level;
        }catch (Exception e){
            //保存日志后抛出异常
            sendScoreLog.setState(1);
            sendScoreLog.setMsg(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 新的查询方法
     * @param userId 用户id
     * @return
     */
    @Override
    public Object getPostsByUserIdNew(String userId) throws IOException, ParseException {
        String catId = "96";//96表示技术分享板块
        //开始帖子，第一次为0
        String start = "0";
        //标记，是否继续循环
        boolean flag = true;
        //结果集合
        List<Map<String,Object>> listResult = new ArrayList<>();
        do{
            String postJsonUrl="http://floor.huluxia.com/post/create/list/ANDROID/2.0?start="+start+"&count=20&user_id="+userId+"&_key="+hlxUtils.getKey();
            Connection.Response response = HttpUtils.get(postJsonUrl);
            String body = response.body();
            JSONObject json=new JSONObject();
            PostsJsonRootBean jsonRootBean = json.parseObject(response.body(), PostsJsonRootBean.class);
            List<Posts> posts = jsonRootBean.getPosts();
            //下一次查询开始的帖子id
            start = jsonRootBean.getStart();
            //只查询当月和上月的帖子
            //获取当月和上月
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); // 设置为当前时间
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
            date = calendar.getTime();

            for(Posts post : posts){
                //不是技术分享板块帖子，跳出循环
                if(!String.valueOf(post.getCategory().getCategoryID()).equals(catId)){
                    continue;
                }
                //只获取一个月内的帖子
                if(date.getTime()<=post.getCreateTime()){
                    //获取是否精帖、赞的数量
                    PostsJsonRootBean postDetails = HlxUtils.getPostDetails(String.valueOf(post.getPostID()));
                    Map<String,Object> map = new HashMap<>();
                    map.put("postId",post.getPostID());//帖子id
                    map.put("title",post.getTitle());//标题
                    map.put("hit",post.getHit());//浏览量
                    map.put("praise",postDetails.getPost().getPraise());//赞
                    map.put("commentCount",post.getCommentCount());//回复数量
                    map.put("source",post.getScore());//葫芦
                    map.put("isGood",postDetails.getPost().getIsGood()==1?"是":"否");//是否精贴
                    map.put("createTime",new SimpleDateFormat("yyyy-MM-dd").format(post.getCreateTime()));//发帖时间
                    listResult.add(map);
                }else{
                    //不是一个月内的帖子，不再参与结算查询，如需结算需要自行抓包，本程序不提供服务（也不是懒，只是不想惯着长时间不结算的毛病）
                    flag = false;
                    break;
                }
            }
            //小与20表示本次查询不满，即为已经没有帖子可以用于下次查询
            if(posts.size()<20){
                flag = false;
            }
        }while (flag);
        return listResult;
    }



}
