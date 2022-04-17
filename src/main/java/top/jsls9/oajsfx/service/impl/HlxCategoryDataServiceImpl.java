package top.jsls9.oajsfx.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;
import top.jsls9.oajsfx.dao.HlxCategoryDataDao;
import top.jsls9.oajsfx.model.HlxCategoryData;
import top.jsls9.oajsfx.service.HlxCategoryDataService;
import top.jsls9.oajsfx.utils.JsonUtiles;
import top.jsls9.oajsfx.vo.CategoryHeatVo;
import top.jsls9.oajsfx.vo.HlxCategoryDataVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bSu
 * @date 2022/4/10 - 14:59
 */
@Service
public class HlxCategoryDataServiceImpl implements HlxCategoryDataService {

    @Autowired
    private HlxCategoryDataDao hlxCategoryDataDao;

    @Override
    public HlxCategoryDataVo queryHlxCategoryDatas(String date) {
        //获取板块热度
        List<HlxCategoryData> dataList = hlxCategoryDataDao.selectByDateAndType(date,0);
        //获取最新帖子
        List<HlxCategoryData> dataList1 = hlxCategoryDataDao.selectByDateAndType(date,1);
        //封装数据
        HlxCategoryDataVo vo = new HlxCategoryDataVo();
        vo.setCategoryHeats(dataList);
        vo.setPost(dataList1 != null && dataList1.size()>=1 ? dataList1.get(0) : null);
        return vo;
    }

    @Override
    public Map<String, Object> queryHlxCategoryDatasSimple(String date) {
        Map<String, Object> map = new HashMap<>();
        //获取板块热度
        List<HlxCategoryData> dataList = hlxCategoryDataDao.selectByDateAndType(date,0);
        //获取最新帖子
        List<HlxCategoryData> dataList1 = hlxCategoryDataDao.selectByDateAndType(date,1);
        List<CategoryHeatVo> categoryHeatVoList = new ArrayList<>();
        JSONObject json= new JSONObject();
        for(HlxCategoryData hlxCategoryData : dataList){
            CategoryHeatVo vo = new CategoryHeatVo();
            vo.setId(hlxCategoryData.getCatId());
            vo.setName(hlxCategoryData.getCatTitle());
            HlxCategoryData.CatDate catDate = json.parseObject(hlxCategoryData.getCatData(), HlxCategoryData.CatDate.class);
            //String viewCount = JsonUtiles.getJsonString(hlxCategoryData.getCatData(), "viewCount");
            vo.setViewCount(catDate.getViewCount());
            //String postCount = JsonUtiles.getJsonString(hlxCategoryData.getCatData(), "postCount");
            vo.setPostCount(catDate.getPostCount());
            if(catDate.getAddViewCount() != 0){
                vo.setAddViewCount(catDate.getAddViewCount());
                vo.setAddPostCount(catDate.getAddPostCount());
            }
            categoryHeatVoList.add(vo);
        }
        map.put("categoryHeats", categoryHeatVoList);
        map.put("postId",null);
        if(dataList1 != null && dataList1.size()>=1){
            String postId = JsonUtiles.getJsonString(dataList1.get(0).getCatData(), "postId");
            map.put("postId", Long.valueOf(postId));
        }

        return map;
    }

}
