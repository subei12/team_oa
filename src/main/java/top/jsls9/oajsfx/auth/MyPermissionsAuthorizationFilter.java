package top.jsls9.oajsfx.auth;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jsls9.oajsfx.config.ShiroConfig;
import top.jsls9.oajsfx.utils.RespBean;
import top.jsls9.oajsfx.utils.WebHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collections;

/** 本次权限-url映射修改，代码主要来源：https://cloud.tencent.com/developer/article/1372836
 * @author bSu
 * @date 2022/4/20 - 17:15
 */
public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    private static final Logger log = LoggerFactory
            .getLogger(MyPermissionsAuthorizationFilter.class);

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);

        String[] strings = path.split(ShiroConfig.SEPARATOR_SHIRO_PERM);

        if (strings.length <= 1) {
            // 普通的 URL, 正常处理
            return this.pathsMatch(strings[0], requestURI);
        } else {
            // 获取当前请求的 http method.
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();

            // 匹配当前请求的 http method 与 过滤器链中的的是否一致
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
        }
    }


    /**
     *  是否是拒绝登录，未登录或者没有权限会走这个方法
     * 来源：https://wenku.baidu.com/view/69cbe8fc6c1aff00bed5b9f3f90f76c660374c51.html
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        // 如果未登录
        if (subject.getPrincipal() == null) {
            //未登录
            log.info("自定义onAccessDenied，未登录");
            log.info("用户: [{}] 请求 restful url : {}, 未登录被拦截.", subject.getPrincipal(), this.getPathWithinApplication(request));//没登录要这个用户干嘛？？？
            //throw new RuntimeException("自定义onAccessDenied，未登录");//为什么捕获不到这里的异常？已经被捕获过了

            WebHelper.writeJson(
                    RespBean.error(401,"未登录，请登录后重试。",null),
                    response);
        } else {
            // 如果已登陆, 但没有权限
            log.info("自定义onAccessDenied，已登录，无权限");
            //throw new UnauthorizedException("自定义onAccessDenied，未登录");
            WebHelper.writeJson(
                    RespBean.error(403,"亲，无权限哦。", Collections.emptyList()),
                    response);
        }
        return false;
    }


    /**
     * 当没有权限被拦截时:
     * 如果是 AJAX 请求, 则返回 JSON 数据.
     *  如果是普通请求, 则跳转到配置 UnauthorizedUrl 页面.
     */
    /*@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        // 如果未登录
        if (subject.getPrincipal() == null) {
            // AJAX 请求返回 JSON
            if (im.zhaojun.util.WebUtils.isAjaxRequest(WebUtils.toHttp(request))) {
                if (log.isDebugEnabled()) {
                    log.debug("用户: [{}] 请求 restful url : {}, 未登录被拦截.", subject.getPrincipal(), this.getPathWithinApplication(request));                }
                Map<String, Object> map = new HashMap<>();
                map.put("code", -1);
                im.zhaojun.util.WebUtils.writeJson(map, response);
            } else {
                // 其他请求跳转到登陆页面
                saveRequestAndRedirectToLogin(request, response);
            }
        } else {
            // 如果已登陆, 但没有权限
            // 对于 AJAX 请求返回 JSON
            if (im.zhaojun.util.WebUtils.isAjaxRequest(WebUtils.toHttp(request))) {
                if (log.isDebugEnabled()) {
                    log.debug("用户: [{}] 请求 restful url : {}, 无权限被拦截.", subject.getPrincipal(), this.getPathWithinApplication(request));
                }

                Map<String, Object> map = new HashMap<>();
                map.put("code", -2);
                map.put("msg", "没有权限啊!");
                im.zhaojun.util.WebUtils.writeJson(map, response);
            } else {
                // 对于普通请求, 跳转到配置的 UnauthorizedUrl 页面.
                // 如果未设置 UnauthorizedUrl, 则返回 401 状态码
                String unauthorizedUrl = getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }

        }
        return false;
    }
*/
}
