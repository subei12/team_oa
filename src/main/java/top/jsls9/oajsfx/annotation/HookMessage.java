package top.jsls9.oajsfx.annotation;

import top.jsls9.oajsfx.enums.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bSu
 * @date 2022/4/11 - 17:49
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HookMessage {

    EventType[] event() default {};

    String plain() default "";

}
