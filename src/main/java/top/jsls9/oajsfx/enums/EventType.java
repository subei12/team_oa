package top.jsls9.oajsfx.enums;

/**
 * @author bSu
 * @date 2022/4/11 - 17:52
 */
public enum EventType {

    /**
     * 私聊
     */
    FRIENDMESSAGE("FriendMessage"),

    /**
     * 群聊
     */
    GROUPMESSAGE("GroupMessage"),

    /**
     * 群临时消息
     */
    TEMPMESSAGE("TempMessage"),

    /**
     * 陌生人消息
     */
    STRANGERMESSAGE("StrangerMessage");

    private String value;

    EventType() {
    }

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
