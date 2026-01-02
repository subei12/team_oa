package top.jsls9.oajsfx.onebot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.jsls9.oajsfx.onebot.annotation.*;
import top.jsls9.oajsfx.onebot.model.OneBotEvent;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;

import java.lang.reflect.Method;
import java.util.*;

/**
 * OneBot 事件分发器
 * <p>
 * 负责在应用启动时扫描并注册所有 OneBot 事件处理器，
 * 并在接收到事件时将其分发到相应的方法。
 * </p>
 *
 * @author bSu
 */
@Component
public class OneBotDispatcher implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OneBotDispatcher.class);

    @Value("${onebot.global-prefix:}")
    private String globalPrefix;

    private final Map<String, List<HandlerMethod>> handlers = new HashMap<>();

    /**
     * 监听 ContextRefreshedEvent 事件，扫描并注册处理器
     *
     * @param event 上下文刷新事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, Object> controllers = context.getBeansWithAnnotation(OneBotController.class);

        for (Object bean : controllers.values()) {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OneBotGroupHandler.class)) {
                    OneBotGroupHandler annotation = method.getAnnotation(OneBotGroupHandler.class);
                    registerHandler("message.group", bean, method, annotation.keywords());
                } else if (method.isAnnotationPresent(OneBotPrivateHandler.class)) {
                    OneBotPrivateHandler annotation = method.getAnnotation(OneBotPrivateHandler.class);
                    registerHandler("message.private", bean, method, annotation.keywords());
                } else if (method.isAnnotationPresent(OneBotEventHandler.class)) {
                    OneBotEventHandler annotation = method.getAnnotation(OneBotEventHandler.class);
                    String key = annotation.postType();
                    if (StringUtils.hasText(annotation.detailType())) {
                        key += "." + annotation.detailType();
                    }
                    registerHandler(key, bean, method, new String[]{});
                }
            }
        }
    }

    /**
     * 注册处理器方法
     *
     * @param key      事件键 (例如: message.group)
     * @param bean     Bean 实例
     * @param method   处理方法
     * @param keywords 触发关键字
     */
    private void registerHandler(String key, Object bean, Method method, String[] keywords) {
        handlers.computeIfAbsent(key, k -> new ArrayList<>()).add(new HandlerMethod(bean, method, keywords));
        logger.info("Registered OneBot handler: {} -> {} # {}", key, method.getName(), Arrays.toString(keywords));
    }

    /**
     * 分发事件到对应的处理器
     *
     * @param event OneBot 事件对象
     * @return 处理结果
     */
    public Object dispatch(OneBotEvent event) {
        String key = event.getPostType();
        if ("message".equals(key)) {
            OneBotMessageEvent messageEvent = (OneBotMessageEvent) event;
            key += "." + messageEvent.getMessageType(); // message.group or message.private
        }
        // 可以扩展处理 notice/request 的子类型

        List<HandlerMethod> methods = handlers.get(key);
        if (methods == null || methods.isEmpty()) {
            return null;
        }

        for (HandlerMethod handler : methods) {
            if (handler.matches(event, globalPrefix)) {
                try {
                    return handler.method.invoke(handler.bean, event);
                } catch (java.lang.reflect.InvocationTargetException ite) {
                    // 解包反射异常，获取真实的业务异常
                    logger.error("Error executing OneBot handler: {}.{}", 
                            handler.bean.getClass().getSimpleName(), 
                            handler.method.getName(), 
                            ite.getTargetException());
                } catch (Exception e) {
                    logger.error("Error invoking OneBot handler: {}.{}", 
                            handler.bean.getClass().getSimpleName(), 
                            handler.method.getName(), 
                            e);
                }
            }
        }
        return null;
    }

    /**
     * 内部类：封装处理器方法信息
     */
    private static class HandlerMethod {
        final Object bean;
        final Method method;
        final String[] keywords;

        HandlerMethod(Object bean, Method method, String[] keywords) {
            this.bean = bean;
            this.method = method;
            this.keywords = keywords;
        }

        /**
         * 检查事件是否匹配当前处理器
         *
         * @param event        事件对象
         * @param globalPrefix 全局命令前缀
         * @return 是否匹配
         */
        boolean matches(OneBotEvent event, String globalPrefix) {
            if (event instanceof OneBotMessageEvent) {
                OneBotMessageEvent messageEvent = (OneBotMessageEvent) event;
                String matchText = extractMessageText(messageEvent);
                if (!StringUtils.hasText(matchText)) return false;
                
                // 1. 检查全局前缀
                if (StringUtils.hasText(globalPrefix)) {
                    if (!matchText.startsWith(globalPrefix)) {
                        return false;
                    }
                    matchText = matchText.substring(globalPrefix.length());
                }
                
                // 2. 处理前缀@，群聊@机器人时也可匹配命令
                matchText = stripLeadingAt(matchText);
                if (keywords == null || keywords.length == 0) {
                    return true; // 没有关键字限制，则匹配所有（前提是已通过全局前缀检查）
                }

                for (String keyword : keywords) {
                    if (matchText.startsWith(keyword)) {
                        return true;
                    }
                }
                return false;
            }
            return true; // 非消息事件直接匹配类型（因为已经在 dispatch 的 map 中筛选过了）
        }

        /**
         * 从 message 字段提取首个文本内容
         *
         * @param event 消息事件
         * @return 首个文本消息（去除首尾空格）
         */
        private static String extractMessageText(OneBotMessageEvent event) {
            Object message = event.getMessage();
            if (message instanceof String) {
                String text = ((String) message).trim();
                return StringUtils.hasText(text) ? text : null;
            }
            if (message instanceof JSONArray) {
                JSONArray nodes = (JSONArray) message;
                for (Object node : nodes) {
                    if (node instanceof JSONObject) {
                        JSONObject jsonNode = (JSONObject) node;
                        if ("text".equals(jsonNode.getString("type"))) {
                            JSONObject data = jsonNode.getJSONObject("data");
                            if (data != null) {
                                String text = data.getString("text");
                                if (StringUtils.hasText(text)) {
                                    return text.trim();
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }

        /**
         * 移除消息开头的 CQ @ 代码，保证命令关键词能匹配
         *
         * @param message 原始消息
         * @return 去除前缀@的消息
         */
        private static String stripLeadingAt(String message) {
            if (!StringUtils.hasText(message)) {
                return message;
            }
            String result = message;
            while (result.startsWith("[CQ:at,")) {
                int end = result.indexOf("]");
                if (end < 0) {
                    break;
                }
                result = result.substring(end + 1).trim();
            }
            return result;
        }
    }
}
