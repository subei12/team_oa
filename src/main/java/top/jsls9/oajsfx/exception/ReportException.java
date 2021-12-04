package top.jsls9.oajsfx.exception;

/**上报异常类，不做全局异常处理
 * @author bSu
 * @date 2021/12/4 - 17:17
 */
public class ReportException extends Exception{

    public ReportException() {
    }

    public ReportException(String message) {
        super(message);
    }


}
