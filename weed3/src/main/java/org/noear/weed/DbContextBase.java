package org.noear.weed;

import org.noear.weed.ext.Get1;
import org.noear.weed.utils.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class DbContextBase<T> {
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

    public T propSet(Map prop){
        return propSet(prop::get);
    }

    public T propSet( Properties prop){
        return propSet(prop::get);
    }

    public T propSet(Get1<?,Object> sets){
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

        return (T)this;
    }

    /** 名字获取 */
    public String name(){
        return _name;
    }

    //
    // 构建函数 end
    //

    /** 特性设置 */
    public T attrSet(String name, String value) {
        _attrMap.put(name, value);
        return (T)this;
    }

    /** 特性获取 */
    public String attr(String name) {
        return _attrMap.get(name);
    }


    /** 设置JDBC驱动 */
    public T driverSet(String driverClassName){
        try{
            Class.forName(driverClassName);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return (T)this;
    }

    /** 数据源设置 */
    public T dataSourceSet(DataSource dataSource) {
        _dataSource = dataSource;
        return (T)this;
    }

    /** 获取数据源 */
    public DataSource dataSource() {
        return _dataSource;
    }


    /** 数据集合名称设置 */
    public T schemaSet(String schema) {
        _schema = schema;
        return (T)this;
    }

    /** 代码注解设置 */
    public T codeHintSet(String hint) {
        _codeHint = hint;
        return (T)this;
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
    public T fieldFormatSet(String format) {
        _formater.fieldFormatSet(format);
        return (T)this;
    }

    /**
     * 对象格式符设置
     */
    public T objectFormatSet(String format) {
        _formater.objectFormatSet(format);
        return (T)this;
    }

    public T formaterSet(IDbFormater formater){
        _formater = formater;
        return (T)this;
    }
    public IDbFormater formater(){
        return _formater;
    }
}
