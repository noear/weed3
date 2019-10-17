package test.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.noear.weed.DbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Bean(name = "dataSource",destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }


    @Bean(name = "db")
    @Primary
    public DbContext db() throws Exception {
        return new DbContext()
                .dataSourceSet(dataSource)
                .nameSet("db");
    }
}
