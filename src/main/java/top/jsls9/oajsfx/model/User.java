package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author 
 */
public class User implements Serializable {
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 盐
     */
    private String salt;

    /**
     * 用户状态；0-正常，1-不可用
     */
    private Integer state;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 葫芦侠用户id
     */
    private String hlxUserId;

    /**
     * 葫芦侠用户昵称
     */
    private String hlxUserNick;

    /**
     * 绑定qq
     */
    private String qq;

    /**
     * 部门(团队)id
     */
    private String deptId;


    /**
     * 补充字段，仅用于查询映射及显示
     */
    private String deptName;

    /**
     * 补充字段，用于显示角色名
     */
    private String roleNames;

    /**
     * 用户个人积分
     */
    private Integer integral;

    /**
     * 用户oa账户剩余葫芦
     */
    private String gourd;

    /**
     * 补充字段，用户当前佩戴称号
     */
    private String title;

    /**
     * 补充字段，用户当前的社区昵称
     */
    private String nick;


    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(String id, String username, String password, Date createTime, String salt, Integer state, Date lastTime, Date updateTime, String hlxUserId, String hlxUserNick, String qq, String deptId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.salt = salt;
        this.state = state;
        this.lastTime = lastTime;
        this.updateTime = updateTime;
        this.hlxUserId = hlxUserId;
        this.hlxUserNick = hlxUserNick;
        this.qq = qq;
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public void setHlxUserNick(String hlxUserNick) {
        this.hlxUserNick = hlxUserNick;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }

    public String getHlxUserNick() {
        return hlxUserNick;
    }

    public String getQq() {
        return qq;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getGourd() {
        return gourd;
    }

    public void setGourd(String gourd) {
        this.gourd = gourd;
    }

    public String getTitle() {
        return title;
    }

    public String getNick() {
        return nick;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", salt='" + salt + '\'' +
                ", state=" + state +
                '}';
    }
}