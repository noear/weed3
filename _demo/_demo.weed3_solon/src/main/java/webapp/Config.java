package webapp;


import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import webapp.dso.DsHelper;

import javax.sql.DataSource;

@Configuration
public class Config {
    //
    //缓存服务配置:: //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
    //
    public final static ICacheServiceEx cache = new LocalCache("test", 60).nameSet("test");


    //
    //直接配置 数据库上下文
    //
    @Bean(value = "db1" ,typed = true)
    public DataSource db1(@Inject("${test.db1}") HikariDataSource dataSource) {
        DsHelper.initData(dataSource);
        return dataSource;
    }
}
