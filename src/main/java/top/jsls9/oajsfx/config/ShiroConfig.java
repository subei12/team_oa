package top.jsls9.oajsfx.config;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jsls9.oajsfx.auth.MySessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bSu
 * @date 2020/5/26 - 17:35
 */

/**
 * 哈哈哈，网上拷贝来的，这怎么记得住
 */
@Configuration//声明为配置类
public class ShiroConfig {


    //创建userRealm
    //将自己的验证方式加入容器
    @Bean
    public UserRealm myShiroRealm() {
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }


    // 配置org.apache.shiro.web.session.mgt.DefaultWebSessionManager(shiro session的管理)
    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager() {
        /*DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(1000 * 60 * 30);// 会话过期时间，单位：毫秒(在无操作时开始计时)--->一分钟,用于测试
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);*/
        MySessionManager mySessionManager = new MySessionManager();
        return mySessionManager;
        //return defaultWebSessionManager;
    }


    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("myShiroRealm") UserRealm myShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义session管理
        securityManager.setSessionManager(getDefaultWebSessionManager());
        //关联userRealm
        securityManager.setRealm(myShiroRealm);
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        //登出
        //map.put("/logout", "logout");
        //对所有用户认证
        //map.put("/user/add.html", "authc");//anno所有用户可访问，更多在底部
        //map.put("/user/update.html", "authc");//anno所有用户可访问，更多在底部
        //授权
        /*map.put("/user/add.html","perms[user_add]");
        map.put("/userAdd","perms[user_add]");*/
        //登录，未通过认证的跳转到登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //首页
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        //shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }




     /*
     /admin*//**=anon ：无参，表示可匿名访问
     /admin/user/**=authc ：无参，表示需要认证才能访问
     /admin/user/**=authcBasic ：无参，表示需要httpBasic认证才能访问
     /admin/user/**=ssl ：无参，表示需要安全的URL请求，协议为https
     /home=user ：表示用户不一定需要通过认证，只要曾被 Shiro 记住过登录状态就可以正常发起 /home 请求
     /edit=authc,perms[admin:edit]：表示用户必需已通过认证，并拥有 admin:edit 权限才可以正常发起 /edit 请求
     /admin=authc,roles[admin] ：表示用户必需已通过认证，并拥有 admin 角色才可以正常发起 /admin 请求
     /admin/user/**=port[8081] ：当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString
     /admin/user/**=rest[user] ：根据请求方式来识别，相当于 /admins/user/**=perms[user:get]或perms[user:post] 等等
     /admin**=roles["admin,guest"] ：允许多个参数（逗号分隔），此时要全部通过才算通过，相当于hasAllRoles()
     /admin**=perms["user:add:*,user:del:*"]：允许多个参数（逗号分隔），此时要全部通过才算通过，相当于isPermitedAll()
    */

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    //开启对shior注解的支持（aop）
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
