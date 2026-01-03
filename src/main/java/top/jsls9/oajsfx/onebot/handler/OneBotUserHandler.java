package top.jsls9.oajsfx.onebot.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.jsls9.oajsfx.dao.DeptDao;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.Dept;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OneBot 用户管理处理器
 */
@OneBotController
public class OneBotUserHandler {

    private static final Logger logger = LoggerFactory.getLogger(OneBotUserHandler.class);
    private static final String QUERY_USER_KEYWORD = "查询用户";
    private static final String LIST_USER_KEYWORD = "用户列表";
    private static final String ADD_USER_KEYWORD = "添加用户";
    private static final String UPDATE_USER_KEYWORD = "修改用户";
    private static final String DELETE_USER_KEYWORD = "删除用户";
    private static final String LIST_DEPT_KEYWORD = "团队列表";
    private static final String LIST_DEPT_ALIAS = "查询团队列表";
    private static final String MENU_KEYWORD = "菜单";
    private static final String HELP_KEYWORD = "帮助";
    private static final Pattern QUERY_NUMBER_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F-]{32,36}$");

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DeptDao deptDao;

    /**
     * 私聊：机器人菜单
     */
    @OneBotPrivateHandler(keywords = {MENU_KEYWORD, HELP_KEYWORD})
    public JSONObject handlePrivateMenu(OneBotPrivateMessageEvent event) {
        return buildMenuReply(event, false);
    }

    /**
     * 群聊：机器人菜单（带@）
     */
    @OneBotGroupHandler(keywords = {MENU_KEYWORD, HELP_KEYWORD})
    public JSONObject handleGroupMenu(OneBotGroupMessageEvent event) {
        return buildMenuReply(event, true);
    }

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
     * 私聊：查询用户列表
     */
    @OneBotPrivateHandler(keywords = LIST_USER_KEYWORD)
    public JSONObject handlePrivateListUsers(OneBotPrivateMessageEvent event) {
        return buildUserListReply(event, false);
    }

    /**
     * 群聊：查询用户列表（带@）
     */
    @OneBotGroupHandler(keywords = LIST_USER_KEYWORD)
    public JSONObject handleGroupListUsers(OneBotGroupMessageEvent event) {
        return buildUserListReply(event, true);
    }

    /**
     * 私聊：添加用户
     */
    @OneBotPrivateHandler(keywords = ADD_USER_KEYWORD)
    public JSONObject handlePrivateAddUser(OneBotPrivateMessageEvent event) {
        return buildUserAddReply(event, false);
    }

    /**
     * 群聊：添加用户（带@）
     */
    @OneBotGroupHandler(keywords = ADD_USER_KEYWORD)
    public JSONObject handleGroupAddUser(OneBotGroupMessageEvent event) {
        return buildUserAddReply(event, true);
    }

    /**
     * 私聊：修改用户
     */
    @OneBotPrivateHandler(keywords = UPDATE_USER_KEYWORD)
    public JSONObject handlePrivateUpdateUser(OneBotPrivateMessageEvent event) {
        return buildUserUpdateReply(event, false);
    }

    /**
     * 群聊：修改用户（带@）
     */
    @OneBotGroupHandler(keywords = UPDATE_USER_KEYWORD)
    public JSONObject handleGroupUpdateUser(OneBotGroupMessageEvent event) {
        return buildUserUpdateReply(event, true);
    }

    /**
     * 私聊：删除用户
     */
    @OneBotPrivateHandler(keywords = DELETE_USER_KEYWORD)
    public JSONObject handlePrivateDeleteUser(OneBotPrivateMessageEvent event) {
        return buildUserDeleteReply(event, false);
    }

    /**
     * 群聊：删除用户（带@）
     */
    @OneBotGroupHandler(keywords = DELETE_USER_KEYWORD)
    public JSONObject handleGroupDeleteUser(OneBotGroupMessageEvent event) {
        return buildUserDeleteReply(event, true);
    }

    /**
     * 私聊：查询团队列表
     */
    @OneBotPrivateHandler(keywords = {LIST_DEPT_KEYWORD, LIST_DEPT_ALIAS})
    public JSONObject handlePrivateListDepts(OneBotPrivateMessageEvent event) {
        return buildDeptListReply(event, false);
    }

    /**
     * 群聊：查询团队列表（带@）
     */
    @OneBotGroupHandler(keywords = {LIST_DEPT_KEYWORD, LIST_DEPT_ALIAS})
    public JSONObject handleGroupListDepts(OneBotGroupMessageEvent event) {
        return buildDeptListReply(event, true);
    }

    /**
     * 统一处理查询逻辑并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserQueryReply(OneBotMessageEvent event, boolean atUser) {
        String messageText = extractMessageText(event);
        if (StringUtils.isNotBlank(messageText)) {
            int index = messageText.indexOf(QUERY_USER_KEYWORD);
            if (index >= 0) {
                String rest = messageText.substring(index + QUERY_USER_KEYWORD.length()).trim();
                if (rest.startsWith("列表")) {
                    return buildUserListReply(event, atUser);
                }
            }
        }
        // 解析用户标识（支持“查询用户 123”或“查询用户123”）
        String query = extractQueryId(messageText);
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
     * 菜单内容
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildMenuReply(OneBotMessageEvent event, boolean atUser) {
        StringBuilder text = new StringBuilder();
        text.append("用户管理指令\n");
        text.append("1. 查询用户 <社区ID|QQ>\n");
        text.append("2. 用户列表 [页码] [每页]\n");
        text.append("   可选筛选: 社区ID=xx QQ=xx 团队ID=xx 团队名=xx\n");
        text.append("3. 团队列表\n");
        text.append("4. 添加用户 <社区ID> <QQ> <团队ID|团队名> [社区昵称]\n");
        text.append("5. 修改用户 <社区ID|QQ> QQ=xx 社区ID=xx 昵称=xx\n");
        text.append("6. 删除用户 <社区ID|QQ>\n");
        text.append("示例: 用户列表 1 10");
        return replyText(event, atUser, text.toString());
    }

    /**
     * 统一处理列表查询并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserListReply(OneBotMessageEvent event, boolean atUser) {
        String argsText = extractArgs(extractMessageText(event), LIST_USER_KEYWORD);
        int pageNum = 1;
        int pageSize = 10;
        User query = new User();

        if (StringUtils.isNotBlank(argsText)) {
            Map<String, String> argsMap = parseKeyValueArguments(argsText);
            if (!argsMap.isEmpty()) {
                String pageValue = getArgValue(argsMap, "page", "页码");
                String sizeValue = getArgValue(argsMap, "size", "perPage", "每页");
                Integer parsedPage = parsePositiveInt(pageValue);
                Integer parsedSize = parsePositiveInt(sizeValue);
                if (pageValue != null && parsedPage == null) {
                    return replyText(event, atUser, "页码必须为正整数");
                }
                if (sizeValue != null && parsedSize == null) {
                    return replyText(event, atUser, "每页数量必须为正整数");
                }
                if (parsedPage != null) {
                    pageNum = parsedPage;
                }
                if (parsedSize != null) {
                    pageSize = parsedSize;
                }

                String hlxUserId = getArgValue(argsMap, "社区ID", "社区id", "hlxUserId", "hlx");
                String qq = getArgValue(argsMap, "QQ", "qq");
                String deptId = getArgValue(argsMap, "团队ID", "团队id", "deptId", "dept");
                String deptName = getArgValue(argsMap, "团队名", "团队名称", "团队", "deptName");
                if (StringUtils.isNotBlank(hlxUserId)) {
                    query.setHlxUserId(hlxUserId);
                }
                if (StringUtils.isNotBlank(qq)) {
                    query.setUsername(qq);
                }
                if (StringUtils.isNotBlank(deptId)) {
                    query.setDeptId(deptId);
                } else if (StringUtils.isNotBlank(deptName)) {
                    Dept dept = resolveDeptByValue(deptName);
                    if (dept == null) {
                        return replyText(event, atUser, "未找到团队: " + deptName);
                    }
                    query.setDeptId(dept.getId());
                }
            } else {
                String[] args = splitArguments(argsText);
                if (args.length >= 1) {
                    Integer parsedPage = parsePositiveInt(args[0]);
                    if (parsedPage == null) {
                        return replyText(event, atUser, "页码必须为正整数");
                    }
                    pageNum = parsedPage;
                }
                if (args.length >= 2) {
                    Integer parsedSize = parsePositiveInt(args[1]);
                    if (parsedSize == null) {
                        return replyText(event, atUser, "每页数量必须为正整数");
                    }
                    pageSize = parsedSize;
                }
            }
        }

        try {
            PageHelper.startPage(pageNum, pageSize);
            List<User> users = userDao.queryUsers(query);
            PageInfo<User> pageInfo = new PageInfo<>(users);
            if (users == null || users.isEmpty()) {
                return replyText(event, atUser, "暂无用户数据");
            }

            StringBuilder text = new StringBuilder();
            text.append("用户列表 (")
                    .append(pageInfo.getPageNum())
                    .append("/")
                    .append(pageInfo.getPages())
                    .append(", 共")
                    .append(pageInfo.getTotal())
                    .append("条)\n");

            int index = (pageInfo.getPageNum() - 1) * pageInfo.getPageSize();
            for (User user : users) {
                index++;
                text.append(index).append(". ");
                text.append("社区ID: ").append(valueOrDash(user.getHlxUserId())).append(" ");
                text.append("QQ: ").append(valueOrDash(user.getQq()));
                text.append(" 昵称: ").append(valueOrDash(user.getHlxUserNick()));
                if (StringUtils.isNotBlank(user.getDeptName())) {
                    text.append(" 团队: ").append(user.getDeptName());
                } else if (StringUtils.isNotBlank(user.getDeptId())) {
                    text.append(" 团队ID: ").append(user.getDeptId());
                }
                text.append("\n");
            }
            return replyText(event, atUser, text.toString().trim());
        } catch (Exception e) {
            logger.error("Query user list failed", e);
            return replyText(event, atUser, "查询用户列表失败");
        }
    }

    /**
     * 统一处理团队列表并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildDeptListReply(OneBotMessageEvent event, boolean atUser) {
        try {
            List<Dept> depts = deptDao.getDepts();
            if (depts == null || depts.isEmpty()) {
                return replyText(event, atUser, "暂无团队数据");
            }
            StringBuilder text = new StringBuilder();
            text.append("团队列表 (共").append(depts.size()).append("条)\n");
            int index = 0;
            for (Dept dept : depts) {
                index++;
                text.append(index).append(". ");
                text.append("团队ID: ").append(valueOrDash(dept.getId())).append(" ");
                text.append("团队名: ").append(valueOrDash(dept.getName())).append("\n");
            }
            return replyText(event, atUser, text.toString().trim());
        } catch (Exception e) {
            logger.error("Query dept list failed", e);
            return replyText(event, atUser, "查询团队列表失败");
        }
    }

    /**
     * 统一处理添加用户并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserAddReply(OneBotMessageEvent event, boolean atUser) {
        String argsText = extractArgs(extractMessageText(event), ADD_USER_KEYWORD);
        if (StringUtils.isBlank(argsText)) {
            return replyText(event, atUser, "用法: 添加用户 <社区ID> <QQ> <团队ID|团队名> [社区昵称]");
        }

        String hlxUserId = null;
        String qq = null;
        String deptValue = null;
        String nick = null;

        Map<String, String> argsMap = parseKeyValueArguments(argsText);
        if (!argsMap.isEmpty()) {
            hlxUserId = getArgValue(argsMap, "社区ID", "社区id", "hlxUserId", "hlx");
            qq = getArgValue(argsMap, "QQ", "qq");
            deptValue = getArgValue(argsMap, "团队ID", "团队id", "deptId");
            if (StringUtils.isBlank(deptValue)) {
                deptValue = getArgValue(argsMap, "团队名", "团队名称", "团队", "deptName", "dept");
            }
            nick = getArgValue(argsMap, "昵称", "社区昵称", "hlxUserNick", "nick");
        } else {
            String[] args = splitArguments(argsText);
            if (args.length >= 1) {
                hlxUserId = args[0];
            }
            if (args.length >= 2) {
                qq = args[1];
            }
            if (args.length >= 3) {
                deptValue = args[2];
            }
            if (args.length >= 4) {
                nick = args[3];
            }
        }

        if (StringUtils.isBlank(hlxUserId) || StringUtils.isBlank(qq) || StringUtils.isBlank(deptValue)) {
            return replyText(event, atUser, "用法: 添加用户 <社区ID> <QQ> <团队ID|团队名> [社区昵称]");
        }
        if (hlxUserId.length() > 10 || qq.length() > 11) {
            return replyText(event, atUser, "录入信息不合规则，请仔细检查后重试");
        }
        if (userService.getCountByUserName(qq) != 0) {
            return replyText(event, atUser, "用户名已存在，请重试");
        }

        Dept dept = resolveDeptByValue(deptValue);
        if (dept == null) {
            return replyText(event, atUser, "未找到团队: " + deptValue);
        }

        User user = new User();
        user.setHlxUserId(hlxUserId);
        user.setQq(qq);
        user.setDeptId(dept.getId());
        if (StringUtils.isNotBlank(nick)) {
            user.setHlxUserNick(nick);
        }
        user.setUsername(qq);

        try {
            int result = userService.insetUser(user);
            if (result <= 0) {
                return replyText(event, atUser, "添加失败，请联系管理员");
            }
            StringBuilder text = new StringBuilder();
            text.append("添加成功\n");
            text.append("用户ID: ").append(valueOrDash(user.getId())).append("\n");
            text.append("社区ID: ").append(valueOrDash(user.getHlxUserId())).append("\n");
            text.append("QQ: ").append(valueOrDash(user.getQq())).append("\n");
            text.append("团队ID: ").append(valueOrDash(user.getDeptId()));
            if (StringUtils.isNotBlank(dept.getName())) {
                text.append("\n团队名: ").append(valueOrDash(dept.getName()));
            }
            return replyText(event, atUser, text.toString());
        } catch (Exception e) {
            logger.error("Add user failed", e);
            return replyText(event, atUser, "添加失败，请联系管理员");
        }
    }

    /**
     * 统一处理修改用户并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserUpdateReply(OneBotMessageEvent event, boolean atUser) {
        String messageText = extractMessageText(event);
        String argsText = extractArgs(messageText, UPDATE_USER_KEYWORD);
        if (StringUtils.isBlank(argsText)) {
            return replyText(event, atUser, "用法: 修改用户 <社区ID|QQ> QQ=xx 社区ID=xx 昵称=xx");
        }

        String[] args = splitArguments(argsText);
        if (args.length < 2) {
            return replyText(event, atUser, "用法: 修改用户 <社区ID|QQ> QQ=xx 社区ID=xx 昵称=xx");
        }

        String identifier = args[0];
        String optionsText = argsText.substring(argsText.indexOf(identifier) + identifier.length()).trim();
        Map<String, String> argsMap = parseKeyValueArguments(optionsText);
        if (argsMap.isEmpty()) {
            return replyText(event, atUser, "用法: 修改用户 <社区ID|QQ> QQ=xx 社区ID=xx 昵称=xx");
        }

        String newHlxUserId = getArgValue(argsMap, "社区ID", "社区id", "hlxUserId", "hlx");
        String newQq = getArgValue(argsMap, "QQ", "qq");
        String newNick = getArgValue(argsMap, "昵称", "社区昵称", "hlxUserNick", "nick");
        if (StringUtils.isBlank(newHlxUserId) && StringUtils.isBlank(newQq) && StringUtils.isBlank(newNick)) {
            return replyText(event, atUser, "至少需要一个修改项");
        }
        if (StringUtils.isNotBlank(newHlxUserId) && newHlxUserId.length() > 10) {
            return replyText(event, atUser, "社区ID长度不合法");
        }
        if (StringUtils.isNotBlank(newQq) && newQq.length() > 11) {
            return replyText(event, atUser, "QQ长度不合法");
        }

        User user = resolveUserByIdentifier(identifier);
        if (user == null) {
            return replyText(event, atUser, "未找到用户: " + identifier);
        }

        if (StringUtils.isNotBlank(newQq) && !newQq.equals(user.getQq())) {
            if (userService.getCountByUserName(newQq) != 0) {
                return replyText(event, atUser, "用户名已存在，请重试");
            }
        }

        User update = new User();
        update.setId(user.getId());
        if (StringUtils.isNotBlank(newHlxUserId)) {
            update.setHlxUserId(newHlxUserId);
        }
        if (StringUtils.isNotBlank(newQq)) {
            update.setQq(newQq);
        }
        if (StringUtils.isNotBlank(newNick)) {
            update.setHlxUserNick(newNick);
        }

        try {
            userService.updateUserById(update);
            return replyText(event, atUser, "修改成功");
        } catch (Exception e) {
            logger.error("Update user failed", e);
            return replyText(event, atUser, "修改失败，请联系管理员");
        }
    }

    /**
     * 统一处理删除用户并返回 OneBot 回复体
     *
     * @param event  OneBot 消息事件
     * @param atUser 是否在群聊中@发送者
     * @return OneBot 回复 JSON
     */
    private JSONObject buildUserDeleteReply(OneBotMessageEvent event, boolean atUser) {
        String argsText = extractArgs(extractMessageText(event), DELETE_USER_KEYWORD);
        if (StringUtils.isBlank(argsText)) {
            return replyText(event, atUser, "用法: 删除用户 <社区ID|QQ>");
        }

        List<String> identifiers = splitIdentifiers(argsText);
        if (identifiers.isEmpty()) {
            return replyText(event, atUser, "用法: 删除用户 <社区ID|QQ>");
        }

        Set<String> ids = new LinkedHashSet<>();
        List<String> notFound = new ArrayList<>();
        for (String identifier : identifiers) {
            User user = resolveUserByIdentifier(identifier);
            if (user == null) {
                notFound.add(identifier);
            } else {
                ids.add(user.getId());
            }
        }

        if (ids.isEmpty()) {
            return replyText(event, atUser, "未找到用户: " + String.join(",", notFound));
        }

        try {
            userService.delUserById(ids.toArray(new String[0]));
            StringBuilder text = new StringBuilder();
            text.append("删除成功: ").append(ids.size()).append(" 人");
            if (!notFound.isEmpty()) {
                text.append("\n未找到: ").append(String.join(",", notFound));
            }
            return replyText(event, atUser, text.toString());
        } catch (Exception e) {
            logger.error("Delete user failed", e);
            return replyText(event, atUser, "删除失败，请联系管理员");
        }
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
        String replyText = text;
        if (atUser && event instanceof OneBotGroupMessageEvent) {
            replyText = "\n" + (text == null ? "" : text);
        }
        builder.text(replyText);
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
     * 从原始消息中提取参数文本
     *
     * @param messageText 消息文本
     * @param keyword     关键字
     * @return 参数文本
     */
    private String extractArgs(String messageText, String keyword) {
        if (StringUtils.isBlank(messageText) || StringUtils.isBlank(keyword)) {
            return null;
        }
        int index = messageText.indexOf(keyword);
        if (index < 0) {
            return null;
        }
        String rest = messageText.substring(index + keyword.length()).trim();
        return StringUtils.isNotBlank(rest) ? rest : null;
    }

    /**
     * 解析参数文本为 key=value
     *
     * @param argsText 参数文本
     * @return 参数 Map
     */
    private Map<String, String> parseKeyValueArguments(String argsText) {
        Map<String, String> result = new LinkedHashMap<>();
        if (StringUtils.isBlank(argsText)) {
            return result;
        }
        String normalized = argsText.replace('，', ',').replace(',', ' ');
        String[] parts = normalized.trim().split("\\s+");
        for (String part : parts) {
            if (StringUtils.isBlank(part)) {
                continue;
            }
            String normalizedPart = part.replace('：', ':');
            int sep = normalizedPart.indexOf('=');
            if (sep < 0) {
                sep = normalizedPart.indexOf(':');
            }
            if (sep <= 0 || sep >= normalizedPart.length() - 1) {
                continue;
            }
            String key = normalizedPart.substring(0, sep).trim();
            String value = normalizedPart.substring(sep + 1).trim();
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                result.put(key, value);
            }
        }
        return result;
    }

    /**
     * 从参数 Map 中按优先级获取值
     *
     * @param args 参数 Map
     * @param keys 可选 key
     * @return 参数值
     */
    private String getArgValue(Map<String, String> args, String... keys) {
        if (args == null || args.isEmpty() || keys == null || keys.length == 0) {
            return null;
        }
        for (String key : keys) {
            if (args.containsKey(key)) {
                return args.get(key);
            }
        }
        for (Map.Entry<String, String> entry : args.entrySet()) {
            for (String key : keys) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 参数分割
     *
     * @param argsText 参数文本
     * @return 参数数组
     */
    private String[] splitArguments(String argsText) {
        if (StringUtils.isBlank(argsText)) {
            return new String[0];
        }
        return argsText.trim().split("\\s+");
    }

    /**
     * 分割删除参数
     *
     * @param argsText 参数文本
     * @return 参数列表
     */
    private List<String> splitIdentifiers(String argsText) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isBlank(argsText)) {
            return result;
        }
        String normalized = argsText.replace('，', ',');
        String[] parts = normalized.split("[,\\s]+");
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                result.add(part.trim());
            }
        }
        return result;
    }

    /**
     * 解析正整数
     *
     * @param value 字符串
     * @return 正整数或 null
     */
    private Integer parsePositiveInt(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 根据标识查找用户（社区ID/QQ/用户ID）
     *
     * @param identifier 标识
     * @return 用户信息
     */
    private User resolveUserByIdentifier(String identifier) {
        if (StringUtils.isBlank(identifier)) {
            return null;
        }
        String normalized = identifier;
        if (identifier.contains("=") || identifier.contains(":") || identifier.contains("：")) {
            Map<String, String> argsMap = parseKeyValueArguments(identifier);
            String parsed = getArgValue(argsMap, "id", "ID", "用户ID", "社区ID", "社区id", "QQ", "qq", "hlxUserId", "hlx");
            if (StringUtils.isNotBlank(parsed)) {
                normalized = parsed;
            }
        }

        User user = userService.queryUserByHlxUserId(normalized);
        if (user == null) {
            user = userService.getUserByUserName(normalized);
        }
        if (user == null && UUID_PATTERN.matcher(normalized).matches()) {
            try {
                user = userDao.selectByPrimaryKey(normalized);
            } catch (Exception e) {
                logger.warn("Query user by id failed: {}", normalized, e);
            }
        }
        return user;
    }

    /**
     * 根据团队标识查找团队（团队ID/团队名）
     *
     * @param deptValue 团队标识
     * @return 团队信息
     */
    private Dept resolveDeptByValue(String deptValue) {
        if (StringUtils.isBlank(deptValue)) {
            return null;
        }
        String normalized = deptValue.trim();
        Dept dept = null;
        try {
            dept = deptDao.selectByPrimaryKey(normalized);
        } catch (Exception e) {
            logger.warn("Query dept by id failed: {}", normalized, e);
        }
        if (dept != null) {
            if (dept.getState() == null || dept.getState() == 0) {
                return dept;
            }
            dept = null;
        }

        try {
            List<Dept> depts = deptDao.getDepts();
            if (depts != null) {
                for (Dept item : depts) {
                    if (item != null && normalized.equals(item.getName())) {
                        return item;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Query dept by name failed: {}", normalized, e);
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
