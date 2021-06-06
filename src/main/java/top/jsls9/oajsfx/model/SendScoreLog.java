package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * send_score_log
 * @author 
 */
public class SendScoreLog implements Serializable {
    /**
     * 主键
     */
    private String sendScoreLogId;

    /**
     * 赠送葫芦操作人
     */
    private String userName;

    /**
     * 所得葫芦的用户在社区的id
     */
    private String hlxUserId;

    /**
     * 所得葫芦的帖子
     */
    private String hlxPostId;

    /**
     * 所得葫芦的数量
     */
    private Integer sourceNumber;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 返回消息，赠送成功为空
     */
    private String msg;

    /**
     * 状态；0-成功、1-失败
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public String getSendScoreLogId() {
        return sendScoreLogId;
    }

    public void setSendScoreLogId(String sendScoreLogId) {
        this.sendScoreLogId = sendScoreLogId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public String getHlxPostId() {
        return hlxPostId;
    }

    public void setHlxPostId(String hlxPostId) {
        this.hlxPostId = hlxPostId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(Integer sourceNumber) {
        this.sourceNumber = sourceNumber;
    }
}