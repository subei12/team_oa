package top.jsls9.oajsfx.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author bSu
 * @date 2020/5/12 - 10:34
 */
@Configuration
public class MyBatisConfigForMysql {



    /*@Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.anran.model.pojo");
        return sqlSessionFactoryBean.getObject();
    }
*/

}
