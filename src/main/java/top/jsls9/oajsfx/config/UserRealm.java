package top.jsls9.oajsfx.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import top.jsls9.oajsfx.dao.PermissionDao;
import top.jsls9.oajsfx.dao.RoleDao;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.model.Role;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.LoginService;
import top.jsls9.oajsfx.service.PermissionService;
import top.jsls9.oajsfx.service.UserService;

import java.util.Collections;
import java.util.List;

/**
 * @author bSu
 * @date 2020/5/26 - 17:30
 */

//自定义的realm类(权限验证)，继承AuthorizingRealm 即可
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //授权，即角色或者权限验证。
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权--》AuthorizationInfo");
        //获取登录信息，认证成功之后给一个name，这里应该只要是个唯一的值就行，对应下面new SimpleAuthenticationInfo()的第一个参数
        User userLogin = userService.getUserLogin();
        SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();

        //资源权限
        List<Permission> permissions = permissionService.selectAllByUserId(userLogin.getId());
        for(Permission p : permissions){
            info.addStringPermission(p.getPerms());
        }

        List<Role> roles = roleDao.queryRoleByUserName(userLogin.getUsername());
        //移除所有的null元素
        roles.removeAll(Collections.singleton(null));
        //授权
        if(roles==null){
            return info;
        }
        for(Role role : roles){//有可能会包含一个所有属性为空的对象，所以循环之前要做判断
            info.addRole(role.getName());
        }
        return info;
    }

    //身份认证/登录(账号密码验证)。
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证--》doGetAuthenticationInfo");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取用户信息
        User user = loginService.getUserByUserName(token.getUsername());
        if(user==null){
            throw new AccountException("用户名或者密码错误");
        }
        if(user.getState()!=0){
            throw new LockedAccountException ("账号不可用，或已被冻结/删除。");
        }
        //第一个参数明明可以放一个对象，我为什么会放一个userName？真是脑子瓦特了。
        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),getName());
    }
}
