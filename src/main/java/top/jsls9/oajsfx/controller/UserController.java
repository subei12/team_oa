package top.jsls9.oajsfx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jsls9.oajsfx.model.BudgetLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.model.UserRole;
import top.jsls9.oajsfx.service.UserRoleService;
import top.jsls9.oajsfx.service.UserService;
import top.jsls9.oajsfx.utils.RespBean;

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
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
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
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PostMapping("/user")
    public Object addUser(@RequestBody User user){
        try {
            //对用户名进行校验，用户名必须唯一
            int count = userService.getCountByUserName(user.getUsername());
            if(count!=0){
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
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
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
            for(String uId : ids){
                //admin用户只能结算自己团队的帖子
                if(!subject.hasRole("superAdmin")){
                    if(subject.hasRole("admin")){
                        //查询被奖励者团队id
                        User user = userService.queryUserById(id);
                        if(!user.getDeptId().equals(userLogin.getDeptId())){
                            return RespBean.error("删除失败，包含无权限删除用户。");
                        }
                    }else{
                        return RespBean.error("删除失败，包含无权限删除用户。");
                    }
                }
            }
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
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
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
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @GetMapping("/user/{id}")
    public Object getUser(@PathVariable("id") String id){
        if(StringUtils.isBlank(id)){
            return RespBean.error("参数缺失,查询失败");
        }
        User user = userService.queryUserById(id);
        return RespBean.success("查询成功",user);
    }

    @ApiOperation("给用户赋予角色")
    @RequiresRoles(value = {"superAdmin"},logical = Logical.OR)
    @PutMapping("/user/role/{id}")
    public Object updateUserRoleByUserId(@PathVariable("id") String id,@RequestBody Map<String,String> map){
        try {
            String roles = map.get("roles");
            //先删除当前用户的所有角色
            userRoleService.deleteByUserId(id);
            User u = new User();
            if (StringUtils.isNotBlank(roles)){
                //如果checkboxes不为空，就代表此用户被赋予角色
                String[] roleIds = roles.split(",");
                //给用户赋予权限
                for(String roleId : roleIds){
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(roleId);
                    userRole.setUserId(id);
                    userRoleService.insert(userRole);
                }
            }
            return RespBean.success("角色赋予成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("操作，我联系我自己",e.getMessage());
        }
    }

    @ApiOperation("自定义发放奖励")
    @RequiresRoles(value = {"superAdmin","admin"},logical = Logical.OR)
    @PutMapping("/user/reward/{id}")
    public RespBean updateUserRewardByUserId(@PathVariable("id") String id,@RequestBody BudgetLog budgetLog){
        try {
            if(StringUtil.isBlank(budgetLog.getText()) || budgetLog.getSource()==null){
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
            if(!subject.hasRole("superAdmin")){
                if(subject.hasRole("admin")){
                   //查询被奖励者团队id
                   User user = userService.queryUserById(id);
                   if(!user.getDeptId().equals(userLogin.getDeptId())){
                       return RespBean.error("奖励失败，您当前无权限奖励此用户。");
                   }
                }else{
                    return RespBean.error("奖励失败，您当前无权限奖励此用户。");
                }
            }
            //此id为用户id
            budgetLog.setUserId(id);

            budgetLog.setCreateUserId(userLogin.getId());
            userService.updateUserRewardByUserId(budgetLog);
            return RespBean.success("奖励发放成功。");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("奖励失败",e);
            return RespBean.error("奖励失败"+e.getMessage());
        }
    }

}
