package weed3demo.mapper;

import org.noear.weed.DbContext;
import org.noear.weed.DbQuery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlSqlFactory {
    private Map<String, IXmlSqlBuilder> _sqlMap = new ConcurrentHashMap<>();

    public void register(String name, IXmlSqlBuilder xmlSqlBuilder) {
        _sqlMap.put(name, xmlSqlBuilder);
    }

    public DbQuery call(DbContext db, String name, Map map) {
        IXmlSqlBuilder xmlSqlBuilder = _sqlMap.get(name);
        if (xmlSqlBuilder != null) {
            return db.sql(xmlSqlBuilder.build(map));
        }

        return null;
    }
}
