/**
 * Copyright 2022 bejson.com
 */
package top.jsls9.oajsfx.hlxPojo.qqPojo;
import java.util.List;

/**
 * Auto-generated: 2022-04-17 15:18:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class NodeList {

    private long senderId;
    private long time;
    private String senderName;
    private List<String> messageChain;
    private long messageId;
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }
    public long getSenderId() {
        return senderId;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public long getTime() {
        return time;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getSenderName() {
        return senderName;
    }

    public void setMessageChain(List<String> messageChain) {
        this.messageChain = messageChain;
    }
    public List<String> getMessageChain() {
        return messageChain;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
    public long getMessageId() {
        return messageId;
    }

}