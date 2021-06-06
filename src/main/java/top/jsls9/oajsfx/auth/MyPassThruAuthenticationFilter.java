package top.jsls9.oajsfx.auth;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/** auth包内代码来自https://www.jianshu.com/p/9a057ad53f57
 *  稍作改动就解决了一下午的困惑，果然牛逼
 *  必应牛逼
 * @author bSu
 * @date 2021/3/14 - 22:08
 */
@Component
public class MyPassThruAuthenticationFilter extends PassThruAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    //获取请求方法，若为OPTIONS请求直接返回True放行
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        log.info("进入MyPassThruAuthenticationFilter");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            log.info("OPTIONS方法直接返回True");
            return true;
        }
        return super.onPreHandle(request, response, mappedValue);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResp = WebUtils.toHttp(response);
        HttpServletRequest httpReq = WebUtils.toHttp(request);

        /** 系统重定向会默认把请求头清空，这里通过拦截器重新设置请求头，解决跨域问题 */
        httpResp.addHeader("Access-Control-Allow-Origin", httpReq.getHeader("Origin"));
        httpResp.addHeader("Access-Control-Allow-Headers", "*");
        httpResp.addHeader("Access-Control-Allow-Methods", "*");
        httpResp.addHeader("Access-Control-Allow-Credentials", "true");

        if (isLoginRequest(request, response)) {
            return true;
        } else {
            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }
}
