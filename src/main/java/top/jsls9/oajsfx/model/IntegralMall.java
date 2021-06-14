package top.jsls9.oajsfx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * integral_mall
 * @author 
 */
public class IntegralMall implements Serializable {
    /**
     * 主键
     */
    private String integralMallId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImages;

    /**
     * 商品价格
     */
    private Integer goodsPrice;

    /**
     * 剩余数量
     */
    private Integer goodsCount;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建用户，其实就我一个超管可以创建
     */
    private String createUser;

    /**
     * 状态，0-正常 1-下架
     */
    private Integer state;

    /**
     * 版本号，用于实现乐观锁
     */
    private Integer version;

    /**
     * 商品描述
     */
    private String goodsDescription;

    /**
     * 限制每个用户每月兑换的数量，为0则是不限制
     */
    private Integer userCount;

    private static final long serialVersionUID = 1L;

    public String getIntegralMallId() {
        return integralMallId;
    }

    public void setIntegralMallId(String integralMallId) {
        this.integralMallId = integralMallId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImages() {
        return goodsImages;
    }

    public void setGoodsImages(String goodsImages) {
        this.goodsImages = goodsImages;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}