package top.jsls9.oajsfx.model.entity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.utils.QqSendMsgUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**称号上报实体
 * @author bSu
 * @date 2021/8/14 - 20:48
 */
@Component
public class TitleReportEntity extends ReportBaseEntity{


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
     * 目前称号
     */
    private String title;

    /**
     * 需要上报的称号，上报时title需为空
     */
    private String newTitle;

    /**
     * 上、下称号
     * true上，false下
     */
    private boolean isTop;

    /**
     * 操作原因
     */
    private String remark;



    @Autowired
    private QqSendMsgUtils qqSendMsgUtils;

    Logger log = LoggerFactory.getLogger(this.getClass());

    public String getId() {
        return id;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }


    public String getNewTitle() {
        return newTitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }


    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void doReport() throws IOException {

        log.info("+++++++++++++++");
        log.info("上报中..........");
        List<MessageChain> messageChainList = new ArrayList<>();
        MessageChain messageChain = new MessageChain();
        messageChain.setType("Plain");
        //正式上报
        String text = "版块：技术分享\n" +
                "\n" +
                "ID："+this.hlxUserId+"\n" +
                "\n" +
                "处理："+(this.isTop?"上":"下")+"【"+(this.isTop?""+this.getNewTitle()+"":""+this.getTitle()+"")+"】称号\n" +
                "\n" +
                "原因："+this.remark+"";
        messageChain.setText(text);
        messageChainList.add(messageChain);
        qqSendMsgUtils.sendGroupMessage(this.group,messageChainList);


        log.info("上报完成..........");
        log.info("---------------");
    }


}
