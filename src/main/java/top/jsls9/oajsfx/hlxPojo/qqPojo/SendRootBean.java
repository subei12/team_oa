package top.jsls9.oajsfx.hlxPojo.qqPojo;

import java.util.List;

/** webHook上报时发送消息
 * @author bSu
 * @date 2022/4/17 - 16:30
 */
public class SendRootBean {

    /**
     * 命令字，如：sendFriendMessage=好友消息
     */
    private String command;

    /**
     * 命令内容
     */
    private Content content;


    public String getCommand() {
        return command;
    }

    public Content getContent() {
        return content;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
