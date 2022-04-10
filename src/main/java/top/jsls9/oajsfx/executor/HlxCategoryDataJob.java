package top.jsls9.oajsfx.executor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.dao.HlxCategoryDataDao;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.model.HlxCategoryData;
import top.jsls9.oajsfx.utils.HttpUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author bSu
 * @date 2022/4/9 - 14:53
 */
@Component
public class HlxCategoryDataJob {

    private static Logger logger = LoggerFactory.getLogger(HlxCategoryDataJob.class);

    private static String hlxCategoryData ;

    @Resource
    private HlxCategoryDataDao hlxCategoryDataDao;

    //id为84的可以签到，但并不是板块，类似首页推荐
    //id为60的板块是"我的关注"，这个板块很奇怪，参数是60但获取到的却为0，也不用参与统计
    final Integer[] catIds = {1,2,3,4,6,11,15,16,21,22,23,29,34,43,44,45,56,57,58,63,67,68,69,70,71,76,77,81,82,88,90,92,94,96,98,101,102,103,105,107,108,110,111,112,113,115,116,117};

    @Value("${hlx.url.hlxCategoryData}")
    public void setSignInUrl(String hlxCategoryData) {
        HlxCategoryDataJob.hlxCategoryData = hlxCategoryData;
    }


    /**
     * 获取板块热度数据并存档
     * @throws Exception
     */
    @XxlJob("HlxCategoryHeatData")
    public void hlxCategoryHeatDataJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, 获取板块热度数据并存档。");

        //设置请求头
        Map<String,String> map=new HashMap<>();
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Connection","close");
        map.put("Host","floor.huluxia.com");
        map.put("Accept-Encoding","gzip");
        map.put("User-Agent","okhttp/3.8.1");
        Map<String,String > paramMap=new HashMap();
        JSONObject json= new JSONObject();
        for(Integer i : catIds){
            try {

                paramMap.put("cat_id",String.valueOf(i));
                Connection.Response result = HttpUtils.post(map, hlxCategoryData, paramMap);

                PostsJsonRootBean jsonRootBean = json.parseObject(result.body(), PostsJsonRootBean.class);
                logger.info("当前板块名称：{}，当前板块ID：{}，当前板块热度：{}，当前板块贴量：{}",
                        jsonRootBean.getCategory().getTitle(),
                        jsonRootBean.getCategory().getCategoryID(),
                        jsonRootBean.getCategory().getViewCount(),
                        jsonRootBean.getCategory().getPostCount());
                //入库
                HlxCategoryData hlxCategoryData = new HlxCategoryData();
                hlxCategoryData.setDate(new Date());
                hlxCategoryData.setCatId(jsonRootBean.getCategory().getCategoryID());
                hlxCategoryData.setCatTitle(jsonRootBean.getCategory().getTitle());
                hlxCategoryData.setType(0);
                HlxCategoryData.CatDate catDate = new HlxCategoryData.CatDate();
                catDate.setPostCount(jsonRootBean.getCategory().getPostCount());
                catDate.setViewCount(jsonRootBean.getCategory().getViewCount());
                hlxCategoryData.setCatData(JSONObject.toJSONString(catDate));
                //指定参与序列化的属性
                SimplePropertyPreFilter filter = new SimplePropertyPreFilter(HlxCategoryData.CatDate.class, "viewCount", "postCount");
                hlxCategoryData.setCatData(JSONObject.toJSONString(catDate, filter));
                hlxCategoryDataDao.insert(hlxCategoryData);
            } catch (Exception e){
                e.printStackTrace();
                XxlJobHelper.log("板块数据获取异常，版块ID："+i+"；异常信息："+ e.getMessage());
            }

        }
        //获取当日最新帖子
        paramMap.put("cat_id","2");//泳池
        paramMap.put("sort_by","1");//最新帖维度
        Connection.Response result = HttpUtils.post(map,"https://floor.huluxia.com/post/list/ANDROID/2.1", paramMap);
        PostsJsonRootBean jsonRootBean = json.parseObject(result.body(), PostsJsonRootBean.class);
        logger.info("-------------------------");
        logger.info("当前板块名称：{}，当前板块ID：{}，当前板块热度：{}，当前板块贴量：{}",
                jsonRootBean.getCategory().getTitle(),
                jsonRootBean.getCategory().getCategoryID(),
                jsonRootBean.getCategory().getViewCount(),
                jsonRootBean.getCategory().getPostCount());
        logger.info("当前帖子标题：{}，当前帖子ID：{}", jsonRootBean.getPosts().get(0).getTitle(), jsonRootBean.getPosts().get(0).getPostID());
        XxlJobHelper.log("今日最新帖ID：" + jsonRootBean.getPosts().get(0).getPostID() + "；今日最新贴标题：" + jsonRootBean.getPosts().get(0).getTitle());
        //今日最新帖入库
        HlxCategoryData hlxCategoryData = new HlxCategoryData();
        hlxCategoryData.setDate(new Date());
        hlxCategoryData.setCatId(jsonRootBean.getCategory().getCategoryID());
        hlxCategoryData.setCatTitle(jsonRootBean.getCategory().getTitle());
        hlxCategoryData.setType(1);
        HlxCategoryData.CatDate catDate = new HlxCategoryData.CatDate();
        catDate.setPostId(jsonRootBean.getPosts().get(0).getPostID());
        hlxCategoryData.setCatData(JSONObject.toJSONString(catDate));
        //指定参与序列化的属性
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(HlxCategoryData.CatDate.class, "postId");
        hlxCategoryData.setCatData(JSONObject.toJSONString(catDate, filter));
        hlxCategoryDataDao.insert(hlxCategoryData);

        XxlJobHelper.log("JobHandler.hlxCategoryHeatDataJobHandler执行结束...");


    }

}
