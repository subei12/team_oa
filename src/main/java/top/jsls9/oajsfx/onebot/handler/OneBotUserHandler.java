package top.jsls9.oajsfx.onebot.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.onebot.annotation.OneBotController;
import top.jsls9.oajsfx.onebot.annotation.OneBotGroupHandler;
import top.jsls9.oajsfx.onebot.annotation.OneBotPrivateHandler;
import top.jsls9.oajsfx.onebot.model.OneBotGroupMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotMessageEvent;
import top.jsls9.oajsfx.onebot.model.OneBotPrivateMessageEvent;
import top.jsls9.oajsfx.onebot.utils.OneBotReplyBuilder;
import top.jsls9.oajsfx.service.UserService;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OneBot 用户查询处理器
 */
@OneBotController
public class OneBotUserHandler {

    private static final Logger logger = LoggerFactory.getLogger(OneBotUserHandler.class);
    private static final String QUERY_USER_KEYWORD = "查询用户";
    private static final Pattern QUERY_NUMBER_PATTERN = Pattern.compile("(\\d+)");

    @Autowired
    private UserService userService;

    /**
     * 私聊：查询用户信息
     */
    @OneBotPrivateHandler(keywords = QUERY_USER_KEYWORD)
    public JSONObject handlePrivateQueryUser(OneBotPrivateMessageEvent event) {
        return buildUserQueryReply(event, false);
    }

    /**
     * 群聊：查询用户信息（带@）
     */
    @OneBotGroupHandler(keywords = QUERY_USER_KEYWORD)
    public JSONObject handleGroupQueryUser(OneBotGroupMessageEvent event) {
        return buildUserQueryReply(event, true);
    }

    /**
     * 统一处理查询逻辑并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserQueryReply(OneBotMessageEvent event, boolean atUser) {
        // 解析用户标识（支持“查询用户 123”或“查询用户123”）
        String query = extractQueryId(extractMessageText(event));
        if (StringUtils.isBlank(query)) {
            return replyText(event, atUser, "用法: 查询用户 <社区ID|QQ>");
        }

        // 先按社区ID查询，查不到再按用户名/QQ查询
        User user = userService.queryUserByHlxUserId(query);
        if (user == null) {
            user = userService.getUserByUserName(query);
        }
        if (user == null) {
            return replyText(event, atUser, "未找到用户: " + query);
        }

        // 补全社区昵称与头衔等详情（失败时回退到基础信息）
        User detail = user;
        try {
            detail = userService.queryUserById(user.getId());
        } catch (IOException e) {
            logger.warn("Query user detail failed for id={}", user.getId(), e);
        }

        // 组装回复文本
        StringBuilder text = new StringBuilder();
        text.append("用户信息\n");
        text.append("社区ID: ").append(valueOrDash(detail.getHlxUserId())).append("\n");
        text.append("QQ: ").append(valueOrDash(detail.getQq())).append("\n");
        text.append("昵称: ").append(valueOrDash(detail.getNick())).append("\n");
        text.append("头衔: ").append(valueOrDash(detail.getTitle())).append("\n");
        text.append("积分: ").append(valueOrDash(detail.getIntegral())).append("\n");
        text.append("葫芦(OA): ").append(valueOrDash(detail.getGourd())).append("\n");
        text.append("团队: ").append(valueOrDash(detail.getDeptName()));

        return replyText(event, atUser, text.toString());
    }

    /**
     * 构建标准回复（私聊引用消息，群聊可选@）
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否@发送者
     * @param text   回复文本
     * @return OneBot 回复 JSON
     */
    private JSONObject replyText(OneBotMessageEvent event, boolean atUser, String text) {
        // 私聊使用 reply 引用，群聊避免 reply 触发额外@
        OneBotReplyBuilder builder = OneBotReplyBuilder.create();
        if (!(event instanceof OneBotGroupMessageEvent)) {
            builder.replyTo(event.getMessageId());
        }
        if (atUser) {
            builder.at(event.getUserId());
        }
        builder.text(text);
        return builder.build();
    }

    /**
     * 从 message 字段提取首个文本内容
     *
     * @param event 消息事件
     * @return 首个文本消息（去除首尾空格）
     */
    private String extractMessageText(OneBotMessageEvent event) {
        Object message = event.getMessage();
        if (message instanceof String) {
            String text = ((String) message).trim();
            return StringUtils.isNotBlank(text) ? text : null;
        }
        if (message instanceof JSONArray) {
            JSONArray nodes = (JSONArray) message;
            for (Object node : nodes) {
                if (node instanceof JSONObject) {
                    JSONObject jsonNode = (JSONObject) node;
                    if ("text".equals(jsonNode.getString("type"))) {
                        JSONObject data = jsonNode.getJSONObject("data");
                        if (data != null) {
                            String text = data.getString("text");
                            if (StringUtils.isNotBlank(text)) {
                                return text.trim();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从原始消息中提取查询参数
     *
     * @param messageText 消息文本
     * @return 用户标识（优先数字）
     */
    private String extractQueryId(String messageText) {
        // 提取查询参数，优先取数字
        if (StringUtils.isBlank(messageText)) {
            return null;
        }
        int index = messageText.indexOf(QUERY_USER_KEYWORD);
        if (index < 0) {
            return null;
        }
        String rest = messageText.substring(index + QUERY_USER_KEYWORD.length()).trim();
        if (StringUtils.isBlank(rest)) {
            return null;
        }
        Matcher matcher = QUERY_NUMBER_PATTERN.matcher(rest);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return rest;
    }

    /**
     * 空值安全输出
     *
     * @param value 任意值
     * @return 空值返回 "-"，否则返回字符串
     */
    private String valueOrDash(Object value) {
        // 空值占位，避免输出 null
        return value == null ? "-" : value.toString();
    }
}
