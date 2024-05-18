package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * web_sort
 * @author 
 */
public class Dept implements Serializable {
    private String id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 创建用户id
     */
    private String createUserId;

    /**
     * 状态；0-正常，1-不可用
     */
    private Integer state;

    /**
     * 剩余葫芦
     */
    private Integer source;

    /**
     * 结算状态，0-可以结算 1-不可结算
     */
    private Integer settlementState;

    /**
     * 各团队分别配置奖励用的key,
     * 如果没有配置默认使用系统配置的
     */
    private String deptKey;

    private static final long serialVersionUID = 1L;

    public Dept(String id, String name, Date createDate, String createUserId, Integer state,Integer source,Integer settlementState) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.createUserId = createUserId;
        this.state = state;
        this.source = source;
        this.settlementState = settlementState;
    }

    public Dept() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public Integer getState() {
        return state;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setSettlementState(Integer settlementState) {
        this.settlementState = settlementState;
    }

    public Integer getSettlementState() {
        return settlementState;
    }

    public String getDeptKey() {
        return deptKey;
    }

    public void setDeptKey(String deptKey) {
        this.deptKey = deptKey;
    }

    @Override
    public String toString() {
        return "WebSort{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", createUserId='" + createUserId + '\'' +
                ", state=" + state +
                ", settlementState=" + settlementState +
                '}';
    }
}