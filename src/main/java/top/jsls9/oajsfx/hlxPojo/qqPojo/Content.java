/**
 * Copyright 2022 bejson.com
 */
package top.jsls9.oajsfx.hlxPojo.qqPojo;
import java.util.List;

/**
 * Auto-generated: 2022-04-17 18:9:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Content {

    private long target;
    private long qq;
    private long group;
    private long subject;
    private String kind;
    private List<MessageChain> messageChain;
    public void setTarget(long target) {
        this.target = target;
    }
    public long getTarget() {
        return target;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }
    public long getQq() {
        return qq;
    }

    public void setGroup(long group) {
        this.group = group;
    }
    public long getGroup() {
        return group;
    }

    public void setSubject(long subject) {
        this.subject = subject;
    }
    public long getSubject() {
        return subject;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getKind() {
        return kind;
    }

    public void setMessageChain(List<MessageChain> messageChain) {
        this.messageChain = messageChain;
    }
    public List<MessageChain> getMessageChain() {
        return messageChain;
    }

}