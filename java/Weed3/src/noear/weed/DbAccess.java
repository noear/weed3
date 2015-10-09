package noear.weed;

import noear.weed.cache.CacheUsing;
import noear.weed.cache.ICacheService;
import noear.weed.ext.*;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 14-9-5.
 * 数据库方问基类
 */
public abstract class DbAccess<T extends DbAccess> implements IWeedKey,IQuery,Serializable {
    /*查询语句*/
    public String commandText = null;

    /*数据库上下文*/
    public DbContext context;
    /*访问参数*/
    public List<Variate> paramS = new ArrayList<Variate>();
    /*获取执行命令（由子类实现）*/
    protected abstract Command getCommand();
    /*获取访问标识（由子类实现）*/
    protected abstract String getCommandID();

    public DbAccess(DbContext context){
        this.context = context;
    }

    /*IWeedKey begin*/
    protected String _weedKey;
    public String getWeedKey()
    {
        if(_weedKey==null)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(getCommandID()).append(":");

            for(Variate p:paramS) {
                sb.append("_").append(p.getValue());
            }

            _weedKey=sb.toString();
        }
        return _weedKey;
    }
     /*IWeedKey end*/

    /*获取参数*/
    protected Variate doGet(String paramName) {
        int hash = paramName.hashCode();
        for (Variate p1 : paramS) {
            if (hash == p1._hash)
                return p1;
        }

        return null;
    }

    protected Variate doGet(int index) {
        return paramS.get(index);
    }

    /*设置参数值*/
    protected  void doSet(String param, Object value) {
        paramS.add(new Variate(param, value));
    }

    protected  void doSet(String param, Fun0<Object> valueGetter) {
        paramS.add(new VariateEx(param, valueGetter));
    }

    protected  void doSet(String param, Fun0<Object> valueGetter, Act1<Object> valueSetter) {
        paramS.add(new VariateEx(param, valueGetter, valueSetter));
    }
    //=======================
    //
    // 执行相关代码
    //


    /*执行命令（返回受影响数）*/
    public int execute() throws SQLException
    {
        return new SQLer().execute(getCommand(),_tran);
    }

    public long getCount() throws SQLException
    {
        return getValue(0l);
    }

    public Object getValue() throws SQLException {
        return new SQLer().getVariate(getCommand(), _tran).getValue();
    }

    /*执行命令（返回符合条件的第一个值）*/
    public <T> T getValue(T def) throws SQLException
    {
        return getValue(def,null);
    }

    /*执行命令（返回符合条件的第一个值）*/
    public <T> T getValue(T def,Act2<CacheUsing,T> cacheCondition) throws SQLException {
        if (_cache == null)
            return new SQLer().getVariate(getCommand(), _tran).value(def);
        else {
            _cache.usingCache(cacheCondition);
            return _cache.getEx(this.getWeedKey(), () -> (new SQLer().getVariate(getCommand(), _tran).value(def)));
        }
    }



    /*执行命令（返回一个模理）*/
    public <T extends IBinder> T getItem(T model) throws SQLException {
        return getItem(model,null);
    }

    /*执行命令（返回一个模理）*/
    public <T extends IBinder> T getItem(T model,Act2<CacheUsing,T> cacheCondition) throws SQLException {
        if (_cache == null)
            return new SQLer().getItem(getCommand(), _tran, model);
        else {
            _cache.usingCache(cacheCondition);
            return _cache.getEx(this.getWeedKey(), () -> (new SQLer().getItem(getCommand(), _tran, model)));
        }
    }
    /*执行命令（返回一个列表）*/
    public <T extends IBinder> List<T> getList(T model) throws SQLException{
        return getList(model,null);
    }

    /*执行命令（返回一个列表）*/
    public <T extends IBinder> List<T> getList(T model,Act2<CacheUsing,List<T>> cacheCondition) throws SQLException {

        if (_cache == null)
            return new SQLer().getList(getCommand(), _tran, model);
        else
        {
            _cache.usingCache(cacheCondition);
            return _cache.getEx(this.getWeedKey(), () -> (new SQLer().getList(getCommand(), _tran, model)));
        }
    }


    public DataList getTable() throws SQLException
    {
        return getTable(null);
    }

    public DataList getTable(Act2<CacheUsing,DataList> cacheCondition) throws SQLException
    {
        if (_cache == null)
            return new SQLer().getTable(getCommand(), _tran);
        else {
            _cache.usingCache(cacheCondition);
            return _cache.getEx(this.getWeedKey(), () -> (new SQLer().getTable(getCommand(), _tran)));
        }
    }

    public DataItem getRow() throws SQLException
    {
        return getRow(null);
    }

    public DataItem getRow(Act2<CacheUsing,DataList> cacheCondition) throws SQLException
    {
        if (_cache == null)
            return new SQLer().getRow(getCommand(), _tran);
        else {
            _cache.usingCache(cacheCondition);
            return _cache.getEx(this.getWeedKey(), () -> (new SQLer().getRow(getCommand(), _tran)));
        }
    }

    protected DbTran _tran = null;
    public T tran(DbTran transaction)
    {
        _tran = transaction;
        return (T)this;
    }

    public T tran()
    {
        _tran = context.tran();
        return (T)this;
    }

    //=======================
    //
    // 缓存控制相关
    //

    protected CacheUsing _cache = null;
    /*引用一个缓存服务*/
    public DbAccess caching(ICacheService service)
    {
        _cache = new CacheUsing(service);
        return this;
    }
    /*是否使用缓存*/
    public DbAccess usingCache (boolean isCache)
    {
        _cache.usingCache(isCache);
        return this;
    }
    /*使用缓存时间（单位：秒）*/
    public DbAccess usingCache (int seconds)
    {
        _cache.usingCache(seconds);
        return this;
    }

//    public DbAccess usingCache(Act2<CacheUsing,Object> condition)
//    {
//        _cache.usingCache(condition);
//        return this;
//    }



    /*添加缓存标签*/
    public DbAccess cacheTag(String tag)
    {
        _cache.cacheTag(tag);
        return this;
    }
}
