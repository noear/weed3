package webapp;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import webapp.dso.DataSourceHelper;

import javax.sql.DataSource;
import java.util.Properties;

@Setter
@ConfigurationProperties("test.db1")
public class Config {

    @Autowired
    ApplicationContext ctx;

    //
    //cache
    //
    @Bean
    public ICacheServiceEx cache(){
        //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
        return new LocalCache("test",60).nameSet("test");
    }

    private String url;
    private String driverClassName;
    private Properties db1;

    @Bean("db1")
    @Primary
    public DbContext db2(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(db1.getProperty("url"));
        dataSource.setUsername(db1.getProperty("username"));
        dataSource.setPassword(db1.getProperty("password"));
        dataSource.setDriverClassName(db1.getProperty("driverClassName"));

        DataSourceHelper.initData(dataSource);

        return new DbContext(dataSource);
    }
}
