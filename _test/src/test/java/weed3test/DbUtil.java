package weed3test;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

public class DbUtil {

    private final static Map<String, String> dbMysqlCfg(){
        Map<String, String> map = new HashMap<>();

        map.put("schema", "rock");
        map.put("url", "jdbc:mysql://localdb:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        map.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        map.put("username", "demo");
        map.put("password", "UL0hHlg0Ybq60xyb");

        return map;
    }

    private final static Map<String, String> dbMssqlCfg(){
        Map<String, String> map = new HashMap<>();

        map.put("schema", "rock");
        map.put("url", "jdbc:sqlserver://localdb:1433;DatabaseName=rock");
        map.put("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        map.put("username", "sa");
        map.put("password", "sqlsev@2019");

        return map;
    }

    private final static Map<String, String> dbOracleCfg(){
        Map<String, String> map = new HashMap<>();

        map.put("schema", "demo");
        map.put("url", "jdbc:oracle:thin:@//192.168.8.118:1521/demo ");
        map.put("driverClassName", "oracle.jdbc.OracleDriver");
        map.put("username", "demo");
        map.put("password", "demo");

        return map;
    }

    private final static Map<String, String> dbPgsqlCfg(){
        Map<String, String> map = new HashMap<>();

        map.put("schema", "public");
        map.put("url", "jdbc:postgresql://localdb:5432/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        map.put("driverClassName", "org.postgresql.Driver");
        map.put("username", "postgres");
        map.put("password", "postgres");

        return map;
    }

    private final static HikariDataSource dataSource(Map<String, String> map){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(map.get("url"));
        dataSource.setUsername(map.get("username"));
        dataSource.setPassword(map.get("password"));
        dataSource.setDriverClassName(map.get("driverClassName"));

        return dataSource;
    }

    public static DbContext getDb() {
        //dbOracleCfg(); //

        Map<String, String> map =dbMysqlCfg(); //  dbPgsqlCfg(); //dbMssqlCfg();//

        DbContext db = new DbContext(map.get("schema"), dataSource(map)).nameSet("rock");
        WeedConfig.isUsingSchemaPrefix =true;
        return db;
    }

    public static DbContext db = getDb();
    public static ICacheServiceEx cache = new LocalCache().nameSet("test");
}
