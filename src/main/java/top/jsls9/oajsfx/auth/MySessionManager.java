package top.jsls9.oajsfx.auth;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author bSu
 * @date 2021/3/14 - 22:07
 */
public class MySessionManager extends DefaultWebSessionManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String AUTHORIZATION = "Authorization";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //前端ajax的headers中必须传入Authorization的值
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            logger.info("调用MySessionManager获取sessionId="+id);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            logger.info("调用MySessionManager使用默认模式从cookie获取sessionID为"+id);
            return super.getSessionId(request, response);
            //我都用token了，就不允许session了，有需要再一起用
            //return null;
        }
    }

}
