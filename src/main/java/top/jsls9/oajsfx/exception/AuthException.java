package top.jsls9.oajsfx.exception;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.Collections;

/** 对用户认证及授权的全局异常处理
 * @author bSu
 * @date 2021/5/4 - 17:12
 */
@RestControllerAdvice
public class AuthException {

    @ExceptionHandler(value= {AuthorizationException.class})
    public RespBean handleAuthException(Exception e){
        //UnauthorizedException继承的AuthorizationException，所以要放在前面
        if (e instanceof UnauthorizedException) {
            return RespBean.error(403,"亲，无权限哦。", Collections.emptyList());
        }else if (e instanceof AccountException) {
            return RespBean.error("用户名或者密码错误。");
        } else if (e instanceof LockedAccountException) {
            return RespBean.error("账号不可用，或已被冻结/删除。");
        } else if (e instanceof AuthorizationException) {
            return RespBean.error(401,"未登录，请登录后重试。",null);
        }
        return RespBean.error("亲，异常了哦。",null);
    }

}
