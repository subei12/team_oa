package top.jsls9.oajsfx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author bSu
 * @date 2021/3/7 - 22:37
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //重写这个方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")//这里写哪个启用哪个，doc.html=美化版本，swagger-ui.html=原版
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
