package top.jsls9.oajsfx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

}
