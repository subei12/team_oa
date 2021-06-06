package top.jsls9.oajsfx.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.HashMap;
import java.util.Map;

/** jsr303校验全局异常处理
 * @author bSu
 * @date 2021/3/9 - 22:02
 */
@RestControllerAdvice
public class Jsr303ExceptionControllerAdvice {

    @ExceptionHandler(value= {MethodArgumentNotValidException.class , BindException.class})
    public RespBean handleVaildException(Exception e){
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException)e).getBindingResult();
        }
        for(FieldError error : bindingResult.getFieldErrors()){
            //只输出第一个就好，我为什么不直接get(0)呢？
            return new RespBean(400,"非法参数",error.getDefaultMessage());
        }
        return new RespBean(400,"参数错误",null);
    }

}
