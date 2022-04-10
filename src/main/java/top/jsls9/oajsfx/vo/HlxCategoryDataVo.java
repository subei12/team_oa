package top.jsls9.oajsfx.vo;

import io.swagger.annotations.ApiModelProperty;
import top.jsls9.oajsfx.model.HlxCategoryData;

import java.util.List;

/**
 * @author bSu
 * @date 2022/4/10 - 14:52
 */
public class HlxCategoryDataVo {

    /**
     * 当日各板块数据
     */
    @ApiModelProperty("当日各板块数据")
    private List<HlxCategoryData> categoryHeats;

    /**
     * 当日最新贴（取泳池数据）
     */
    @ApiModelProperty("当日最新贴（取泳池数据）")
    private HlxCategoryData post;

    public HlxCategoryDataVo() {
    }

    public HlxCategoryDataVo(List<HlxCategoryData> categoryHeats, HlxCategoryData post) {
        this.categoryHeats = categoryHeats;
        this.post = post;
    }

    public List<HlxCategoryData> getCategoryHeats() {
        return categoryHeats;
    }

    public HlxCategoryData getPost() {
        return post;
    }

    public void setCategoryHeats(List<HlxCategoryData> categoryHeats) {
        this.categoryHeats = categoryHeats;
    }

    public void setPost(HlxCategoryData post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "HlxCategoryDataVo{" +
                "categoryHeats=" + categoryHeats +
                ", post=" + post +
                '}';
    }
}
