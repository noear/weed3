package weed3adb;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.Utils;
import org.noear.weed.DbContext;
import org.noear.weed.DbDataSource;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import javax.sql.DataSource;
import java.io.InputStream;

public class DbUtil {

    private final static DataSource dbClickHouseCfg() {
        DbDataSource ds = new DbDataSource("jdbc:clickhouse://localhost:8123/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");

        ds.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");

        return ds;
    }

    private final static DataSource dbPrestoCfg() {
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:presto://localhost:8123/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        //ds.setUsername("root");
        //ds.setPassword("123456");
        ds.setDriverClassName("io.prestosql.jdbc.PrestoDriver");

        return ds;
    }


    public static DbContext getDb() {
        //

        WeedConfig.onException((cmd, ex) -> {
            System.out.println(cmd.text);
        });

        WeedConfig.onExecuteAft((cmd) -> {
            if (cmd.isBatch) {
                System.out.println(":::" + cmd.text + " --:batch");
            } else {
                System.out.println(":::" + cmd.text);
            }
        });

        DataSource source = dbClickHouseCfg();

        DbContext db = new DbContext(source).nameSet("rock");
        //WeedConfig.isUsingSchemaPrefix =true;
        //WeedConfig.isUsingUnderlineColumnName=true;
        db.initMetaData();
        return db;
    }

    public static DbContext db = getDb();
}
