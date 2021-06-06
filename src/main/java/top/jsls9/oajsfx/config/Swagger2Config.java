package top.jsls9.oajsfx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.jsls9.oajsfx.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("OA-JSFX Api文档")
                .description("一个独立于社区外的人员管理系统。")
                .termsOfServiceUrl("https://www.jsls9.top")
                .contact(new Contact("bsulike",  "https://www.jsls9.top","bsulike@gmail.com"))
                .version("1.0")
                .build();
    }



}

// 注意："com.imooc.product.controller" 指的是你的controller包路径