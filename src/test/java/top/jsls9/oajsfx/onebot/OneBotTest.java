package top.jsls9.oajsfx.onebot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import top.jsls9.oajsfx.controller.OneBotWebHookController;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.onebot.annotation.OneBotController;
import top.jsls9.oajsfx.onebot.annotation.OneBotGroupHandler;
import top.jsls9.oajsfx.onebot.annotation.OneBotPrivateHandler;
import top.jsls9.oajsfx.onebot.core.OneBotDispatcher;
import top.jsls9.oajsfx.onebot.handler.OneBotHelloHandler;
import top.jsls9.oajsfx.onebot.handler.OneBotUserHandler;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;
import top.jsls9.oajsfx.service.UserService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * OneBot 消息分发测试类
 * <p>
 * 用于测试 {@link OneBotDispatcher} 和 {@link OneBotWebHookController} 的功能，
 * 验证消息是否能根据注解配置正确分发到对应的处理方法。
 * </p>
 *
 * @author bSu
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OneBotTest.Config.class})
public class OneBotTest {

    @Autowired
    private OneBotWebHookController oneBotWebHookController;

    @Autowired
    private TestHandler testHandler;

    @Autowired
    private UserService userService;

    /**
     * 测试群消息分发
     * <p>
     * 验证带有关键字 "hello" 的群消息能否被 {@link TestHandler#handleGroupHello} 正确接收。
     * </p>
     */
    @Test
    public void testGroupMessageDispatch() {
        // 重置状态
        testHandler.lastReceivedMessage = null;

        // 模拟一条群消息 "hello world"
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "group");
        json.put("message", buildTextMessage("hello world"));
        json.put("raw_message", "hello world");
        json.put("group_id", 123456L);
        json.put("user_id", 11111L);

        // 调用 webhook (无需签名)
        oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证接收到的消息和群组ID是否正确
        Assertions.assertEquals("hello world", testHandler.lastReceivedMessage);
        Assertions.assertEquals(123456L, testHandler.lastGroupId);
    }

    /**
     * 测试私聊消息分发
     * <p>
     * 验证带有关键字 "ping" 的私聊消息能否被 {@link TestHandler#handlePrivatePing} 正确接收。
     * </p>
     */
    @Test
    public void testPrivateMessageDispatch() {
        // 重置状态
        testHandler.lastReceivedMessage = null;

        // 模拟一条私聊消息 "ping me"
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "private");
        json.put("message", buildTextMessage("ping me"));
        json.put("raw_message", "ping me");
        json.put("user_id", 22222L);

        // 调用 webhook (无需签名)
        oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证接收到的消息内容是否正确
        Assertions.assertEquals("ping me", testHandler.lastReceivedMessage);
    }
    
    /**
     * 测试 OneBotHelloHandler 的自动回复功能
     */
    @Test
    public void testHelloAutoReply() {
        // 模拟一条私聊消息 "hello there"
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "private");
        json.put("message", buildTextMessage("hello there"));
        json.put("raw_message", "hello there");
        json.put("user_id", 33333L);

        // 调用 webhook 接口
        Object result = oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证返回值是否为预期的 JSON 回复
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JSONObject);
        JSONObject jsonResult = (JSONObject) result;
        Object reply = jsonResult.get("reply");
        Assertions.assertNotNull(reply);
        Assertions.assertTrue(reply.toString().contains("嗨"));
    }

    /**
     * 测试查询用户的私聊回复
     */
    @Test
    public void testUserQueryPrivateReply() throws Exception {
        // 重置 mock
        reset(userService);

        // 准备用户数据
        User baseUser = new User();
        baseUser.setId("u1");
        baseUser.setHlxUserId("2622798046");
        baseUser.setQq("888");
        baseUser.setDeptId("d1");

        User detailUser = new User();
        detailUser.setId("u1");
        detailUser.setHlxUserId("2622798046");
        detailUser.setQq("888");
        detailUser.setDeptId("d1");
        detailUser.setNick("测试昵称");
        detailUser.setTitle("达人");
        detailUser.setIntegral(10);
        detailUser.setGourd(20);

        when(userService.queryUserByHlxUserId("2622798046")).thenReturn(baseUser);
        when(userService.queryUserById("u1")).thenReturn(detailUser);

        // 模拟私聊查询
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "private");
        json.put("message", buildTextMessage("查询用户 2622798046"));
        json.put("raw_message", "查询用户 2622798046");
        json.put("user_id", 55555L);
        json.put("message_id", 101);

        Object result = oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证回复内容包含关键字段
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JSONObject);
        JSONObject jsonResult = (JSONObject) result;
        Object reply = jsonResult.get("reply");
        Assertions.assertNotNull(reply);
        String replyText = reply.toString();
        Assertions.assertTrue(replyText.contains("社区ID: 2622798046"));
        Assertions.assertTrue(replyText.contains("QQ: 888"));
        Assertions.assertTrue(replyText.contains("昵称: 测试昵称"));
    }

    /**
     * 测试查询用户的群聊回复（包含@）
     */
    @Test
    public void testUserQueryGroupReply() throws Exception {
        // 重置 mock
        reset(userService);

        // 准备用户数据
        User baseUser = new User();
        baseUser.setId("u2");
        baseUser.setHlxUserId("2622798046");
        baseUser.setQq("999");
        baseUser.setDeptId("d2");

        when(userService.queryUserByHlxUserId("2622798046")).thenReturn(baseUser);
        when(userService.queryUserById("u2")).thenReturn(baseUser);

        // 模拟群聊查询
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "group");
        json.put("message", buildAtTextMessage(2969001254L, "查询用户 2622798046"));
        json.put("raw_message", "查询用户 2622798046");
        json.put("user_id", 66666L);
        json.put("message_id", 202);
        json.put("group_id", 123456L);

        Object result = oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证回复包含用户信息（群聊@机器人时不再重复@发送者）
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JSONObject);
        JSONObject jsonResult = (JSONObject) result;
        Object reply = jsonResult.get("reply");
        Assertions.assertNotNull(reply);
        String replyText = reply.toString();
        Assertions.assertTrue(replyText.contains("社区ID: 2622798046"));

        // 群聊不@机器人时也能触发
        JSONObject jsonNoAt = new JSONObject();
        jsonNoAt.put("post_type", "message");
        jsonNoAt.put("message_type", "group");
        jsonNoAt.put("message", buildTextMessage("查询用户 2622798046"));
        jsonNoAt.put("raw_message", "查询用户 2622798046");
        jsonNoAt.put("user_id", 66666L);
        jsonNoAt.put("message_id", 203);
        jsonNoAt.put("group_id", 123456L);

        Object resultNoAt = oneBotWebHookController.handleWebhook(jsonNoAt.toJSONString(), null, null);
        Assertions.assertNotNull(resultNoAt);
        Assertions.assertTrue(resultNoAt instanceof JSONObject);
    }
    
    /**
     * 测试关键字不匹配的情况
     * <p>
     * 验证不包含指定关键字的消息是否会被忽略（不触发处理方法）。
     * </p>
     */
    @Test
    public void testKeywordMiss() {
        // 重置状态
        testHandler.lastReceivedMessage = null;

        // 模拟一条群消息，但内容不以 "hello" 开头，不应匹配到 handleGroupHello
        JSONObject json = new JSONObject();
        json.put("post_type", "message");
        json.put("message_type", "group");
        json.put("message", buildTextMessage("other message"));
        json.put("raw_message", "other message");

        oneBotWebHookController.handleWebhook(json.toJSONString(), null, null);

        // 验证消息未被处理，最后接收消息应为 null
        Assertions.assertNull(testHandler.lastReceivedMessage);
    }

    /**
     * 测试 HMAC SHA1 签名验证逻辑
     * <p>
     * 分别测试：
     * 1. 签名正确 -> 验证通过
     * 2. 签名错误 -> 403 Forbidden
     * 3. 签名缺失 -> 401 Unauthorized
     * </p>
     */
    @Test
    public void testSignatureVerification() throws Exception {
        String secret = "test-secret";
        // 使用 ReflectionTestUtils 临时注入 secret，模拟配置了密钥的情况
        ReflectionTestUtils.setField(oneBotWebHookController, "secret", secret);

        try {
            // 构造测试消息体
            JSONObject json = new JSONObject();
            json.put("post_type", "meta_event");
            json.put("meta_event_type", "heartbeat");
            String body = json.toJSONString();

            // 计算正确的签名: sha1=<hex(hmac)>
            String validSignature = "sha1=" + hmacSha1(secret, body);

            // 模拟 HttpServletResponse 对象
            HttpServletResponse response = mock(HttpServletResponse.class);

            // 1. 测试签名正确的情况
            oneBotWebHookController.handleWebhook(body, validSignature, response);
            // 验证没有设置任何错误状态码 (即验证通过)
            verify(response, never()).setStatus(anyInt());

            // 2. 测试签名错误的情况
            reset(response);
            oneBotWebHookController.handleWebhook(body, "sha1=invalid", response);
            // 验证设置了 403 Forbidden
            verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);

            // 3. 测试缺少签名的情况
            reset(response);
            oneBotWebHookController.handleWebhook(body, null, response);
            // 验证设置了 401 Unauthorized
            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } finally {
            // 测试结束后恢复 secret 为空，以免影响其他测试方法（因为 Controller 是单例）
            ReflectionTestUtils.setField(oneBotWebHookController, "secret", "");
        }
    }

    /**
     * 计算 HMAC SHA1 签名的辅助方法
     */
    private String hmacSha1(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hmacBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 构建仅包含文本的 message 数组
     *
     * @param text 文本内容
     * @return message 数组
     */
    private JSONArray buildTextMessage(String text) {
        JSONArray message = new JSONArray();
        JSONObject textNode = new JSONObject();
        textNode.put("type", "text");
        JSONObject textData = new JSONObject();
        textData.put("text", text);
        textNode.put("data", textData);
        message.add(textNode);
        return message;
    }

    /**
     * 构建包含@与文本的 message 数组
     *
     * @param atQq 被@的 QQ
     * @param text 文本内容
     * @return message 数组
     */
    private JSONArray buildAtTextMessage(Long atQq, String text) {
        JSONArray message = new JSONArray();
        JSONObject atNode = new JSONObject();
        atNode.put("type", "at");
        JSONObject atData = new JSONObject();
        atData.put("qq", String.valueOf(atQq));
        atNode.put("data", atData);
        message.add(atNode);

        JSONObject textNode = new JSONObject();
        textNode.put("type", "text");
        JSONObject textData = new JSONObject();
        textData.put("text", text);
        textNode.put("data", textData);
        message.add(textNode);
        return message;
    }

    /**
     * 测试配置类
     * <p>
     * 仅加载测试所需的 Bean，避免启动完整的 Spring Boot 上下文（含数据库连接等）。
     * </p>
     */
    @TestConfiguration
    @Import({OneBotWebHookController.class, OneBotDispatcher.class, OneBotHelloHandler.class, OneBotUserHandler.class})
    static class Config {
        /**
         * 注入测试处理器
         */
        @Bean
        public TestHandler testHandler() {
            return new TestHandler();
        }

        /**
         * 注入 UserService 的 mock 实例
         */
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    /**
     * 测试用的处理器类
     */
    @OneBotController
    public static class TestHandler {
        public String lastReceivedMessage;
        public Long lastGroupId;

        /**
         * 处理以 "hello" 开头的群消息
         */
        @OneBotGroupHandler(keywords = {"hello"})
        public void handleGroupHello(OneBotGroupMessageEvent event) {
            this.lastReceivedMessage = event.getRawMessage();
            this.lastGroupId = event.getGroupId();
            System.out.println("TestHandler 收到群消息: " + event.getRawMessage());
        }

        /**
         * 处理以 "ping" 开头的私聊消息
         */
        @OneBotPrivateHandler(keywords = {"ping"})
        public void handlePrivatePing(OneBotPrivateMessageEvent event) {
            this.lastReceivedMessage = event.getRawMessage();
            System.out.println("TestHandler 收到私聊消息: " + event.getRawMessage());
        }
    }
}

