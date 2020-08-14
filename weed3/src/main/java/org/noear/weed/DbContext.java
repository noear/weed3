package org.noear.weed;

import org.noear.weed.ext.*;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.DbFormater;
import org.noear.weed.xml.XmlSqlLoader;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by noear on 14-6-12.
 * 数据库上下文
 */
public class DbContext extends DbContextMetaData {

    /**
     * 最后次执行命令 (线程不安全，仅供调试用)
     */
    @Deprecated
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
    protected DbFormater _formater = new DbFormater(this);

    //特性支持
    protected Map<String, String> _attrMap = new HashMap<>();

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
            dataSourceSet(new DbDataSource(url));
        }else{
            dataSourceSet(new DbDataSource(url, username, password));
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
    public DbContext dataSourceSet(DataSource ds) {
        dataSourceDoSet(ds);
        return this;
    }



    /** 数据集合名称设置 */
    public DbContext schemaSet(String schema) {
        _schema = schema;
        if(_name == null){
            _name = schema;
        }

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
    public DbContext formaterSet(DbFormater formater){
        _formater = formater;
        return this;
    }
    public DbFormater formater(){
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
    public DbContext(String schema, String url) {
        schemaSet(schema);
        dataSourceSet(new DbDataSource(url));
    }

    //基于手动配置（无线程池）
    public DbContext(String schema, String url, String username, String password) {
        schemaSet(schema);
        dataSourceSet(new DbDataSource(url, username, password));
    }

    public DbContext(String schema, DataSource dataSource) {
        schemaSet(schema);
        dataSourceSet(dataSource);
    }

    //
    // 执行能力支持
    //

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

    /**
     * 输入process name，获取process执行对象
     *
     * @param process process name,process code,xsqlid
     */
    public DbProcedure call(String process) {
        if (process.startsWith("@")) {
            XmlSqlLoader.tryLoad();
            return new DbXmlsqlProcedure(this).sql(process.substring(1));
        }

        if (process.lastIndexOf(" ") > 0) {
            return new DbQueryProcedure(this).sql(process);
        }

        return new DbStoredProcedure(this).call(process);
    }

    public DbProcedure call(String process, Map<String,Object> args){
        if (process.startsWith("@")) {
            XmlSqlLoader.tryLoad();
            return new DbXmlsqlProcedure(this).sql(process.substring(1));
        }


        if(process.startsWith("#")){
            try {
                String _sql = SQLRenderManager.global().render(process.substring(1), args);
                return new DbQueryProcedure(this).sql(_sql).setMap(args);
            }catch (Throwable ex){
                throw new RuntimeException(ex);
            }
        }

        if (process.lastIndexOf(" ") > 0) {
            return new DbQueryProcedure(this).sql(process).setMap(args);
        }

        return new DbStoredProcedure(this).call(process).setMap(args);
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
     * 执行代码，按需返回
     */
    public Object exe(String code, Object... args) throws Exception {
        String cmd = "val";
        String[] ss = code.split("::");
        if(ss.length>1){
            cmd = ss[0];
            code = ss[1];
        }

        String codeUp = code.trim().substring(0,10).toUpperCase();
        if(codeUp.startsWith("SELECT ")){
            switch (cmd){
                case "obj":
                case "map":
                    return sql(code, args).getMap();

                case "ary":
                case "list":
                    return sql(code, args).getMapList();

                default:
                    return sql(code, args).getValue();
            }
        }else{
            return sql(code, args).execute();
        }
    }

    /**
     * 执行代码，返回影响行数
     * */
//    @Deprecated
//    public int exec(String code, Object... args) throws Exception {
//        return sql(code, args).execute();
//    }



    public DbTran tran(Act1Ex<DbTran, Throwable> handler) throws Throwable {
        return tran().execute(handler);
    }


    public DbTran tran(DbTranQueue queue, Act1Ex<DbTran, Throwable> handler) throws Throwable {
        return tran().join(queue).execute(handler);
    }

    public DbTran tran() {
        return new DbTran(this);
    }

    /**
     * 由Db发起，语义不合理；改用 new DbTranQueue()
     * */
    @Deprecated
    public DbTranQueue tranQueue(Act1Ex<DbTranQueue, Throwable> handler) throws Throwable {
        return new DbTranQueue().execute(handler);
    }
}
