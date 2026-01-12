package top.jsls9.oajsfx.onebot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.Permission;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.onebot.model.OneBotMessageEvent;
import top.jsls9.oajsfx.service.PermissionService;
import top.jsls9.oajsfx.service.UserService;

import java.util.List;

/**
 * OneBot 指令权限校验
 */
@Component
public class OneBotPermissionChecker {

    private static final Logger logger = LoggerFactory.getLogger(OneBotPermissionChecker.class);
    private static final int COMMAND_PERMISSION_TYPE = 3;

    @Value("${onebot.global-prefix:}")
    private String globalPrefix;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    /**
     * 校验指令权限
     *
     * @param event   OneBot 消息事件
     * @param command 指令关键字
     * @return 无权限时返回提示文本，否则返回 null
     */
    public String checkCommandPermission(OneBotMessageEvent event, String command) {
        String normalized = normalizeCommand(command);
        if (StringUtils.isBlank(normalized)) {
            return null;
        }
        Permission configured = findCommandPermission(normalized);
        if (configured == null) {
            return null;
        }
        User operator = resolveOperatorUser(event);
        if (operator == null) {
            return "未绑定OA账号，无法执行该指令";
        }
        if (operator.getState() != null && operator.getState() != 0) {
            return "账号不可用，无法执行该指令";
        }
        if (!hasCommandPermission(operator, normalized)) {
            return "无权限执行该指令";
        }
        return null;
    }

    /**
     * 根据关键词解析并校验指令权限
     *
     * @param event    OneBot 消息事件
     * @param keywords 指令关键字列表
     * @return 无权限时返回提示文本，否则返回 null
     */
    public String checkCommandPermissionByKeywords(OneBotMessageEvent event, String... keywords) {
        String command = resolveCommand(event, keywords);
        if (StringUtils.isBlank(command)) {
            return null;
        }
        return checkCommandPermission(event, command);
    }

    /**
     * 根据发送者 QQ 获取 OA 用户（包含 qq 兜底）
     *
     * @param event OneBot 消息事件
     * @return OA 用户
     */
    private User resolveOperatorUser(OneBotMessageEvent event) {
        if (event == null || event.getUserId() == null) {
            return null;
        }
        String qq = String.valueOf(event.getUserId());
        User user = userService.getUserByUserName(qq);
        if (user != null) {
            return user;
        }
        User probe = new User();
        probe.setQq(qq);
        try {
            List<User> users = userDao.queryUsers(probe);
            if (users != null && !users.isEmpty()) {
                return users.get(0);
            }
        } catch (Exception e) {
            logger.warn("Query user by qq failed: {}", qq, e);
        }
        return null;
    }

    /**
     * 判断用户是否拥有指令权限
     *
     * @param user    OA 用户
     * @param command 指令关键字
     * @return 是否允许
     */
    private boolean hasCommandPermission(User user, String command) {
        if (user == null) {
            return false;
        }
        List<Permission> permissions = permissionService.selectAllByUserId(user.getId());
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        for (Permission permission : permissions) {
            if (isCommandPermission(permission, command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找是否配置了该指令权限
     *
     * @param command 指令关键字
     * @return 权限配置
     */
    private Permission findCommandPermission(String command) {
        List<Permission> permissions = permissionService.selectAll();
        if (permissions == null || permissions.isEmpty()) {
            return null;
        }
        for (Permission permission : permissions) {
            if (isCommandPermission(permission, command)) {
                return permission;
            }
        }
        return null;
    }

    /**
     * 判定权限记录是否为指令且匹配关键词
     *
     * @param permission 权限配置
     * @param command    指令关键字
     * @return 是否匹配
     */
    private boolean isCommandPermission(Permission permission, String command) {
        if (permission == null || permission.getType() == null) {
            return false;
        }
        if (permission.getType() != COMMAND_PERMISSION_TYPE) {
            return false;
        }
        String url = normalizeCommand(permission.getUrl());
        if (StringUtils.isBlank(url)) {
            return false;
        }
        return url.equalsIgnoreCase(normalizeCommand(command));
    }

    /**
     * 从消息文本中解析命中的指令关键字
     *
     * @param event    OneBot 消息事件
     * @param keywords 指令关键字列表
     * @return 命中的指令
     */
    private String resolveCommand(OneBotMessageEvent event, String... keywords) {
        if (event == null || keywords == null || keywords.length == 0) {
            return null;
        }
        String messageText = extractMessageText(event);
        if (StringUtils.isBlank(messageText)) {
            return null;
        }
        String matchText = messageText.trim();
        if (StringUtils.isNotBlank(globalPrefix) && matchText.startsWith(globalPrefix)) {
            matchText = matchText.substring(globalPrefix.length()).trim();
        }
        matchText = stripLeadingAt(matchText);
        for (String keyword : keywords) {
            if (StringUtils.isNotBlank(keyword) && matchText.startsWith(keyword)) {
                return keyword;
            }
        }
        return null;
    }

    /**
     * 提取消息首个文本内容
     *
     * @param event 消息事件
     * @return 文本内容
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
     * 去除前置 @ 的 CQ 码
     *
     * @param message 原始消息
     * @return 去除后的文本
     */
    private String stripLeadingAt(String message) {
        if (StringUtils.isBlank(message)) {
            return message;
        }
        String result = message;
        while (result.startsWith("[CQ:at,")) {
            int end = result.indexOf("]");
            if (end < 0) {
                break;
            }
            result = result.substring(end + 1).trim();
        }
        return result;
    }

    /**
     * 规范化指令文本
     *
     * @param command 指令
     * @return 规范化结果
     */
    private String normalizeCommand(String command) {
        if (command == null) {
            return null;
        }
        String trimmed = command.trim();
        return StringUtils.isNotBlank(trimmed) ? trimmed : null;
    }
}
