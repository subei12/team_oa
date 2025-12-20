package top.jsls9.oajsfx.onebot.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * OneBot 消息事件基类
 *
 * @author bSu
 */
public class OneBotMessageEvent extends OneBotEvent {
    /**
     * 消息类型 (group, private)
     */
    @JSONField(name = "message_type")
    private String messageType;

    /**
     * 消息子类型 (normal, anonymous, notice 等)
     */
    @JSONField(name = "sub_type")
    private String subType;

    /**
     * 消息 ID
     */
    @JSONField(name = "message_id")
    private Integer messageId;

    /**
     * 发送者 QQ 号
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 消息内容
     */
    @JSONField(name = "message")
    private Object message; // Can be String or JSONArray

    /**
     * 原始消息内容
     */
    @JSONField(name = "raw_message")
    private String rawMessage;

    /**
     * 字体
     */
    @JSONField(name = "font")
    private Integer font;

    /**
     * 发送者信息
     */
    @JSONField(name = "sender")
    private Object sender; // Map or Object

    /**
     * 获取消息类型
     * @return 消息类型
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型
     * @param messageType 消息类型
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取消息子类型
     * @return 子类型
     */
    public String getSubType() {
        return subType;
    }

    /**
     * 设置消息子类型
     * @param subType 子类型
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * 获取消息 ID
     * @return 消息 ID
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * 设置消息 ID
     * @param messageId 消息 ID
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取发送者 QQ 号
     * @return 发送者 QQ
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置发送者 QQ 号
     * @param userId 发送者 QQ
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取消息内容
     * @return 消息内容
     */
    public Object getMessage() {
        return message;
    }

    /**
     * 设置消息内容
     * @param message 消息内容
     */
    public void setMessage(Object message) {
        this.message = message;
    }

    /**
     * 获取原始消息
     * @return 原始消息
     */
    public String getRawMessage() {
        return rawMessage;
    }

    /**
     * 设置原始消息
     * @param rawMessage 原始消息
     */
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    /**
     * 获取字体
     * @return 字体
     */
    public Integer getFont() {
        return font;
    }

    /**
     * 设置字体
     * @param font 字体
     */
    public void setFont(Integer font) {
        this.font = font;
    }

    /**
     * 获取发送者信息
     * @return 发送者信息对象
     */
    public Object getSender() {
        return sender;
    }

    /**
     * 设置发送者信息
     * @param sender 发送者信息对象
     */
    public void setSender(Object sender) {
        this.sender = sender;
    }
}