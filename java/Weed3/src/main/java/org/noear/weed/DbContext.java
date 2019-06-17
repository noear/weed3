package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act1Ex;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 14-6-12.
 * 数据库上下文
 */
public class DbContext {
    public Command lastCommand;
    public boolean allowMultiQueries;

    public boolean isCompilationMode=false;

    //添加特性支持
    private Map<String,String> _attrMap = new HashMap<>();
    public DbContext attrSet(String name, String value){
        _attrMap.put(name,value);
        return this;
    }
    public String attr(String name){
        return _attrMap.get(name);
    }


    public DbContext(){

    }

    public DbContext(String schemaName, String url) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url);
        _fieldFormat = "";
    }

    //基于线程池配置（如："proxool."）
    //fieldFormat："`%`"
    public DbContext(String schemaName,String url,String fieldFormat) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url);
        _fieldFormat = fieldFormat;
    }

    //基于手动配置（无线程池）
    public DbContext(String schemaName,String url, String user,String password, String fieldFormat) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url,user,password);
        _fieldFormat = fieldFormat;
    }

    public DbContext(String schemaName, DataSource dataSource) {
        _schemaName = schemaName;
        _dataSource = dataSource;
        _fieldFormat = "";
    }

    public DbContext(String schemaName, DataSource dataSource, String fieldFormat) {
        _schemaName = schemaName;
        _dataSource = dataSource;
        _fieldFormat = fieldFormat;
    }

    private DataSource _dataSource;
    public DbContext dataSource(DataSource dataSource){
        _dataSource = dataSource;
        return this;
    }

    public DataSource dataSource(){
        return _dataSource;
    }

    private String _schemaName;
    public DbContext schemaName(String schemaName){
        _schemaName=schemaName;
        return this;
    }

    private String _fieldFormat;
    public DbContext fieldFormat(String format){
        _fieldFormat = format;
        return this;
    }

    protected String _hint = null;
    public DbContext hint(String hint) {
        _hint = hint;
        return this;
    }

    public String field(String key){
        if(_fieldFormat == null || _fieldFormat.length()==0) {
            return key;
        }
        else {
            return _fieldFormat.replace("%", key);
        }
    }

    /*是否配置了schema*/
    public boolean hasSchema(){return _schemaName!=null;}

    /*获取schema*/
    public String getSchema(){
        return _schemaName;
    }

    /*获取连接*/
    public  Connection getConnection() throws SQLException {
        return _dataSource.getConnection();
    }

    public DbQuery sql(String code, Object... args) {
        return new DbQuery(this).sql(new SQLBuilder().append(code, args));
    }

    public DbQuery sql(Act1<SQLBuilder> sqlBuilder) {
        SQLBuilder sql = new SQLBuilder();
        sqlBuilder.run(sql);
        return new DbQuery(this).sql(sql);
    }

    public DbQuery sql(SQLBuilder sqlBuilder) {
        return new DbQuery(this).sql(sqlBuilder);
    }

    /**获取process执行对象*/
    public DbProcedure call(String process) {
        if(process.indexOf(" ")>0) {
            return new DbQueryProcedure(this).sql(process);
        }
        else {
            return new DbStoredProcedure(this).call(process);
        }
    }


    /**获取一个表对象［用于操作插入也更新］*/
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

    public DbTranQueue tranQueue(Act1Ex<DbTranQueue,SQLException> handler) throws SQLException {
        return new DbTranQueue().execute(handler);
    }

}
