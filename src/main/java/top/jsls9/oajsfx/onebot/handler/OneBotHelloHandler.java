package top.jsls9.oajsfx.onebot.handler;

import com.alibaba.fastjson.JSONObject;
import top.jsls9.oajsfx.onebot.annotation.OneBotController;
import top.jsls9.oajsfx.onebot.annotation.OneBotPrivateHandler;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;

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
        // 构建 OneBot 11 快速回复的 JSON 对象
        // 格式参考：https://github.com/botuniverse/onebot-11/blob/master/communication/http.md#%E5%quick-reply
        JSONObject response = new JSONObject();
        response.put("reply", "嗨");
        // 可选：如果要回复原消息（引用回复），有些实现可能需要 auto_escape
        response.put("auto_escape", true);
        
        return response;
    }
}
