package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * permission
 * @author 
 */
public class Permission implements Serializable {
    private Integer id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限访问路径
     */
    private String url;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * ⽗级权限id
     */
    private Integer parentId;

    /**
     * 类型   0：⽬录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 排序，从小到大
     * 这个嘎嘎重要，会影响权限到底能不能正确配置
     */
    private Integer orderNum;

    /**
     * 图标
     */
    private String icon;

    /**
     * 状态：0有效；1删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 子权限
     */
    List<Permission> children = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    public Permission() {
    }

    public Permission(Integer id, String name, String description, String url, String perms, Integer parentId, Integer type, Integer orderNum, String icon, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.perms = perms;
        this.parentId = parentId;
        this.type = type;
        this.orderNum = orderNum;
        this.icon = icon;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }
}