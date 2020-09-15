package webapp;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import webapp.dso.DataSourceHelper;


@Configuration
public class Config {

    @Bean
    public ICacheServiceEx cache(){
        return new LocalCache("test",60);
    }

    @Bean
    @ConfigurationProperties(prefix = "test.db1")
    public HikariDataSource ds1(){
        return new HikariDataSource();
    }

    @Bean("db1")
    @Primary
    public DbContext db2(HikariDataSource dataSource){
        DataSourceHelper.initData(dataSource);

        return new DbContext(dataSource);
    }
}
