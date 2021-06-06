package top.jsls9.oajsfx.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.controller.HlxController;
import top.jsls9.oajsfx.dao.PostLogDao;
import top.jsls9.oajsfx.dao.SendScoreLogDao;
import top.jsls9.oajsfx.hlxPojo.Post;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.model.PostLog;
import top.jsls9.oajsfx.model.SendScoreLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.HlxService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.RespBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * @author bSu
 * @date 2021/5/15 - 18:00
 */
@Service
public class HlxUserServiceImpl implements HlxService {

    private static Logger logger = Logger.getLogger(HlxUserServiceImpl.class);

    @Autowired
    private HlxUtils hlxUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private PostLogDao postLogDao;
    @Autowired
    private SendScoreLogDao sendScoreLogDao;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 结算帖子奖励
     * @param hlxUserId 登录用户的社区用户id
     * @param postId    社区帖子id
     * @return
     */
    @Override
    public Object settlement (String hlxUserId, String postId) throws ParseException, IOException {
        try {
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
            Integer integer = sendSourceByLevel(postDetails.getPost(), level, hlxUserId,postLogByHlxPostId);
            return RespBean.success("结算成功。");
        }catch (Exception e){
            logger.error("帖子结算失败，帖子id：【"+postId+"】；"+"原因："+e.getMessage());
            e.printStackTrace();
            return RespBean.error("结算失败，"+e.getMessage());
        }
    }

    /**
     * 通过帖子id和等级给帖子结算葫芦，直接赠送到帖子上
     * @param post  结算帖子详情
     * @param level 本次结算的等级
     * @param hlxUserId 登录用户的葫芦侠id
     * @return  当前次结算等级
     * @throws IOException
     */
    private Integer sendSourceByLevel(Post post, int level, String hlxUserId, PostLog postLog) throws IOException {
        int source = 0;
        switch (level){
            case 1:
                source=25;
                break;
            case 2:
                source=100;
                break;
            case 3:
                source=180;
                break;
        }
        //获得上次结算的等级
        //要看是不是为空，待办，没电了拜拜，什么垃圾代码;TODO 结算功能待重构
        int gradeSource = 0;
        switch (postLog==null?0:postLog.getGrade()){
            case 0://可能未结算过，勾除的上次结算奖励即为0
                gradeSource=0;
                break;
            case 1:
                gradeSource=25;
                break;
            case 2:
                gradeSource=100;
                break;
            case 3:
                gradeSource=180;
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
            hlxUtils.sendSorce("1",String.valueOf(post.getPostID()),"奖励结算，当前结算等级："+level+"；少侠辛苦了。",String.valueOf(source));
            logger.info("奖励结算，当前结算等级："+level+"；少侠辛苦了。获得奖励："+source+"。上次结算等级："+String.valueOf(postLog==null?0:postLog.getGrade()));
            sendScoreLog.setState(0);
            sendScoreLog.setSourceNumber(source);
            //葫芦赠送日志
            sendScoreLogDao.insert(sendScoreLog);
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
            return level;
        }catch (Exception e){
            //保存日志后抛出异常
            sendScoreLog.setState(1);
            sendScoreLog.setMsg(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }



}
