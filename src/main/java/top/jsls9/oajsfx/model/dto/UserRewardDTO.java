package top.jsls9.oajsfx.model.dto;

public class UserRewardDTO {

    /**
     * 葫芦侠用户ID
     */
    private String hlxUserId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 数量
     */
    private Integer source;
    /**
     * 原因
     */
    private String reason;
    /**
     * 错误信息
     */
    private String error;

    // Getters and Setters

    public String getHlxUserId() {
        return hlxUserId;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
