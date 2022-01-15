package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * post_logic
 * @author 
 */
@ApiModel(value="top.jsls9.oajsfx.model.PostLogic结算前置逻辑表")
public class PostLogic implements Serializable {
    /**
     * 主键、id
     */
    @ApiModelProperty(value="主键、id")
    private Integer id;

    /**
     * 逻辑
     */
    @ApiModelProperty(value="逻辑")
    private String logic;

    /**
     * 逻辑描述
     */
    @ApiModelProperty(value="逻辑描述")
    private String logicDescription;

    /**
     * 提示，用于返回结果
     */
    @ApiModelProperty(value="提示，用于返回结果")
    private String prompt;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="创建时间")
    private Date createDate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="修改时间")
    private Date updateDate;

    /**
     * 状态；0-正常、1-已删除、2、已停用
     */
    @ApiModelProperty(value="状态；0-正常、1-已删除、2、已停用")
    private Integer state;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getLogicDescription() {
        return logicDescription;
    }

    public void setLogicDescription(String logicDescription) {
        this.logicDescription = logicDescription;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        PostLogic other = (PostLogic) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLogic() == null ? other.getLogic() == null : this.getLogic().equals(other.getLogic()))
            && (this.getLogicDescription() == null ? other.getLogicDescription() == null : this.getLogicDescription().equals(other.getLogicDescription()))
            && (this.getPrompt() == null ? other.getPrompt() == null : this.getPrompt().equals(other.getPrompt()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLogic() == null) ? 0 : getLogic().hashCode());
        result = prime * result + ((getLogicDescription() == null) ? 0 : getLogicDescription().hashCode());
        result = prime * result + ((getPrompt() == null) ? 0 : getPrompt().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", logic=").append(logic);
        sb.append(", logicDescription=").append(logicDescription);
        sb.append(", prompt=").append(prompt);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", state=").append(state);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}