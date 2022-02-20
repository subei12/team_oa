package top.jsls9.oajsfx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.jsoup.Connection;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import top.jsls9.oajsfx.executor.HlxSignInXxlJob;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.hlxPojo.User;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.service.HlxService;
import top.jsls9.oajsfx.service.impl.HlxUserServiceImpl;
import top.jsls9.oajsfx.utils.HlxUtils;
import top.jsls9.oajsfx.utils.HttpUtils;
import top.jsls9.oajsfx.utils.JsonUtiles;
import top.jsls9.oajsfx.utils.QqSendMsgUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OaJsfxApplicationTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HlxUtils hlxUtils;

    @Autowired
    private HlxUserServiceImpl hlxUserService;

    @Test
    void contextLoads() throws IOException {
        String s = DigestUtils.md5DigestAsHex(("bsulike" + "su123456").getBytes());
        System.out.println(s);
    }

    @Test
    void test01(){
        String key = hlxUtils.getKey();
        System.out.println(key);
    }

    /**
     * {"code":103,"msg":"未登录","title":null,"status":0}
     * {"msg":"","iscredits":1,"status":1} 赠送成功
     * {"code":104,"msg":"贴子已锁定","title":null,"status":0}
     * @throws IOException
     */
    @Test
    void test02() throws IOException {
        //hlxUtils.sendSorce("1","47958018","测试一下。","1");
    }

    @Test
    void test03() throws IOException {
        PostsJsonRootBean postDetails = HlxUtils.getPostDetails("48190364");
        long l = new Date().getTime() - postDetails.getPost().getCreateTime() ;
        System.out.println(postDetails.getPost().getCreateTime());
        System.out.println(new Date().getTime());
        System.out.println(l);
        System.out.println(new Date().getTime() - postDetails.getPost().getCreateTime()>= 1000 * 60 * 60 * 24 *3);

    }

    @Test
    void test04() throws IOException {
        User user = hlxUtils.queryUserInfo("14057952");
        System.out.println(user);
    }

    @Autowired
    private QqSendMsgUtils qqSendMsgUtils;

    @Test
    void test05() throws IOException {
        List<MessageChain> messageChainList = new ArrayList<>();
        MessageChain messageChain = new MessageChain();
        messageChain.setType("Plain");
        String text = "版块：技术分享\n" +
                "\n" +
                "处理ID：20634518\n" +
                "\n" +
                "处理：下【民间大神】称号\n" +
                "\n" +
                "原因：无故退团，无任何原因\n" +
                " ";
        messageChain.setText(text);
        messageChainList.add(messageChain);
        qqSendMsgUtils.sendGroupMessage("937983527",messageChainList);
    }

    // admin-client
    private static String addressUrl = "https://xxl.jsls9.top/xxl-job-admin/";
    private static String accessToken = "su123456789";


    @Test
    public void callback() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken);

        HandleCallbackParam param = new HandleCallbackParam();
        param.setLogId(1);
        param.setHandleCode(XxlJobContext.HANDLE_COCE_SUCCESS);

        List<HandleCallbackParam> callbackParamList = Arrays.asList(param);

        ReturnT<String> returnT = adminBiz.callback(callbackParamList);

        assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);
    }

    @Test
    public void number(){



    }

    /**
     * 字符串转表达式
     */
    @Test
    public void TestScript() throws ScriptException {
        String str = "$title.indexOf('NB') >= 0 || $title.indexOf('HR') >= 0 || $title.indexOf('WD') >= 0 || $title.indexOf('MER') >= 0";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("$title","【WD】我的世界搭建服务器");
        engine.put("$a","123");
        Object result = engine.eval(str);

        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);

    }

    /**
     * 通过key链之间获取JSON中的值
     */
    @Test
    public void getJsonString(){
        String jsonStr = "{\"job\":{\"content\":[{\"reader\":{\"name\":{\"a\":\"b\"}}}]}}";
        String keyPath = "job.content[0].reader.name.a";
        Object json = JsonUtiles.getJsonString(jsonStr, keyPath);
        System.out.println(json);
    }

    /**
     * 测试结算前置逻辑执行是否正常
     */
    @Test
    public void postLogicTest(){
        try {
            String logic = hlxUserService.postLogic("49809440");
            logger.info("逻辑运算结果：{}",logic);
        }catch (IOException e){
            logger.error("获取帖子详情报错",e.getMessage());
        }catch (ScriptException e){
            logger.error("逻辑运算出错",e.getMessage());
        }catch (Exception e){
            logger.error("其他异常",e.getMessage());
        }


    }

    final Integer[] catIds = {1,2,3,4,6,11,15,16,21,22,23,29,34,43,44,45,56,57,58,60,63,67,68,69,70,71,76,77,81,82,84,88,90,92,94,96,98,101,102,103,105,107,108,110,111,112,113,115,116,117};
    @Test
    public void hlxSignInJobHandler() throws Exception {
        logger.info("JobHandler.hlxSignIn开始执行...");
        logger.info("XXL-JOB, HlxSignInXxlJob.hlxSignInJobHandler执行中开始。。。");
        logger.info("自动遍历ID为1-300的版块进行签到，超出300无法签到（已知未有超出的）。");
        //此任务为简单任务，只需传递字符串类型的key
        String key = "D7DB5D42B072DB665A0EAA84A7B4EE64D8D34F587DD2D3909B344C8291D7EF019B6BD1B2FB83CD0CAF95BD19B6FC48BD6179E9530567DD8B";
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
                Connection.Response result = HttpUtils.post(map,"https://floor.huluxia.com/user/signin/IOS/1.1", paramMap);
                JSONObject json= JSON.parseObject(result.body());
                String msg=json.getString("msg");
                logger.info("正在签到，版块ID："+i+"；签到结果："+ (StringUtils.isBlank(msg) ?"成功":msg));
                Thread.sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
                logger.info("签到异常，版块ID："+i+"；异常信息："+ e.getMessage());
            }
        }
        logger.info("JobHandler.hlxSignIn执行结束...");
    }

}
