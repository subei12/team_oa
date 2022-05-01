package top.jsls9.oajsfx.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** https://github.com/zhaojun1998/Shiro-Action/blob/master/src/main/java/im/zhaojun/common/util/WebHelper.java
 * @author bSu
 * @date 2022/4/20 - 20:47
 */
public class WebHelper {

    /**
     * 是否是Ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }

    /**
     * 输出JSON
     */
    public static void writeJson(Object object, ServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSON.toJSONString(object));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void redirectUrl(String redirectUrl) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前请求的 Http Method
     * @return
     */
    public static String getRequestHTTPMethod() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getMethod();
    }

}
