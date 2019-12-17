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
public class DbContext {

    /**
     * 最后次执行命令
     */
    public Command lastCommand;
    /**
     * 充许多片段执行
     */
    public boolean allowMultiQueries;
    /**
     * 编译模式（用于产生代码）
     */
    public boolean isCompilationMode = false;


    //数据集名称
    protected String _schema;
    protected IDbFormater _formater = new DbFormater();
    //特性支持
    protected Map<String, String> _attrMap = new HashMap<>();
    //数据源
    protected DataSource _dataSource;
    //代码注解
    protected String _codeHint = null;
    protected String _name;

    private String get_do(Get1<?,Object> sets, String name){
        Object tmp = sets.get(name);
        if(tmp!=null){
            return tmp.toString();
        }else{
            return null;
        }
    }

    public DbContext propSet(Map prop){
        return propSet(prop::get);
    }

    public DbContext propSet( Properties prop){
        return propSet(prop::get);
    }

    public DbContext propSet(Get1<?,Object> sets){
        String schema = get_do(sets,"schema");
        String url = get_do(sets,"url");
        String username = get_do(sets,"username");
        String password = get_do(sets,"password");
        String driverClassName = get_do(sets,"driverClassName");

        if(StringUtils.isEmpty(url) || url.startsWith("jdbc:")==false){
            throw new RuntimeException("url 配置有问题!");
        }

        if(StringUtils.isEmpty(driverClassName) == false){
            driverSet(driverClassName);
        }

        if(StringUtils.isEmpty(_schema)) {
            _schema = schema;
        }

        if(StringUtils.isEmpty(_schema)) {
            _schema = URI.create(url.substring(5)).getPath().substring(1);
        }

        if(StringUtils.isEmpty(username)){
            _dataSource = new DbDataSource(url);
        }else{
            _dataSource = new DbDataSource(url, username, password);
        }

        return this;
    }

    /** 名字获取 */
    public String name(){
        return _name;
    }
    public DbContext nameSet(String name) {
        _name = name;
        WeedConfig.libOfDb.put(name, this);
        return this;
    }

    //
    // 构建函数 end
    //

    /** 特性设置 */
    public DbContext attrSet(String name, String value) {
        _attrMap.put(name, value);
        return this;
    }

    /** 特性获取 */
    public String attr(String name) {
        return _attrMap.get(name);
    }


    /** 设置JDBC驱动 */
    public DbContext driverSet(String driverClassName){
        try{
            Class.forName(driverClassName);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    /** 数据源设置 */
    public DbContext dataSourceSet(DataSource dataSource) {
        _dataSource = dataSource;
        return this;
    }

    /** 获取数据源 */
    public DataSource dataSource() {
        return _dataSource;
    }


    /** 数据集合名称设置 */
    public DbContext schemaSet(String schema) {
        _schema = schema;
        return this;
    }

    /** 代码注解设置 */
    public DbContext codeHintSet(String hint) {
        _codeHint = hint;
        return this;
    }

    /** 代码注解获取 */
    public String codeHint() {
        return _codeHint;
    }


    /** 是否配置了schema */
    public boolean schemaHas() {
        return _schema != null;
    }

    /** 获取schema */
    public String schema() {
        return _schema;
    }

    //
    // 格式化处理
    //

    /**
     * 字段格式符设置
     */
    public DbContext fieldFormatSet(String format) {
        _formater.fieldFormatSet(format);
        return this;
    }

    /**
     * 对象格式符设置
     */
    public DbContext objectFormatSet(String format) {
        _formater.objectFormatSet(format);
        return this;
    }

    public DbContext formaterSet(IDbFormater formater){
        _formater = formater;
        return this;
    }
    public IDbFormater formater(){
        return _formater;
    }






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


    public <T> BaseMapper<T> mapperBase(Class<T> clz) {
        return new BaseMapperWrap<T>(this, clz);
    }

    /**
     * 印映一个接口代理
     * */
    public <T> T mapper(Class<T> clz) {
        return MapperUtil.proxy(clz, this);
    }

    /**
     * 印映一份数据
     *
     * @param xsqlid @{namespace}.{id}
     * */
    public <T> T mapper(String xsqlid, Map<String,Object> args) throws Exception {
        return (T) MapperUtil.exec(this, xsqlid, args, null, null);
    }


    /**
     * 获取一个表对象［用于操作插入也更新］
     */
    public DbTableQuery table(String table) {
        return new DbTableQuery(this).table(table);
    }

    public DbTableQuery table(Class<?> tableClz) {
        return new DbTableQuery(this).table(tableClz);
    }


    /**
     * 输入process name，获取process执行对象
     *
     * @param process process name,process code,xsqlid
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
     * 执行代码，返回影响行数
     */
    public int exe(String code, Object... args) throws Exception {
        return sql(code, args).execute();
    }

    /**
     * 请改用 exe()
     * */
    @Deprecated
    public int exec(String code, Object... args) throws Exception {
        return exe(code, args);
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
