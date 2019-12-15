package weed3demo;

import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

public class DbUtil {
    public static DbContext getDb() {
        Map<String, String> map = new HashMap<>();

        map.put("schema", "rock");
        map.put("url", "jdbc:mysql://localdb:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        map.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        map.put("username", "demo");
        map.put("password", "UL0hHlg0Ybq60xyb");


        DbContext db = new DbContext(map);

        return db;
    }

    public static DbContext db = getDb();
    public static ICacheService cache = new LocalCache().nameSet("test");
}
