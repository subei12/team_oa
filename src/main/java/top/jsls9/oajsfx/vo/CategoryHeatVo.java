package top.jsls9.oajsfx.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author bSu
 * @date 2022/4/10 - 15:50
 */
public class CategoryHeatVo {

    @ApiModelProperty("板块id")
    private Integer id;

    @ApiModelProperty("板块名称")
    private String name;

    @ApiModelProperty("板块热度")
    private long viewCount;

    public CategoryHeatVo() {
    }

    public CategoryHeatVo(Integer id, String name, long viewCount) {
        this.id = id;
        this.name = name;
        this.viewCount = viewCount;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "CategoryHeatVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", viewCount='" + viewCount + '\'' +
                '}';
    }

}
