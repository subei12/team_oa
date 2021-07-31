package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * budget_log
 * @author 
 */
public class BudgetLog implements Serializable {
    /**
     * 主键
     */
    private String budgetLogId;

    /**
     * 团队id
     */
    private String deptId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 操作数量，正为加、负为减
     */
    private Integer source;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建用户id
     */
    private String createUserId;

    /**
     * 修改时填入的理由
     */
    private String text;


    private static final long serialVersionUID = 1L;

    public BudgetLog() {
    }

    public BudgetLog(String budgetLogId, String deptId, String userId, Integer source, Date createDate, String createUserId, String text) {
        this.budgetLogId = budgetLogId;
        this.deptId = deptId;
        this.userId = userId;
        this.source = source;
        this.createDate = createDate;
        this.createUserId = createUserId;
        this.text = text;
    }

    public String getBudgetLogId() {
        return budgetLogId;
    }

    public void setBudgetLogId(String budgetLogId) {
        this.budgetLogId = budgetLogId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BudgetLog other = (BudgetLog) that;
        return (this.getBudgetLogId() == null ? other.getBudgetLogId() == null : this.getBudgetLogId().equals(other.getBudgetLogId()))
            && (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
            && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
            && (this.getText() == null ? other.getText() == null : this.getText().equals(other.getText()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBudgetLogId() == null) ? 0 : getBudgetLogId().hashCode());
        result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", budgetLogId=").append(budgetLogId);
        sb.append(", deptId=").append(deptId);
        sb.append(", userId=").append(userId);
        sb.append(", source=").append(source);
        sb.append(", createDate=").append(createDate);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", text=").append(text);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}