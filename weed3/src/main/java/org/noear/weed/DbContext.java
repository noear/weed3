package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act1Ex;
import org.noear.weed.ext.Get1;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.xml.XmlSqlLoader;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by noear on 14-6-12.
 * 数据库上下文
 */
public class DbContext extends DbContextBase<DbContext>{


    //
    // 构建函数 start
    //
    public DbContext() {
    }

    public DbContext(Properties properties) {
        propSet(properties);
    }

    public DbContext(Map map) {
        propSet(map);
    }

    //基于线程池配置（如："proxool."）
    //fieldFormat："`%`"
    public DbContext(String schema, String url) {
        _schema = schema;
        _dataSource = new DbDataSource(url);
    }

    //基于手动配置（无线程池）
    public DbContext(String schema, String url, String username, String password) {
        _schema = schema;
        _dataSource = new DbDataSource(url, username, password);
    }

    public DbContext(String schema, DataSource dataSource) {
        _schema = schema;
        _dataSource = dataSource;
    }

    //
    // 执行能力支持
    //

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return _dataSource.getConnection();
    }

    public <T> T mapper(Class<T> clz){
        if(WeedConfig.libOfDb.containsKey(clz) == false) {
            WeedConfig.libOfDb.put(clz, this);
        }

        return XSqlMapper.get(clz);
    }

    /**
     * 执行代码，返回影响行数
     */
    public int exec(String code, Object... args) throws Exception {
        return new DbQuery(this).sql(new SQLBuilder().append(code, args)).execute();
    }

    public <T> T exec(String sqlid, Map<String,Object> paramS) throws Exception {
        return (T) XSqlUtil.exec(this, sqlid, paramS, null, null);
    }

    /**
     * 输入SQL，获取查询器
     */
    public DbQuery sql(String code, Object... args) {
        return sql(new SQLBuilder().append(code, args));
    }

    /**
     * 输入SQL builder，获取查询器
     */
    public DbQuery sql(Act1<SQLBuilder> buildRuner) {
        SQLBuilder sql = new SQLBuilder();
        buildRuner.run(sql);
        return sql(sql);
    }

    /**
     * 输入SQL builder，获取查询器
     */
    public DbQuery sql(SQLBuilder sqlBuilder) {
        return new DbQuery(this).sql(sqlBuilder);
    }


    /**
     * 输入process name，获取process执行对象
     */
    public DbProcedure call(String process) {
        if (process.startsWith("@")) {
            XmlSqlLoader.tryLoad();
            return new DbSqlProcedure(this).sql(process.substring(1));
        }

        if (process.indexOf(" ") > 0) {
            return new DbQueryProcedure(this).sql(process);
        }

        return new DbStoredProcedure(this).call(process);
    }


    /**
     * 获取一个表对象［用于操作插入也更新］
     */
    public DbTableQuery table(String table) {
        return new DbTableQuery(this).table(table);
    }

    public DbTran tran(Act1Ex<DbTran, Exception> handler) throws Exception {
        return new DbTran(this).execute(handler);
    }

    public DbTran tran() {
        return new DbTran(this);
    }

    public DbTranQueue tranQueue(Act1Ex<DbTranQueue, Exception> handler) throws Exception {
        return new DbTranQueue().execute(handler);
    }
}
