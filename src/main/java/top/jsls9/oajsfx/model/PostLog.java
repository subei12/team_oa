package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * post_log
 * @author 
 */
public class PostLog implements Serializable {
    /**
     * 主键
     */
    private String postId;

    /**
     * 操作结算的帖子id，社区帖子id
     */
    private String hlxPostId;

    /**
     * 所操作帖子的楼主，社区用户id
     */
    private String hlxUserId;

    /**
     * 申请结算的人，存社区用户id
     */
    private String operationHlxUserId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建的类别，0-OA系统内结算、1-社区命令结算
     */
    private String createState;

    /**
     * 结算等级，一共三个等级；1-普通贴、2热贴、3精帖
     */
    private Integer grade;

    /**
     * 结算状态，0-结算成功、1-本次结算操作失败
     */
    private String state;

    private static final long serialVersionUID = 1L;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getHlxPostId() {
        return hlxPostId;
    }

    public void setHlxPostId(String hlxPostId) {
        this.hlxPostId = hlxPostId;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public String getOperationHlxUserId() {
        return operationHlxUserId;
    }

    public void setOperationHlxUserId(String operationHlxUserId) {
        this.operationHlxUserId = operationHlxUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateState() {
        return createState;
    }

    public void setCreateState(String createState) {
        this.createState = createState;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}