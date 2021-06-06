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

    private static final long serialVersionUID = 1L;

    public Dept(String id, String name, Date createDate, String createUserId, Integer state) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.createUserId = createUserId;
        this.state = state;
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

    @Override
    public String toString() {
        return "WebSort{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", createUserId='" + createUserId + '\'' +
                ", state=" + state +
                '}';
    }
}