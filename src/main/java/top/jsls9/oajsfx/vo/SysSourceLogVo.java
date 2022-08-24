package top.jsls9.oajsfx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bSu
 * @date 2022/8/23 - 20:57
 */
@ApiModel(value="商品表查询参数", description="商品表查询参数")
public class SysSourceLogVo implements Serializable {

    private Integer id;

    /**
     * 葫芦接收人社区id
     */
    @ApiModelProperty(name = "hlxUserId", value = "葫芦接收人社区id")
    private String hlxUserId;

    /**
     * 奖励数量
     */
    @ApiModelProperty(name = "source", value = "奖励数量")
    private Integer source;

    /**
     * 来源类型；1-帖子直接结算、2-oa账户提现、3-团队自定义奖励、4-团队成员自荐优质内容奖励
     */
    @ApiModelProperty(name = "type", value = "来源类型；1-帖子直接结算、2-oa账户提现、3-团队自定义奖励、4-团队成员自荐优质内容奖励")
    private Integer type;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "startDate", value = "开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "endDate", value = "结束时间")
    private Date endDate;


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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
