package noear.weed;

import noear.weed.ext.Act1Ex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by noear on 14-6-12.
 * 数据库上下文
 */
public class DbContext {

    //基于线程池配置（如："proxool."）
    public DbContext(String schemaName,String url) {
        _schemaName = schemaName;
        _url = url;
    }

    //基于手动配置（无线程池）
    public DbContext(String schemaName,String url,String user,String password) {
        _schemaName = schemaName;
        _url = url;
        _user = user;
        _password = password;
    }

    private String _url;
    private String _user;
    private String _password;

    private String _schemaName;

    /*是否配置了schema*/
    public boolean hasSchema(){return _schemaName!=null;}

    /*获取schema*/
    public String getSchema(){
        return _schemaName;
    }

    /*获取连接*/
    public  Connection getConnection() throws SQLException {
        if (_user == null)
            return DriverManager.getConnection(_url);
        else
            return DriverManager.getConnection(_url, _user, _password);
    }

    public DbQuery sql(String code, Object... args) {
        return new DbQuery(this).sql(new SQLBuilder().append(code, args));
    }

    public DbQuery sql(SQLBuilder sqlBuilder) {
        return new DbQuery(this).sql(sqlBuilder);
    }

    /*获取process执行对象*/
    public DbStoredProcedure call(String process) {
        return new DbStoredProcedure(this).call(process);
    }

    /*获取一个表对象［用于操作插入也更新］*/
    public DbTableQuery table(String table) {
        return new DbTableQuery(this).table(table);
    }

    public DbTran tran(Act1Ex<DbTran,SQLException> handler) throws SQLException {
        return new DbTran(this).execute(handler);
    }

    public DbTran tran()
    {
        return new DbTran(this);
    }

}
