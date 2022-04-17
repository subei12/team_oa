/**
 * Copyright 2022 bejson.com
 */
package top.jsls9.oajsfx.hlxPojo.qqPojo;
import java.util.List;

/** webHook上报时接收消息
 * Auto-generated: 2022-04-16 18:54:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class MessageRootBean {

    private String type;
    private Sender sender;
    private Subject subject;
    private List<MessageChain> messageChain;
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }
    public Sender getSender() {
        return sender;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    public Subject getSubject() {
        return subject;
    }

    public void setMessageChain(List<MessageChain> messageChain) {
        this.messageChain = messageChain;
    }
    public List<MessageChain> getMessageChain() {
        return messageChain;
    }

}