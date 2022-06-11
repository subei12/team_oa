package top.jsls9.oajsfx.enums;

/**
 * @author bSu
 * @date 2022/6/11 - 18:17
 */
public enum  SysSourceLogType {

    /**
     * 帖子直接结算
     */
    TYPE1(1),

    /**
     * oa账户提现
     */
    TYPE2(2),

    /**
     * 团队自定义奖励
     */
    TYPE3(3),

    /**
     * 团队成员自荐优质内容奖励
     */
    TYPE4(4);

    private int value;

    private SysSourceLogType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
