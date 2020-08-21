package weed3test;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

public class DbUtil {

    private final static HikariDataSource dbMysqlCfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:mysql://localdb:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        ds.setUsername("demo");
        ds.setPassword("UL0hHlg0Ybq60xyb");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return ds;
    }

    private final static HikariDataSource dbH2Cfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:h2:mem:rock;DB_CLOSE_ON_EXIT=FALSE");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.h2.Driver");

        return ds;
    }

    private final static HikariDataSource dbDb2Cfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:db2://localdb:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        ds.setUsername("demo");
        ds.setPassword("UL0hHlg0Ybq60xyb");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return ds;
    }

    private final static HikariDataSource dbSqlserverCfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:sqlserver://localdb:1433;DatabaseName=rock");
        ds.setUsername("sa");
        ds.setPassword("sqlsev@2019");
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        return ds;
    }

    private final static HikariDataSource dbOracleCfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("demo");
        ds.setJdbcUrl("jdbc:oracle:thin:@//192.168.8.118:1521/helowinXDB");
        ds.setUsername("demo");
        ds.setPassword("demo");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");

        return ds;
    }

    private final static HikariDataSource dbPgsqlCfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("public");
        ds.setJdbcUrl("jdbc:postgresql://localdb:5432/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        ds.setDriverClassName("org.postgresql.Driver");

        return ds;
    }


    public static DbContext getDb() {
        //

        WeedConfig.onException((cmd,ex)->{
            System.out.println(cmd.text);
        });

        WeedConfig.onExecuteAft((cmd)->{
            System.out.println(":::"+cmd.text);
        });

        HikariDataSource source = dbMysqlCfg(); // dbOracleCfg(); //  dbPgsqlCfg(); // dbMssqlCfg(); //

        DbContext db = new DbContext(source.getSchema(), source).nameSet("rock");
        WeedConfig.isUsingSchemaPrefix =true;
        return db;
    }

    public static DbContext db = getDb();
    public static ICacheServiceEx cache = new LocalCache().nameSet("test");
}
