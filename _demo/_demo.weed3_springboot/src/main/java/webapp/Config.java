package webapp;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import webapp.dso.DataSourceHelper;

import javax.sql.DataSource;

@Configuration
public class Config {
    //
    //cache
    //
    @Bean
    public ICacheServiceEx cache(){
        //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
        return new LocalCache("test",60).nameSet("test");
    }



    @Bean(name = "db1p")
    @ConfigurationProperties(prefix = "test.db1")
    public DataSourceProperties dataSource() {
        return new DataSourceProperties();
    }

    @Bean("db1")
    @Primary
    public DbContext db2(@Qualifier("db1p") DataSourceProperties db1p){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(db1p.getUrl());
        dataSource.setUsername(db1p.getUsername());
        dataSource.setPassword(db1p.getPassword());
        dataSource.setDriverClassName(db1p.getDriverClassName());

        DataSourceHelper.initData(dataSource);

        return new DbContext(dataSource);
    }
}
