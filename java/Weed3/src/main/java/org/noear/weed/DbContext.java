package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act1Ex;
import org.noear.weed.utils.TextUtil;

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
    /** 最后次执行命令*/
    public Command lastCommand;
    /** 充许多片段执行 */
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


    // 构建函数 start
    public DbContext(){}

    //基于线程池配置（如："proxool."）
    //fieldFormat："`%`"
    public DbContext(String schemaName, String url) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url);
    }

    //基于手动配置（无线程池）
    public DbContext(String schemaName, String url, String user, String password) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url,user,password);
    }

    public DbContext(String schemaName, DataSource dataSource) {
        _schemaName = schemaName;
        _dataSource = dataSource;
    }

    // 构建函数 end

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

    //字段格式符
    private String _fieldFormat;
    private String _fieldFormat_start;
    //对象格式符
    private String _objectFormat;
    private String _objectFormat_start;
    /**
     * 字段格式符
     * */

    public DbContext fieldFormat(String format) {
        _fieldFormat = format;
        if (format != null && format.length() > 1) {
            _fieldFormat_start = format.substring(0, 1);
        }else{
            _fieldFormat_start="";
        }

        return this;
    }

    /**
     * 对象格式符
     * */
    public DbContext objectFormat(String format){
        _objectFormat = format;
        if (format != null && format.length() > 1) {
            _objectFormat_start = format.substring(0, 1);
        }else{
            _objectFormat_start="";
        }
        return this;
    }

    protected String _hint = null;
    /**
     * 代码注解
     * */
    public DbContext hint(String hint) {
        _hint = hint;
        return this;
    }

    public String field(String name){
        if(TextUtil.isEmpty(_fieldFormat)) {
            return name;
        }
        else {
            if(name.startsWith(_fieldFormat_start)){
                return name;
            }else {
                return _fieldFormat.replace("%", name);
            }
        }
    }

    public String object(String name){
        if(TextUtil.isEmpty(_objectFormat)) {
            return name;
        }
        else {
            if(name.startsWith(_objectFormat_start)){
                return name;
            }else {
                return _objectFormat.replace("%", name);
            }
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


    public int exec(String code, Object... args) throws Exception{
        return new DbQuery(this).sql(new SQLBuilder().append(code, args)).execute();
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
