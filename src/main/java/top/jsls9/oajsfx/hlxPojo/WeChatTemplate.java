package top.jsls9.oajsfx.hlxPojo;

import java.util.Map;

/** 用于支持发信的模板
 * @author bSu
 * @date 2021/1/31 - 21:08
 */
public class WeChatTemplate {

    //为了支持可以同时多个模板，就不获取配置信息好了

    private String touser;

    private String template_id;

    private String url;

    private Object data;

    public WeChatTemplate() {
    }

    public WeChatTemplate(String touser, String template_id, String url, Object data) {
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.data = data;
    }

    public String getTouser() {
        return touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public String getUrl() {
        return url;
    }

    public Object getData() {
        return data;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WeChatTemplate{" +
                "touser='" + touser + '\'' +
                ", template_id='" + template_id + '\'' +
                ", url='" + url + '\'' +
                ", data=" + data +
                '}';
    }
}
