package top.jsls9.oajsfx.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**葫芦侠自动签到job
 * @author bSu
 * @date 2021/7/3 - 16:59
 */
@Component
public class HlxSignInXxlJob {

    private static Logger logger = LoggerFactory.getLogger(HlxSignInXxlJob.class);


    private static String signInUrl ;

    final Integer[] catIds = {1,2,3,4,6,11,15,16,21,22,23,29,34,43,44,45,56,57,58,60,63,67,68,69,70,71,76,77,81,82,84,88,90,92,94,96,98,101,102,103,105,107,108,110,111,112,113,115,116,117};

    @Value("${hlx.url.signIn}")
    public void setSignInUrl(String signInUrl) {
        HlxSignInXxlJob.signInUrl = signInUrl;
    }

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("hlxSignIn")
    public void hlxSignInJobHandler() throws Exception {
        logger.info("JobHandler.hlxSignIn开始执行...");
        XxlJobHelper.log("XXL-JOB, HlxSignInXxlJob.hlxSignInJobHandler执行中开始。。。");
        XxlJobHelper.log("仅签到2022.02.20前已有的板块。");
        //此任务为简单任务，只需传递字符串类型的key
        String key = XxlJobHelper.getJobParam();
        if(StringUtils.isBlank(key)){
            XxlJobHelper.log("key为空，签到失败,请确认参数。");
            XxlJobHelper.handleFail("key为空，签到失败");
            return;
        }
        //设置请求头
        Map<String,String> map=new HashMap<>();
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Connection","close");
        map.put("Host","floor.huluxia.com");
        map.put("Accept-Encoding","gzip");
        map.put("User-Agent","okhttp/3.8.1");
        for(Integer i : catIds){
            try {

                Map<String,String > paramMap=new HashMap();
                paramMap.put("_key",key);
                paramMap.put("cat_id",String.valueOf(i));
                Connection.Response result = HttpUtils.post(map,signInUrl, paramMap);
                JSONObject json= JSON.parseObject(result.body());
                String msg=json.getString("msg");
                XxlJobHelper.log("正在签到，版块ID："+i+"；签到结果："+ (StringUtils.isBlank(msg) ?"成功":msg));
                Thread.sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
                XxlJobHelper.log("签到异常，版块ID："+i+"；异常信息："+ e.getMessage());
            }
        }
        logger.info("JobHandler.hlxSignIn执行结束...");
    }

}
