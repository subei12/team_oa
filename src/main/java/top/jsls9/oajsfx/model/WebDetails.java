package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * web_details
 * @author 
 */
public class WebDetails implements Serializable {
    private String id;

    /**
     * 网站路径
     */
    private String webPath;

    /**
     * 网站名
     */
    private String webName;

    /**
     * 提交邮箱
     */
    private String email;

    /**
     * 创建黑名单的用户id,为空则表示游客添加
     */
    private String createUserId;

    /**
     * 举报时的理由
     */
    private String webRemarks;

    /**
     * 创建时间
     */
    private Date startDate;

    /**
     * 修改时间,相当于审核的时间
     */
    private Date updateDate;

    /**
     * 修改的用户名，也就是操作审核的用户名
     */
    private String updateUserId;

    /**
     * 分类id
     */
    private String webSortId;

    /**
     * 管理员审核回复
     */
    private String adminRemark;

    /**
     * 网站状态，0-审核通过，1-审核中,2-不通过，3-以删除
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getWebRemarks() {
        return webRemarks;
    }

    public void setWebRemarks(String webRemarks) {
        this.webRemarks = webRemarks;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getWebSortId() {
        return webSortId;
    }

    public void setWebSortId(String webSortId) {
        this.webSortId = webSortId;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


}