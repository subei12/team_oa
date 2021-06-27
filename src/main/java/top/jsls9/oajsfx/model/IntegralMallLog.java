package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * integral_mall_log
 * @author 
 */
public class IntegralMallLog implements Serializable {
    /**
     * 主键
     */
    private String integralMallLogId;

    /**
     * 积分商店表id
     */
    private String integralMallId;

    /**
     * 用户Id；之前为了省事都用的是用户名，亏了呀，埋了一个深坑，以后不好整了呀。
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 单次兑换的个数，目前每个商城单次只能兑换一个
     */
    private Integer count;

    /**
     * 修改时间，也为管理员处理时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 处理兑换的超级管理员id
     */
    private String updateUserId;

    /**
     * 状态，0-待处理 1-处理完成
     */
    private Integer state;

    /**
     * 处理结果，如放兑换码或者回复说已上报
     */
    private String result;

    /**
     * 补充字段，用于显示商品名
     */
    private String goodName;

    private static final long serialVersionUID = 1L;

    public String getIntegralMallLogId() {
        return integralMallLogId;
    }

    public void setIntegralMallLogId(String integralMallLogId) {
        this.integralMallLogId = integralMallLogId;
    }

    public String getIntegralMallId() {
        return integralMallId;
    }

    public void setIntegralMallId(String integralMallId) {
        this.integralMallId = integralMallId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodName() {
        return goodName;
    }
}