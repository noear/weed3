package webapp;


import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XBean;
import org.noear.solon.annotation.XConfiguration;
import org.noear.solon.annotation.XInject;
import org.noear.solon.core.XMap;
import org.noear.weed.DbContext;
import org.noear.weed.DbDataSource;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import webapp.dso.DataSourceHelper;

import javax.sql.DataSource;
import java.util.Properties;

@XConfiguration
public class Config {
    //
    //缓存服务配置:: //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
    //
    public final static ICacheServiceEx cache = new LocalCache("test", 60).nameSet("test");


    //
    //直接配置 数据库上下文
    //
    @XBean("db1")
    public final DbContext db1(@XInject("${test.db1}") Properties props) {
        DataSource ds = dataSource(props);

        DataSourceHelper.initData(ds);

        return new DbContext(props).nameSet("db1");
    }


    //
    //使用连接池 配置 数据库上下文
    //
    private final static HikariDataSource dataSource(Properties props) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));

        return dataSource;
    }

    public final static DbContext db2() {
        DataSource ds = dataSource(XApp.cfg().getProp("test.db1"));

        DataSourceHelper.initData(ds);

        return new DbContext().dataSourceSet(ds);
    }
}
