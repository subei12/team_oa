package top.jsls9.oajsfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_source_log
 * @author 
 */
public class SysSourceLog implements Serializable {
    private Integer id;

    /**
     * 葫芦接收人社区id
     */
    private String hlxUserId;

    /**
     * 奖励数量
     */
    private Integer source;

    /**
     * 来源类型；1-帖子直接结算、2-oa账户提现、3-团队自定义奖励、4-团队成员自荐优质内容奖励
     */
    private Integer type;

    /**
     * 操作人id(oa系统用户id)，没有就留空。划掉不要这个字段了
     */
    private String createUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHlxUserId() {
        return hlxUserId;
    }

    public void setHlxUserId(String hlxUserId) {
        this.hlxUserId = hlxUserId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}