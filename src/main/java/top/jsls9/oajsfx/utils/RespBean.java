package top.jsls9.oajsfx.utils;


/** 为适配Amis 0-success 1-error
 * 返回前台的值,object需要强转有风险，泛型会好一点
 * @author bSu
 */
//200:success
//500:error
//401:未登录
//403:无权限
public class RespBean {
    //状态码
    private Integer status;
    //消息
    private String msg;
    //值
    private Object data;

    public static RespBean build() {
        return new RespBean();
    }

    public static RespBean success(String msg) {
        return new RespBean(0, msg, null);
    }

    public static RespBean success(String msg, Object data) {
        return new RespBean(0, msg, data);
    }

    public static RespBean error(String msg) {
        return new RespBean(1, msg, null);
    }

    public static RespBean error(String msg, Object data) {
        return new RespBean(1, msg, data);
    }

    public static RespBean error(Integer code,String msg, Object data) {
        return new RespBean(code, msg, data);
    }

    private RespBean() {
    }

    /**
     * 我已经不记得这里为什么要放开public了，最开始应该是private,应为我有build方法
     * @param status
     * @param msg
     * @param data
     */
    public RespBean(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void getStatus(Integer status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
