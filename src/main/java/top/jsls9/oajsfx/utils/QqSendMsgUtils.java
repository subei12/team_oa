package top.jsls9.oajsfx.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.hlxPojo.qqPojo.SendMsg;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**QQ发信工具类，使用的是mirai-http-api
 * @author bSu
 * @date 2021/8/28 - 16:29
 */
@Component
public class QqSendMsgUtils {

    /**
     * 监听url
     */
    @Value("${qq.url}")
    private String url;

    /**
     * 认证key
     */
    @Value("${qq.verifyKey}")
    private String verifyKey;

    /**
     * 发信使用的qq
     */
    @Value("${qq.number}")
    private String qq;

    /**
     * 会话，每个线程一个，用完就释放
     * mirai默认session有效期是30分钟，只用来做发信太浪费了，干脆用完即焚
     */
    private static ThreadLocal<String> session = new ThreadLocal<>();

    Gson gson = new Gson();

    /**
     * 认证
     * @return
     */
    public void verify() throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("verifyKey",verifyKey);
        Connection.Response post = HttpUtils.post(url + "/verify",gson.toJson(map));
        session.set((String) JSON.parseObject(post.body()).get("session"));
    }
    /*Connection.Response post = HttpUtils.post(url + "/verify", map);
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(gson.toJson(map),mediaType);
    Request request = new Request.Builder()
            .url(url + "/verify")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();
    Response response = client.newCall(request).execute();
        return gson.toJson(response.body());*/

    /**
     * 绑定
     */
    private void bind() throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("sessionKey",session.get());
        map.put("qq",qq);
        Connection.Response post = HttpUtils.post(url + "/bind",gson.toJson(map));
    }

    /**
     * 释放
     */
    private void release() throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("sessionKey",session.get());
        map.put("qq",qq);
        Connection.Response post = HttpUtils.post(url + "/release",gson.toJson(map));
        session.remove();
    }

    /**
     * 准备
     * 认证+绑定
     */
    private void toPrepare(){
        try {
            verify();
            bind();
        }catch (Exception e){
            if(StringUtils.isNotBlank(session.get())){
                session.remove();
            }
            e.printStackTrace();
        }
    }

    /**
     * 发送群消息
     */
    public void sendGroupMessage(String group, List<MessageChain> messageChains) throws IOException {
        toPrepare();
        SendMsg sendMsg = new SendMsg();
        sendMsg.setSessionKey(session.get());
        sendMsg.setTarget(group);
        sendMsg.setMessageChain(messageChains);
        Connection.Response post = HttpUtils.post(url + "/sendGroupMessage",gson.toJson(sendMsg));
        //释放资源们
        session.remove();
        release();
    }


}
