package top.jsls9.oajsfx.model;

import java.io.Serializable;

/**
 * report_config
 * @author 
 */
public class ReportConfig implements Serializable {
    private String id;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 表单名称
     */
    private String titleName;

    /**
     * 对应表单实体
     */
    private String entityClass;

    /**
     * 状态：0-启用，1-停用
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}