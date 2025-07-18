package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.model.UserRole;
import top.jsls9.oajsfx.service.UserRoleService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.RespBean;

import java.io.IOException;
import java.util.Map;

/**
 * @author bSu
 * @date 2021/5/4 - 21:35
 */
@Api(tags = "社区用户管理接口")
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String ZZS = "[0-9]+$";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 查询所有用户
     * @param page
     * @param perPage
     * @param user
     * @return
     */
    @ApiOperation("查询所有用户")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/users")
    public RespBean getUsers(@RequestParam Integer page, @RequestParam Integer perPage, User user){
        try {
           return RespBean.success("查询成功",userService.queryUsersByPageAndUser(page,perPage,user));
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @ApiOperation("添加用户")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PostMapping("/user")
    public Object addUser(@RequestBody User user){
        try {
            //对用户名进行校验，用户名必须唯一
            int count = userService.getCountByUserName(user.getQq());//默认使用qq作为用户名
            if(count != 0){
                return RespBean.error("用户名已存在，请重试");
            }
            if(user.getHlxUserId().length()>10 || user.getQq().length()>11 ){
                return RespBean.error("录入信息不合规则，请仔细检查后重试");
            }
            //团队不可为空
            if(StringUtils.isBlank(user.getDeptId())){
                return RespBean.error("团队不可为空！！！");
            }
            user.setUsername(user.getQq());
            userService.insetUser(user);
            return RespBean.success("添加成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("添加失败，请联系管理员",e.getMessage());
        }
    }

    /**l
    * 批量删除用户
     * @param id
     * @return
     */
    @ApiOperation("批量删除用户")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @DeleteMapping("/user/{id}")
    public Object delUsers(@PathVariable("id") String id){
        //获得当前登录用户对象
        Subject subject = SecurityUtils.getSubject();
        User userLogin = userService.getUserLogin();//获取登录用户

        if(StringUtils.isBlank(id)){
            return RespBean.error("参数缺失,删除失败");
        }
        try {
            String[] ids = id.split(",");
            /**
             * 移除数据权限控制，没错，摆烂了。数据权限控制太麻烦了，只在查询里控制了。
             */
            userService.delUserById(ids);
            return RespBean.success("删除成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("删除失败",null);
        }
    }

    /**
     * 修改用户信息
     * @param user
     * @param id
     * @return
     */
    @ApiOperation("修改用户信息")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PutMapping("/user/{id}")
    public Object updateUser(@RequestBody User user,@PathVariable("id") String id){
        if(StringUtils.isBlank(id)){
            return RespBean.error("参数缺失,修改失败");
        }
        userService.updateUserById(user);
        return RespBean.success("修改成功");
    }

    /**
     * 通过userId查询用户
     * @param id
     * @return
     */
    @ApiOperation("通过userId查询用户")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/user/{id}")
    public Object getUser(@PathVariable("id") String id) throws IOException {
        if(StringUtils.isBlank(id)){
            return RespBean.error("参数缺失,查询失败");
        }
        User user = userService.queryUserById(id);
        return RespBean.success("查询成功",user);
    }

    // 重构角色、权限管理
    @ApiOperation("给用户赋予角色")
    //@RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    @PutMapping("/user/role/{id}")
    public Object updateUserRoleByUserId(@PathVariable("id") String id,@RequestBody Map<String,String> map){
        try {
            String roles = map.get("roles");
            User u = new User();
            userService.giveRoleByRolesAndUserId(roles, id);
            return RespBean.success("角色赋予成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("操作失败，请联系系统管理员。",e.getMessage());
        }
    }

    @ApiOperation("自定义发放奖励")
    //@RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PutMapping("/user/reward/{id}")
    public RespBean updateUserRewardByUserId(@PathVariable("id") String id,@RequestBody BudgetLog budgetLog){
        try {
            if(StringUtils.isBlank(budgetLog.getText()) || budgetLog.getSource()==null){
                return RespBean.error("参数缺失，奖励失败。");
            }
            boolean matches = String.valueOf(budgetLog.getSource()).matches(ZZS);
            if(budgetLog.getSource()<=0 || !matches){
                return RespBean.error("奖励失败，奖励数量需要为正整数");
            }

            //获得当前登录用户对象
            Subject subject = SecurityUtils.getSubject();
            User userLogin = userService.getUserLogin();//获取登录用户
            //admin用户只能结算自己团队的帖子
            /*if(!subject.hasRole("superAdmin")){
                if(subject.hasRole("admin")){
                   //查询被奖励者团队id
                   User user = userService.queryUserById(id);
                   if(!user.getDeptId().equals(userLogin.getDeptId())){
                       return RespBean.error("奖励失败，您当前无权限奖励此用户。");
                   }
                }else{
                    return RespBean.error("奖励失败，您当前无权限奖励此用户。");
                }
            }*/
            //此id为用户id
            budgetLog.setUserId(id);

            budgetLog.setCreateUserId(userLogin.getId());
            userService.updateUserRewardByUserId(budgetLog);
            return RespBean.success("奖励发放成功。");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("奖励失败",e);
            return RespBean.error("奖励失败；原因："+e.getMessage());
        }
    }

    @ApiOperation("查询登录用户信息")
    @GetMapping("/user/my")
    @RequiresAuthentication
    public RespBean queryLoginUser() throws IOException {
        User userLogin = userService.getUserLogin();
        User user = userService.queryUserById(userLogin.getId());
        return RespBean.success("查询成功",user);
    }

    /**
     * 提现OA账户内得剩余葫芦，获得葫芦的id可自定义（错了不退）。
     * @param id    提现到得葫芦侠id
     * @param count 提现得数量
     * @return
     * @throws IOException
     */
    @ApiOperation("OA账户剩余葫芦提现")
    @PostMapping("/user/gourd/withdrawal/{id}/{count}")
    public RespBean withdrawal(@PathVariable("id") String id, @PathVariable("count") Integer count) throws IOException {
        if(StringUtils.isBlank(id) || count <= 0){
            return RespBean.error("参数异常，请检查后重试");
        }
        String result = userService.withdrawal(id, count);
        if(StringUtils.isNotBlank(result)){
            return RespBean.error(result);
        }
        return RespBean.success("提取成功");
    }


    //无效分页
    @ApiOperation("查看各团队一个月内未发帖情况")
    @GetMapping("/user/posSituationt")
    public RespBean getUsersPosSituationtByDeptId(@RequestParam Integer page, @RequestParam Integer perPage, User user){
        try {
            return RespBean.success("查询成功",userService.getUsersPosSituationtByDeptId(page,perPage,user));
        }catch (Exception e){
            logger.error("查询失败",e.getMessage());
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    @ApiOperation("批量发放奖励")
    @PostMapping("/user/batch-reward")
    public RespBean batchUpdateUserReward(@RequestParam("file") MultipartFile file) {
        try {
            return userService.batchUpdateUserReward(file);
        } catch (Exception e) {
            logger.error("批量奖励失败", e);
            return RespBean.error("批量奖励失败: " + e.getMessage());
        }
    }


}
