package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.jsoup.Connection;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.hlxPojo.Posts;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.HlxService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.HttpUtils;
import top.jsls9.oajsfx.utils.RedisUtil;
import top.jsls9.oajsfx.utils.RespBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** 处理的第三方葫芦侠社区接口
 * @author bSu
 * @date 2021/5/6 - 21:12
 */
@Api(tags = "社区帖子处理")
@RestController
public class HlxController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private HlxService hlxService;

    @Autowired
    private RedisUtil redisUtil;


    private static String getName(){
        return HlxController.class.getName();
    }

    /**
     * 通过社区id，前100个帖子中所有的技术分享板块帖子
     * @return
     */
    @RequiresAuthentication //登录才可访问
    @ApiOperation("查询帖子")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/post_old")
    public RespBean getPostsByUserId(String userId) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if(StringUtil.isBlank(userId)){
            //获得当前登录用户名
            Subject subject = SecurityUtils.getSubject();
            String principal = (String) subject.getPrincipal();
            //查询登录用户信息
            User user = userService.getUserByUserName(principal);
            //参数为空时查询登录用户的帖子
            userId=user.getHlxUserId();
            /*logger.info(getName()+"，查询失败，参数缺失");
            return RespBean.error("查询失败，参数缺失。", Collections.emptyList());*/
        }
        //判断是是否为团队成员
        User user = userService.queryUserByHlxUserId(userId);
        if(user==null){
            return RespBean.error("查询失败，此用户不是团队成员。", Collections.emptyList());
        }
        String catId = "96";//96表示技术分享板块
        String postJsonUrl="http://floor.huluxia.com/post/create/list/ANDROID/2.0?start=0&count=50&user_id="+userId;
        Connection.Response response = HttpUtils.get(postJsonUrl);
        String body = response.body();
        JSONObject json=new JSONObject();
        PostsJsonRootBean jsonRootBean = json.parseObject(response.body(), PostsJsonRootBean.class);
        List<Posts> posts = jsonRootBean.getPosts();
        //结果集合
        List<Map<String,Object>> listResult = new ArrayList<>();

        for(Posts post : posts){
            //五月份之前的帖子已结算完毕，系统只结算五月及之后的帖子
            if(String.valueOf(post.getCategory().getCategoryID()).equals(catId) && sdf.parse("2021-05").getTime()<=post.getCreateTime()){
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
            }
        }
        return RespBean.success("查询成功",listResult);
    }

    /**
     * 通过帖子id进行结算，每次结算可以结算的最高等级，已结算过最高等级的不可再次结算。
     * 自助结算等级，一共三个等级；1-普通贴、2热贴、3精帖
     * @param postId 帖子id
     * @param type 结算类型，0-结算到帖子，1-结算到楼主oa账户上
     * @return
     */
    @ApiOperation("结算帖子")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @RequiresAuthentication
    @PostMapping("/post/{type}/{postId}")
    public RespBean settlement(@PathVariable("postId") String postId,@PathVariable("type") Integer type) throws IOException, ParseException {
        if(type == null){
            type = 0;
        }
        //获得当前登录用户名
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        //查询登录用户信息
        User user = userService.getUserByUserName(principal);
        //获取帖子详情，这tm好耦合啊
        PostsJsonRootBean postDetails = HlxUtils.getPostDetails(postId);
        //判断用户权限，普通用户仅限结算自己的，管理员用户可以结算所属团队的，超级管理员结算不受限制； TODO 单独抽出来做验证，以后也许会用到其他地方
        if(!subject.hasRole("superAdmin")){
            if(!subject.hasRole("admin")){
                boolean equals = user.getHlxUserId().equals(String.valueOf(postDetails.getPost().getUser().getUserID()));
                if(!equals){
                    return RespBean.error("结算失败，当前无权限结算此贴。");
                }
            }else{
                //查询当前帖子楼主是否跟此管理员属于同一团队
                User adminUser = userService.queryUserByHlxUserId(String.valueOf(postDetails.getPost().getUser().getUserID()));
                if(!adminUser.getDeptId().equals(user.getDeptId())){
                    return RespBean.error("结算失败，当前无权限结算此贴。");
                }
            }
        }
        //结算
        RespBean settlement = (RespBean) hlxService.settlement(user.getHlxUserId(), postId, type);
        System.out.println(principal);
        return settlement;
    }


    /**
     * 重构查询方式，使用官方api，每次查询20个，然后第二次使用上一次的start值进一步查询
     * @return
     */
    @RequiresAuthentication //登录才可访问
    @ApiOperation("查询帖子(新)")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/post")
    public RespBean getPostsByUserIdNew(String userId) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if(StringUtil.isBlank(userId)){
            //获得当前登录用户名
            Subject subject = SecurityUtils.getSubject();
            String principal = (String) subject.getPrincipal();
            //查询登录用户信息
            User user = userService.getUserByUserName(principal);
            //参数为空时查询登录用户的帖子
            userId=user.getHlxUserId();
            /*logger.info(getName()+"，查询失败，参数缺失");
            return RespBean.error("查询失败，参数缺失。", Collections.emptyList());*/
        }
        //判断是是否为团队成员
        User user = userService.queryUserByHlxUserId(userId);
        if(user==null){
            return RespBean.error("查询失败，此用户不是团队成员。", Collections.emptyList());
        }
        //查询是否有缓存，如果有就使用缓存
        Object postList = redisUtil.get("postList:" + userId);
        if(postList != null){
            return RespBean.success("查询成功",postList);
        }
        //开始查询
        Object postsByUserIdNew = hlxService.getPostsByUserIdNew(userId);
        //本次查询未走缓存，添加接口到缓存，缓存8个小时
        redisUtil.set("postList:" + userId , postsByUserIdNew ,60 * 60 * 8);
        return RespBean.success("查询成功",postsByUserIdNew);
    }

}
