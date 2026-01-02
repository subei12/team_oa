package top.jsls9.oajsfx.onebot.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * OneBot 快速回复构建器
 * 用于构建符合 OneBot 11 协议的快速响应对象
 *
 * @author Gemini
 */
public class OneBotReplyBuilder {

    private String textMsg;
    private Integer replyMessageId;
    private Long atUserId; // 要艾特的用户ID

    /**
     * 创建一个新的构建器实例
     * @return builder
     */
    public static OneBotReplyBuilder create() {
        return new OneBotReplyBuilder();
    }

    /**
     * 设置回复的文本内容
     * @param text 消息文本
     * @return builder
     */
    public OneBotReplyBuilder text(String text) {
        this.textMsg = text;
        return this;
    }

    /**
     * 设置要引用的消息 ID
     * @param messageId 原消息 ID
     * @return builder
     */
    public OneBotReplyBuilder replyTo(Integer messageId) {
        this.replyMessageId = messageId;
        return this;
    }

    /**
     * 设置要艾特的用户 ID
     * @param userId 用户 QQ 号
     * @return builder
     */
    public OneBotReplyBuilder at(Long userId) {
        this.atUserId = userId;
        return this;
    }

    /**
     * 构建最终的 JSON 对象
     * @return 包含 reply 字段的 JSONObject
     */
    public JSONObject build() {
        JSONObject response = new JSONObject();

        // 如果包含 引用 或 At，必须使用数组格式
        if (replyMessageId != null || atUserId != null) {
            JSONArray messages = new JSONArray();

            // 1. 引用段 (Reply)
            if (replyMessageId != null) {
                JSONObject replyNode = new JSONObject();
                replyNode.put("type", "reply");
                JSONObject replyData = new JSONObject();
                replyData.put("id", replyMessageId.toString());
                replyNode.put("data", replyData);
                messages.add(replyNode);
            }

            // 2. 艾特段 (At)
            if (atUserId != null) {
                JSONObject atNode = new JSONObject();
                atNode.put("type", "at");
                JSONObject atData = new JSONObject();
                atData.put("qq", atUserId.toString());
                atNode.put("data", atData);
                messages.add(atNode);
            }

            // 3. 文本段 (Text)
            if (textMsg != null && !textMsg.isEmpty()) {
                // 如果前面有At，文本前通常加个空格美观一点（可选，这里保持原样）
                JSONObject textNode = new JSONObject();
                textNode.put("type", "text");
                JSONObject textData = new JSONObject();
                textData.put("text", textMsg);
                textNode.put("data", textData);
                messages.add(textNode);
            }

            response.put("reply", messages);
        } else {
            // 简单纯文本回复
            response.put("reply", textMsg);
        }

        // 默认 auto_escape 为 false
        response.put("auto_escape", false);

        return response;
    }
}
