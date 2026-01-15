package top.jsls9.oajsfx.onebot.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * OneBot 事件基类
 *
 * @author bSu
 */
public class OneBotEvent {
    /**
     * 事件发生时间戳
     */
    @JSONField(name = "time")
    private Long time;

    /**
     * 收到事件的机器人 QQ 号
     */
    @JSONField(name = "self_id")
    private Long selfId;

    /**
     * 上报类型 (message, notice, request, meta_event)
     */
    @JSONField(name = "post_type")
    private String postType;

    /**
     * 获取事件发生时间
     * @return 时间戳
     */
    public Long getTime() {
        return time;
    }

    /**
     * 设置事件发生时间
     * @param time 时间戳
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * 获取机器人 QQ 号
     * @return QQ 号
     */
    public Long getSelfId() {
        return selfId;
    }

    /**
     * 设置机器人 QQ 号
     * @param selfId QQ 号
     */
    public void setSelfId(Long selfId) {
        this.selfId = selfId;
    }

    /**
     * 获取上报类型
     * @return 上报类型
     */
    public String getPostType() {
        return postType;
    }

    /**
     * 设置上报类型
     * @param postType 上报类型
     */
    public void setPostType(String postType) {
        this.postType = postType;
    }
}