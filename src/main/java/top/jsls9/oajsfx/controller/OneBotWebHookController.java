package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.onebot.core.OneBotDispatcher;
import top.jsls9.oajsfx.onebot.model.OneBotEvent;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * OneBot WebHook 控制器
 * <p>
 * 接收来自 OneBot 客户端的 HTTP POST 事件上报。
 * </p>
 *
 * @author bSu
 */
@RestController
public class OneBotWebHookController {

    private static final Logger logger = LoggerFactory.getLogger(OneBotWebHookController.class);

    @Autowired
    private OneBotDispatcher dispatcher;

    @Value("${onebot.secret:}")
    private String secret;

    /**
     * 处理 OneBot WebHook 请求
     *
     * @param body      原始 HTTP 请求体
     * @param signature X-Signature 签名头
     * @param response  HTTP 响应对象，用于设置状态码
     * @return 处理结果
     */
    @PostMapping("/onebot/webhook")
    public Object handleWebhook(@RequestBody String body,
                                @RequestHeader(value = "X-Signature", required = false) String signature,
                                HttpServletResponse response) {
        
        // 1. 签名验证 (如果配置了 secret)
        if (secret != null && !secret.isEmpty()) {
            if (signature == null || signature.isEmpty()) {
                logger.warn("OneBot WebHook missing signature");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            if (!verifySignature(body, signature)) {
                logger.warn("OneBot WebHook signature verification failed");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return null;
            }
        }

        // 2. 解析 JSON 消息体
        JSONObject json;
        try {
            json = JSONObject.parseObject(body);
        } catch (Exception e) {
            logger.error("Failed to parse OneBot event JSON", e);
            return null;
        }

        logger.info("Received OneBot event: {}", json.toJSONString());

        // 3. 根据 message_type 映射到对应的 Java 对象
        String postType = json.getString("post_type");
        OneBotEvent event = null;

        if ("message".equals(postType)) {
            String messageType = json.getString("message_type");
            if ("group".equals(messageType)) {
                event = json.toJavaObject(OneBotGroupMessageEvent.class);
            } else if ("private".equals(messageType)) {
                event = json.toJavaObject(OneBotPrivateMessageEvent.class);
            } else {
                event = json.toJavaObject(OneBotMessageEvent.class);
            }
        } else {
            // 对于其他类型（notice, request, meta_event），暂时使用基类
            event = json.toJavaObject(OneBotEvent.class);
        }

        // 4. 分发事件
        if (event != null) {
            return dispatcher.dispatch(event);
        }

        return null;
    }

    /**
     * 验证 HMAC SHA1 签名
     *
     * @param body      请求体内容
     * @param signature 请求头中的签名
     * @return 验证通过返回 true
     */
    private boolean verifySignature(String body, String signature) {
        try {
            // 使用 HMAC SHA1 算法
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            mac.init(secretKeySpec);
            
            // 计算 body 的 HMAC 值
            byte[] hmacBytes = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
            
            // 拼接 "sha1=" 前缀并进行比较
            String calculatedSignature = "sha1=" + bytesToHex(hmacBytes);
            return calculatedSignature.equals(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error verifying OneBot signature", e);
            return false;
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}