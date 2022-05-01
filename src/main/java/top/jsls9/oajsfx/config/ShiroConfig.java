package top.jsls9.oajsfx.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jsls9.oajsfx.auth.MyPathMatchingFilterChainResolver;
import top.jsls9.oajsfx.auth.MyPermissionsAuthorizationFilter;
import top.jsls9.oajsfx.auth.MySessionManager;
import top.jsls9.oajsfx.auth.MyShiroFilterFactoryBean;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.PermissionService;
import top.jsls9.oajsfx.service.UserService;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    private static final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * 区分url method
     */
    public final static String SEPARATOR_SHIRO_PERM = "==";

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

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
        mySessionManager.setSessionDAO(redisSessionDAO());
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
        //配置自定义redisCacheManager
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new MyShiroFilterFactoryBean();
        //配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //更换过滤器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("perms", new MyPermissionsAuthorizationFilter());

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

        //获取用户的所有权限，上面的其实一直没什么用
        Map<String, String> urlPermsMap = getUrlPermsMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(urlPermsMap);
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

    //配置redisCacheManager
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    //配置redisManager
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host+":"+port);
        redisManager.setDatabase(database);
        redisManager.setPassword(password);
        return redisManager;
    }

    //配置redisSessionDAO
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /* 拓展权限 */


    /**
     * 获取用户权限
     * @return
     */
    public Map<String, String> getUrlPermsMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/login", "anon");


        List<Permission> menus = permissionService.selectAll();
        for (Permission menu : menus) {
            //url先不加context-path: /api
            String url = menu.getUrl();
            String perms = "perms[" + menu.getPerms() + "]";
            filterChainDefinitionMap.put(url, perms);
        }
        /* 我这个除配置的url外其他都不需要登录 */
        //filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }


}
