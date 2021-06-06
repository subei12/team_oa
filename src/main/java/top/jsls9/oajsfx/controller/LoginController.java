package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.jsr303Mode.RegisterUser;
import top.jsls9.oajsfx.jsr303Mode.User;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.MailUtils;
import top.jsls9.oajsfx.utils.RedisUtil;
import top.jsls9.oajsfx.utils.RespBean;

import javax.validation.Valid;
import java.util.Date;
import java.util.Random;

/**
 * @author bSu
 * @date 2020/5/26 - 17:45
 */
@Api(tags = "登录接口")
@RestController
public class LoginController {

    private static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private UserDao usersDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/user/login")
    public RespBean login(@Valid User user) {
        Subject subject = SecurityUtils.getSubject();
        //封装用户信息，去认证
        String passWord = DigestUtils.md5DigestAsHex((user.getUserName() + user.getPassWord()).getBytes());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),passWord);
        try {
            subject.login(token);
            return RespBean.success("登录成功");
        }catch (LockedAccountException e){
            return RespBean.error(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("用户名或者密码错误");
        }
    }


    @RequiresPermissions(value = {"user_del","123"},logical = Logical.OR)
    //@ApiIgnore() 不显示在文档内
    @PostMapping("/queryUserByid")
    public RespBean queryUserByid(){
        try {
            Subject subject = SecurityUtils.getSubject();
            Object principal = subject.getPrincipals();
            System.out.println(principal);
            System.out.println(subject.getSession().getTimeout());

            System.out.println(subject.getSession().getStartTimestamp());
            System.out.println(subject.getSession().getLastAccessTime());
            return RespBean.success("查询成功",subject.getSession().getId());
        }catch (Exception e){
            e.printStackTrace();
            //System.out.println(e.getMessage());
            return RespBean.error("查询失败");
        }

    }

    @ApiOperation("登录")
    @GetMapping("/login")
    public RespBean loginNew(@Valid User user) {
        Subject subject = SecurityUtils.getSubject();
        //封装用户信息，去认证
        String passWord = DigestUtils.md5DigestAsHex((user.getUserName() + user.getPassWord()).getBytes());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),passWord);
        try {
            subject.login(token);
            return RespBean.success("登录成功",subject.getSession().getId());
        }catch (LockedAccountException e){
            return RespBean.error(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("用户名或者密码错误");
        }
    }

    @ApiOperation("测试接口")
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/test")
    public RespBean test(){
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        System.out.println(principal);
        return RespBean.success("测试成功",principal);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public RespBean logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return RespBean.success("退出登录成功");
    }

    /**
     * 仅限已录入用户信息的用户通过QQ邮箱设置登录密码
     * @return
     */
    @ApiOperation("注册用户")
    @PostMapping("register")
    public RespBean register(@Valid @RequestBody RegisterUser user){

        if(!user.getPassWord().equals(user.getPassWord1())){
            return RespBean.error("两次密码输入不一致。");
        }
        top.jsls9.oajsfx.model.User userByUserName = usersDao.getUserByUserName(user.getQq());
        if(userByUserName==null){
            logger.info("查无此人."+user.getQq());
            return RespBean.error("密码重置失败，请检查后重试。");
        }
        //判断验证码是否正确
        String code = user.getCode();
        Object redisCode = redisUtil.get(user.getQq() + "@qq.com:code");
        if(redisCode==null || !code.equals(redisCode)){
            //如果查询到与之邮箱相匹配的验证码为空，或者匹配不正确，即为失败
            //return RespBean.error("验证码不正确，请检查后重试。");
            logger.info("验证码不正确，请检查后重试。");
            return RespBean.error("密码重置失败，请检查后重试。");
        }
        //验证通过后，需要把key在redis里删除
        redisUtil.del(user.getQq() + "@qq.com:code");
        //密码进行md5
        String passWord = DigestUtils.md5DigestAsHex((user.getQq() + user.getPassWord()).getBytes());
        userByUserName.setPassword(passWord);
        //重置密码
        userService.updateUserPwdByUserId(userByUserName);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return RespBean.success("密码重置成功，请重新登录");
    }

    @PostMapping("/sendMailCode")
    public Object sendMailCode(String qq){
        try {
            top.jsls9.oajsfx.model.User userByUserName = usersDao.getUserByUserName(qq);
            if(userByUserName==null){
                logger.info("查无此人，但仍返回成功，以防被刷。");
                return RespBean.success("验证码发送成功，请留意邮箱查收！");
            }
            String email = qq+"@qq.com";
            if(StringUtils.isBlank(email)){
                return  RespBean.error("参数缺失。");
            }
            //亏了呀，早知道用jsr303了
            String regex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
            if(!email.matches(regex)){
                return  RespBean.error("请输入正确的邮箱！");
            }
            //判断当前邮箱是否可以发送邮件，一分钟只发送一次
            Object o = redisUtil.get(email + ":sendTime");
            Object o1 = redisUtil.get(email + ":postIp");
            if(o!=null || o1!=null){
                return RespBean.error("请勿重复发送，一分钟内只能发送一次哦");
            }
            String code = randomCode();
            //sendTime 为发送时间，一个邮箱每分钟只发送一次，其实value是什么都无所谓
            redisUtil.set(email+":sendTime", new Date(),60);
            //postIp 请求人ip,一个ip每分钟只发送一次
            redisUtil.set(email+":postIp", new Date(),60);
            //code 为当前邮箱匹配的验证码有效期为，五分钟，未收到一分钟后即可重发并覆盖本value
            redisUtil.set(email+":code",code,60*5);
            //拷贝来的邮件文案
            String text ="尊敬的用户,您好:\n"
                    + "\n本次请求的邮件验证码为:<p style='color:red'>" + code + "</p>,本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                    + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）";
            //异步发送邮件
            mailUtils.sendHtmlMail(email,"【验证码】黑名单提交",text);
            return RespBean.success("验证码发送成功，请留意邮箱查收！");
        }catch (Exception e){
            logger.error("发送邮件验证码失败！！！",e);
            e.printStackTrace();
            return  RespBean.error("验证码发送失败，请联系管理员或重试");
        }
    }

    /**
     * 生成六位验证码
     * @return
     */
    private String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

}
