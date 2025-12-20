package top.jsls9.oajsfx.onebot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于处理其他 OneBot 事件（如 notice, request, meta_event）的通用注解
 *
 * @author bSu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneBotEventHandler {
    /**
     * 匹配的 post_type (例如: "notice", "request")
     *
     * @return 上报类型
     */
    String postType() default "";
    
    /**
     * 详细类型 sub_type 或 detail_type (例如: "poke", "friend_add")
     *
     * @return 详细类型
     */
    String detailType() default "";
}