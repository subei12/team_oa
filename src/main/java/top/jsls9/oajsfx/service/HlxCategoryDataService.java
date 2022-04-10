package top.jsls9.oajsfx.service;

import top.jsls9.oajsfx.vo.HlxCategoryDataVo;

import java.util.Map;

/**
 * @author bSu
 * @date 2022/4/10 - 14:43
 */
public interface HlxCategoryDataService {

    HlxCategoryDataVo queryHlxCategoryDatas(String date);

    /**
     * 获取板块热度数据（简洁版）
     * @param date
     * @return
     */
    Map<String, Object> queryHlxCategoryDatasSimple(String date);

}
