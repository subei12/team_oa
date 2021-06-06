package top.jsls9.oajsfx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 便于团队统计的OA平台
 */
@SpringBootApplication
//开启事务
@EnableTransactionManagement
public class OaJsfxApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(OaJsfxApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
