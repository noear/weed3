package webapp;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "dataSource", destroyMethod = "close")
    @ConfigurationProperties(prefix = "test.db1")
    public HikariDataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean("db1")
    @Primary
    public DbContext db2(@Qualifier("dataSource") DataSource dataSource){

        DataSourceHelper.initData(dataSource);

        return new DbContext().dataSourceSet(dataSource);
    }
}
