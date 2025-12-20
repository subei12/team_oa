package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.onebot.core.OneBotDispatcher;
import top.jsls9.oajsfx.onebot.model.OneBotEvent;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;

/**
 * OneBot WebHook 控制器
 * <p>
 * 接收来自 OneBot 客户端的 HTTP POST 事件上报。
 * </p>
 *
 * @author bSu
 */
@RestController
public class OneBotWebHookController {

    private static final Logger logger = LoggerFactory.getLogger(OneBotWebHookController.class);

    @Autowired
    private OneBotDispatcher dispatcher;

    /**
     * 处理 OneBot WebHook 请求
     *
     * @param json 原始 JSON 数据
     * @return 处理结果
     */
    @PostMapping("/onebot/webhook")
    public Object handleWebhook(@RequestBody JSONObject json) {
        logger.info("Received OneBot event: {}", json.toJSONString());

        String postType = json.getString("post_type");
        OneBotEvent event = null;

        if ("message".equals(postType)) {
            String messageType = json.getString("message_type");
            if ("group".equals(messageType)) {
                event = json.toJavaObject(OneBotGroupMessageEvent.class);
            } else if ("private".equals(messageType)) {
                event = json.toJavaObject(OneBotPrivateMessageEvent.class);
            } else {
                event = json.toJavaObject(OneBotMessageEvent.class);
            }
        } else {
            // 对于其他类型（notice, request, meta_event），暂时使用基类
            // 理想情况下也应该有对应的子类，目前先用基类接收，后续可扩展
            event = json.toJavaObject(OneBotEvent.class);
        }

        if (event != null) {
            return dispatcher.dispatch(event);
        }

        return null;
    }
}