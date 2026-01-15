package top.jsls9.oajsfx.onebot.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * OneBot 群消息事件
 *
 * @author bSu
 */
public class OneBotGroupMessageEvent extends OneBotMessageEvent {
    /**
     * 群号
     */
    @JSONField(name = "group_id")
    private Long groupId;

    /**
     * 匿名信息 (如果不是匿名消息则为 null)
     */
    @JSONField(name = "anonymous")
    private Object anonymous;

    /**
     * 获取群号
     * @return 群号
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置群号
     * @param groupId 群号
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取匿名信息
     * @return 匿名信息对象
     */
    public Object getAnonymous() {
        return anonymous;
    }

    /**
     * 设置匿名信息
     * @param anonymous 匿名信息对象
     */
    public void setAnonymous(Object anonymous) {
        this.anonymous = anonymous;
    }
}