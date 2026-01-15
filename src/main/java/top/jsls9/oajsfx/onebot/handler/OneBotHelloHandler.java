package top.jsls9.oajsfx.onebot.handler;

import com.alibaba.fastjson.JSONObject;
import top.jsls9.oajsfx.onebot.annotation.OneBotController;
import top.jsls9.oajsfx.onebot.annotation.OneBotPrivateHandler;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;
import top.jsls9.oajsfx.onebot.utils.OneBotReplyBuilder;

/**
 * OneBot 示例处理器
 *
 * @author bSu
 */
@OneBotController
public class OneBotHelloHandler {

    /**
     * 监听私聊 "hello" 消息并自动回复 "嗨"
     *
     * @param event 私聊消息事件
     * @return 用于快速回复的 JSON 对象
     */
    @OneBotPrivateHandler(keywords = "hello")
    public JSONObject handlePrivateHello(OneBotPrivateMessageEvent event) {
        try {
            // 使用 Builder 构建引用回复 + 艾特发送者
            return OneBotReplyBuilder.create()
                    .replyTo(event.getMessageId()) // 引用原消息
                    .at(event.getUserId())         // 艾特发送者
                    .text(" 嗨")                   // 回复内容
                    .build();
        } catch (Exception e) {
            // 捕获所有异常，防止反射调用崩溃
            e.printStackTrace(); // 或者使用 logger.error("处理 hello 消息失败", e);
            return null;
        }
    }
}
