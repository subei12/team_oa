package top.jsls9.oajsfx.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 葫芦侠相关常量
 * @author bSu
 * @date 2025/05/11
 */
@Component
public class HlxConstant {

    /**
     * 板块ID
     */
    public static String CATEGORY_ID;

    @Value("${hlx.categoryId}")
    public void setCategoryId(String categoryId) {
        CATEGORY_ID = categoryId;
    }

}
