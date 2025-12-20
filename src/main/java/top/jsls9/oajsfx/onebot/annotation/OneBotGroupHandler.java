package top.jsls9.oajsfx.onebot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于处理 OneBot 群消息的注解
 *
 * @author bSu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneBotGroupHandler {
    /**
     * 触发关键字
     * <p>
     * 如果消息（去除全局前缀后）以任意一个关键字开头，则匹配。
     * 如果为空，则匹配所有群消息（受后续过滤器影响）。
     * </p>
     *
     * @return 关键字数组
     */
    String[] keywords() default {};
}