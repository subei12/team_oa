package top.jsls9.oajsfx.model.dto;

public class UserRewardDTO {

    private String hlxUserId;
    private String nickName;
    private Integer source;
    private String reason;
    private String error; // For storing error messages per row

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
