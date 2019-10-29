package webapp.dso;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Properties;

@Setter
@ConfigurationProperties(prefix = "test")
public class DbConfig {
    //
    //cache
    //
    @Bean("cache")
    public ICacheServiceEx cache(){
        //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
        return new LocalCache("test",60).nameSet("test");
    }

    //
    // db1
    //
    protected Properties db1;

    @Bean("db1")
    public DbContext db1(){
        return new DbContext(db1);
    }

    //
    // db2
    //
    @Bean(name = "dataSource", destroyMethod = "close")
    @ConfigurationProperties(prefix = "test.db2")
    @Primary
    public HikariDataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean("db2")
    public DbContext db2(@Qualifier("dataSource") DataSource dataSource){
        return new DbContext().dataSourceSet(dataSource);
    }

}
