package top.jsls9.oajsfx.model.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.utils.QqSendMsgUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 改名上报
 * @author bSu
 * @date 2021/8/24 - 22:20
 */
@Component
public class ReNameReportEntity extends ReportBaseEntity{

    /**
     * oa
     * 系统用户id
     */
    private String id;

    /**
     * 社区用户id
     */
    private String hlxUserId;

    /**
     * 原名
     */
    private String nick;

    /**
     * 要修改的名字
     */
    private String newNick;

    @Autowired
    private QqSendMsgUtils qqSendMsgUtils;

    public String getId() {
        return id;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }

    public String getNick() {
        return nick;
    }

    public String getNewNick() {
        return newNick;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setNewNick(String newNick) {
        this.newNick = newNick;
    }

    public void doReport() throws IOException {
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info("+++++++++++++++");
        log.info("改名上报中..........");

        List<MessageChain> messageChainList = new ArrayList<>();
        MessageChain messageChain = new MessageChain();
        messageChain.setType("Plain");
        //正式上报
        String text = "版块：技术分享\n" +
                "ID："+this.hlxUserId+"\n" +
                "原名："+this.nick+"\n" +
                "改名："+this.newNick+"\n";
        messageChain.setText(text);
        messageChainList.add(messageChain);
        qqSendMsgUtils.sendGroupMessage(this.group,messageChainList);

        log.info("改名上报完成..........");
        log.info("---------------");
    }

}
