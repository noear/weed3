package weed3test;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.Utils;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.io.InputStream;

public class DbUtil {

    private final static HikariDataSource dbMysqlCfg(){
        HikariDataSource ds = new HikariDataSource();

        ds.setSchema("rock");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return ds;
    }

    private final static HikariDataSource dbH2Cfg(){
        HikariDataSource ds = new HikariDataSource();

        //ds.setSchema("PUBLIC");
        ds.setJdbcUrl("jdbc:h2:mem:rock;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.h2.Driver");

        //初始化表构建和和数据
        String[] sqlAry = getSqlFromFile("/db/rock_h2.sql");
        DbContext db = new DbContext("rock",ds);
        try {
            for (String s1 : sqlAry) {
                if(s1.length() > 10) {
                    db.exe(s1);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ds;
    }

    private final static HikariDataSource dbSqliteCfg(){
        HikariDataSource ds = new HikariDataSource();

        //ds.setSchema("PUBLIC");
        ds.setJdbcUrl("jdbc:sqlite::memory:");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.sqlite.JDBC");

        //初始化表构建和和数据
        String[] sqlAry = getSqlFromFile("/db/rock_sqlite.sql");
        DbContext db = new DbContext("rock",ds);
        try {
            for (String s1 : sqlAry) {
                if(s1.length() > 10) {
                    db.exe(s1);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ds;
    }

    private static String[] getSqlFromFile(String uri){
        try{
            InputStream ins = Utils.getResource(uri).openStream();
            int len = ins.available();
            byte[] bs = new byte[len];
            ins.read(bs);
            String str = new String(bs,"UTF-8");
            String[] sql = str.split(";");
            return sql;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
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

        HikariDataSource source = dbMysqlCfg(); // dbH2Cfg(); // dbSqliteCfg(); // dbH2Cfg(); // dbOracleCfg(); //  dbPgsqlCfg(); // dbMssqlCfg(); //

        DbContext db = new DbContext(source).nameSet("rock");
        //WeedConfig.isUsingSchemaPrefix =true;
        //WeedConfig.isUsingUnderlineColumnName=true;
        db.initMetaData();
        return db;
    }

    public static DbContext db = getDb();
    public static ICacheServiceEx cache = new LocalCache().nameSet("test");
}
