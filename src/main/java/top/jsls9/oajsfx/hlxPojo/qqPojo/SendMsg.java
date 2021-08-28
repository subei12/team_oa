package top.jsls9.oajsfx.hlxPojo.qqPojo;

import java.util.List;

/**
 * @author bSu
 * @date 2021/8/28 - 17:58
 */
public class SendMsg {

    private String sessionKey;
    private String target;
    private List<MessageChain> messageChain;

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setMessageChain(List<MessageChain> messageChain) {
        this.messageChain = messageChain;
    }

    public List<MessageChain> getMessageChain() {
        return messageChain;
    }

}