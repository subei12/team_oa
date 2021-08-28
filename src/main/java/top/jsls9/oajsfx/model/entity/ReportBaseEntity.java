package top.jsls9.oajsfx.model.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**上报基类
 * @author bSu
 * @date 2021/8/14 - 20:49
 */
@Component
public class ReportBaseEntity {


    /**
     * 上报单据id
     */
    private String reportId;

    /**
     * 标题
     */
    private String titleName;

    /**
     * 流程代码,用来标记表单
     */

    private String processId;

    /**
     * 上报群号
     */
    @Value("${qq.report.group}")
    public String group ;



    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
