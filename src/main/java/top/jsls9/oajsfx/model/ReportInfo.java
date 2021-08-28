package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * report_info
 * @author 
 */
public class ReportInfo implements Serializable {
    private Integer reportId;

    /**
     * 流程id
     */
    private String processId;

    private String createUserId;

    private Date createDate;

    private String updateUserId;

    private Date updateDate;

    /**
     * 状态，0-新增，1-已处理
     */
    private Integer state;

    /**
     * 上报内容
     */
    private byte[] reportContent;

    private static final long serialVersionUID = 1L;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public byte[] getReportContent() {
        return reportContent;
    }

    public void setReportContent(byte[] reportContent) {
        this.reportContent = reportContent;
    }
}