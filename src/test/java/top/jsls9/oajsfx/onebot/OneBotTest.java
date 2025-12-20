package top.jsls9.oajsfx.onebot;

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
import top.jsls9.oajsfx.controller.OneBotWebHookController;
import top.jsls9.oajsfx.onebot.handler.OneBotHelloHandler;
import top.jsls9.oajsfx.onebot.annotation.OneBotController;
import top.jsls9.oajsfx.onebot.annotation.OneBotGroupHandler;
import top.jsls9.oajsfx.onebot.annotation.OneBotPrivateHandler;
import top.jsls9.oajsfx.onebot.core.OneBotDispatcher;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;

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
        json.put("raw_message", "hello world");
        json.put("group_id", 123456L);
        json.put("user_id", 11111L);

        oneBotWebHookController.handleWebhook(json);

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
        json.put("raw_message", "ping me");
        json.put("user_id", 22222L);

        oneBotWebHookController.handleWebhook(json);

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
        json.put("raw_message", "hello there");
        json.put("user_id", 33333L);

        // 调用 webhook 接口
        Object result = oneBotWebHookController.handleWebhook(json);

        // 验证返回值是否为预期的 JSON 回复
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JSONObject);
        JSONObject jsonResult = (JSONObject) result;
        Assertions.assertEquals("嗨", jsonResult.getString("reply"));
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
        json.put("raw_message", "other message"); // "other" 不在关键字列表中

        oneBotWebHookController.handleWebhook(json);

        // 验证消息未被处理，最后接收消息应为 null
        Assertions.assertNull(testHandler.lastReceivedMessage);
    }

    /**
     * 测试配置类
     * <p>
     * 仅加载测试所需的 Bean，避免启动完整的 Spring Boot 上下文（含数据库连接等）。
     * </p>
     */
    @TestConfiguration
    @Import({OneBotWebHookController.class, OneBotDispatcher.class, OneBotHelloHandler.class})
    static class Config {
        @Bean
        public TestHandler testHandler() {
            return new TestHandler();
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