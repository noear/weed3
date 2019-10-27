package org.noear.weed;

import org.noear.weed.cache.CacheUsing;
import org.noear.weed.cache.ICacheController;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;
import org.noear.weed.utils.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.noear.weed.WeedConfig.isUsingTableSpace;

/**
 * Created by noear on 14/11/12.
 *
 * #        //不加表空间（table(xxx) : 默认加表空间）
 * $.       //当前表空间
 * $NOW()   //说明这里是一个sql 函数
 * ?...     //说明这里是一个数组或查询结果
 */
public class DbTableQueryBase<T extends DbTableQueryBase> implements ICacheController<DbTableQueryBase> {

    String _table;
    DbContext _context;
    SQLBuilder _builder;
    SQLBuilder _builder_bef;
    int _isLog=0;

    protected String formatObject(String name){
        return _context.formater().formatObject(name);
    }

    protected String formatField(String name){
        return _context.formater().formatField(name);
    }

    protected String formatColumns(String columns){
        return _context.formater().formatColumns(columns);
    }

    protected String formatCondition(String condition){
        return _context.formater().formatCondition(condition);
    }


    public DbTableQueryBase(DbContext context) {
        _context = context;
        _builder = new SQLBuilder();
        _builder_bef = new SQLBuilder();
    }


    public T log(boolean isLog) {
        _isLog = isLog ? 1 : -1;
        return (T) this;
    }

    public T expre(Act1<T> action){
        action.run((T)this);
        return (T)this;
    }

    protected T table(String table) { //相当于 from
        if(table.startsWith("#")){
            _table = table.replace("#","");
        }else {
            if (table.indexOf('.') > 0) {
                _table = table;
            }
            else {
                if(isUsingTableSpace){
                    _table = "$." + table;
                }else{
                    _table = formatObject(table); //"$." + table;
                }
            }
        }

        return (T) this;
    }

    public T with(String name, String code, Object... args) {
        if (_builder_bef.length() < 6) {
            _builder_bef.append(" WITH ");
        }else{
            _builder_bef.append("," );
        }

        _builder_bef.append(formatField(name))
                .append(" AS (")
                .append(code, args)
                .append(") ");

        return (T) this;
    }

    //使用 ?... 支持数组参数
    public T where(String where, Object... args) {
        _builder.append(" WHERE ").append(formatCondition(where), args);
        return (T)this;
    }

    public T where() {
        _builder.append(" WHERE ");
        return (T)this;
    }

    public T whereEq(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }
    public T whereLt(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }
    public T whereLte(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }
    public T whereGt(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }
    public T whereGte(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }
    public T whereLk(String filed, String val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }



    public T and(String and, Object... args) {
        _builder.append(" AND ").append(formatCondition(and), args);
        return (T)this;
    }

    public T and() {
        _builder.append(" AND ");
        return (T)this;
    }

    public T andEq(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }
    public T andLt(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }
    public T andLte(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }
    public T andGt(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }
    public T andGte(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }
    public T andLk(String filed, String val){
        _builder.append(" AND ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }

    public T or(String or, Object... args) {
        _builder.append(" OR ").append(formatCondition(or), args);
        return (T)this;
    }

    public T or() {
        _builder.append(" OR ");
        return (T)this;
    }

    public T orEq(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }
    public T orLt(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }
    public T orLte(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }
    public T orGt(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }
    public T orGte(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }
    public T orLk(String filed, String val){
        _builder.append(" OR ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }


    public T begin() {
        _builder.append(" ( ");
        return (T)this;
    }

    public T begin(String and, Object... args) {
        _builder.append(" ( ").append(formatCondition(and), args);
        return (T)this;
    }

    public T end() {
        _builder.append(" ) ");
        return (T)this;
    }

    public T from(String table){
        _builder.append(" FROM ").append(table);
        return (T)this;
    }

    public long insert(IDataItem data) throws SQLException {
        if (data == null || data.count() == 0) {
            return 0;
        }

        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append(" INSERT INTO ").append(_table).append(" (");
        data.forEach((key, value) -> {
            if(value==null) {
                if(_usingNull == false) {
                    return;
                }
            }

            sb.append(formatField(key)).append(",");
        });

        sb.deleteCharAt(sb.length() - 1);

        sb.append(") ");
        sb.append("VALUES");
        sb.append("(");

        data.forEach((key, value) -> {
            if (value == null) {
                if(_usingNull) {
                    sb.append("null,"); //充许插入null
                }
            } else {
                if (value instanceof String) {
                    String val2 = (String) value;
                    if (isSqlExpr(val2)) { //说明是SQL函数
                        sb.append(val2.substring(1)).append(",");
                    } else {
                        sb.append("?,");
                        args.add(value);
                    }
                } else {
                    sb.append("?,");
                    args.add(value);
                }
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");

        _builder.clear();
        _builder.append(StringUtils.releaseBuilder(sb), args.toArray());

        return compile().insert();
    }

    public <T> boolean insertList(Collection<T> valuesList, Act2<T,DataItem> hander) throws SQLException {
        List<DataItem> list2 = new ArrayList<>();

        for (T values : valuesList) {
            DataItem item = new DataItem();
            hander.run(values, item);

            list2.add(item);
        }



        if (list2.size() > 0) {
            return insertList(list2.get(0), list2);
        }else{
            return false;
        }
    }

    public boolean insertList(List<DataItem> valuesList) throws SQLException {
        if (valuesList == null) {
            return false;
        }

        return insertList(valuesList.get(0), valuesList);
    }

    protected <T extends GetHandler> boolean insertList(IDataItem cols, Collection<T> valuesList)throws SQLException {
        if (valuesList == null || valuesList.size() == 0) {
            return false;
        }

        if (cols == null || cols.count() == 0) {
            return false;
        }

        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append(" INSERT INTO ").append(_table).append(" (");
        for (String key : cols.keys()) {
            sb.append(formatField(key)).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(") ");

        sb.append("VALUES");

        //记录当前长度用于后面比较
        int sb_len = sb.length();

        for (GetHandler item : valuesList) {
            sb.append("(");

            for (String key : cols.keys()) {
                Object val = item.get(key);

                if (val == null) {
                    sb.append("null,");
                } else {
                    if (val instanceof String) {
                        String val2 = (String) val;
                        if (isSqlExpr(val2)) { //说明是SQL函数
                            sb.append(val2.substring(1)).append(",");
                        } else {
                            sb.append("?,");
                            args.add(val);
                        }
                    } else {
                        sb.append("?,");
                        args.add(val);
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }

        //如果长度没有增加，说明没有数据
        if(sb_len == sb.length()){
            return false;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(";");

        _builder.append(StringUtils.releaseBuilder(sb), args.toArray());

        return compile().execute() > 0;
    }

    public long insert(Act1<IDataItem> fun) throws SQLException
    {
        DataItem item = new DataItem();
        fun.run(item);

        return insert(item);
    }

    public void updateExt(IDataItem data, String constraints) throws SQLException {
        String[] ff = constraints.split(",");

        if(ff.length==0){
            throw new RuntimeException("Please enter constraints");
        }

        this.where("1=1");
        for (String f : ff) {
            this.and(f + "=?", data.get(f));
        }

        if (this.exists()) {
            for (String f : ff) {
               data.remove(f);
            }

            this.update(data);
        } else {
            this.insert(data);
        }
    }

    public int update(Act1<IDataItem> fun) throws SQLException
    {
        DataItem item = new DataItem();
        fun.run(item);

        return update(item);
    }

    public int update(IDataItem data) throws SQLException{
        if (data == null || data.count() == 0) {
            return 0;
        }

        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append("UPDATE ").append(_table).append(" SET ");

        data.forEach((key,value)->{
            if(value==null) {
                if (_usingNull) {
                    sb.append(formatField(key)).append("=null,");
                }
                return;
            }

            if (value instanceof String) {
                String val2 = (String)value;
                if (isSqlExpr(val2)) {
                    sb.append(formatField(key)).append("=").append(val2.substring(1)).append(",");
                }
                else {
                    sb.append(formatField(key)).append("=?,");
                    args.add(value);
                }
            }
            else {
                sb.append(formatField(key)).append("=?,");
                args.add(value);
            }
        });

        sb.deleteCharAt(sb.length() - 1);

        _builder.backup();
        _builder.insert(StringUtils.releaseBuilder(sb), args.toArray());

        if(WeedConfig.isUpdateMustConditional && _builder.indexOf(" WHERE ")<0){
            throw new RuntimeException("Lack of update condition!!!");
        }

        int rst = compile().execute();

        _builder.restore();

        return rst;
    }

    public int delete() throws SQLException {
        StringBuilder sb  = StringUtils.borrowBuilder();

        sb.append("DELETE ");

        if(_builder.indexOf(" FROM ")<0){
            sb.append(" FROM ").append(_table);
        }else{
            sb.append(_table);
        }

        _builder.insert(StringUtils.releaseBuilder(sb));

        return compile().execute();
    }

    public T innerJoin(String table) {
        _builder.append(" INNER JOIN ").append(formatObject(table));
        return (T)this;
    }

    public T leftJoin(String table) {
        _builder.append(" LEFT JOIN ").append(formatObject(table));
        return (T)this;
    }

    public T rightJoin(String table) {
        _builder.append(" RIGHT JOIN ").append(formatObject(table));
        return (T)this;
    }

    /** 添加无限制代码 */
    public T append(String code,  Object... args){
        _builder.append(code, args);
        return (T)this;
    }

    public T on(String on) {
        _builder.append(" ON ").append(on);
        return (T)this;
    }

    public T groupBy(String groupBy) {
        _builder.append(" GROUP BY ").append(formatColumns(groupBy));
        return (T)this;
    }

    public T having(String having){
        _builder.append(" HAVING ").append(having);
        return (T)this;
    }

    public T orderBy(String orderBy) {
        _builder.append(" ORDER BY ").append(formatColumns(orderBy));
        return (T)this;
    }

    public T limit(int start, int rows) {
        _builder.append(" LIMIT " + start + "," + rows + " ");
        return (T)this;
    }

    public T limit(int rows) {
        _builder.append(" LIMIT " + rows + " ");
        return (T)this;
    }

    private int _top = 0;
    public T top(int num) {
        _top = num;
        //_builder.append(" TOP " + num + " ");
        return (T)this;
    }



    public boolean exists() throws SQLException {

        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append("SELECT 1 FROM ").append(_table);

        _builder.backup();

        _builder.insert(StringUtils.releaseBuilder(sb));
        _builder.append(" LIMIT 1");

        //1.构建sql
        if(_hint!=null) {
            _builder.insert(_hint);
            _hint = null;
        }

        DbQuery rst = compile();

        if(_cache != null){
            rst.cache(_cache);
        }

        _builder.restore();


        return rst.getValue() != null;
    }

    String _hint = null;
    public T hint(String hint){
        _hint = hint;
        return  (T)this;
    }

    public long count() throws SQLException{
        return count("COUNT(*)");
    }

    public long count(String expr) throws SQLException{
        return select(expr).getVariate().longValue(0l);
    }

    public IQuery select(String columns) {

        StringBuilder sb = StringUtils.borrowBuilder();

        //1.构建sql
        if(_hint!=null) {
            sb.append(_hint);
            _hint = null;
        }

        sb.append("SELECT ");

        if(_top>0){
            sb.append(" TOP ").append(_top).append(" ");
        }

        sb.append(formatColumns(columns)).append(" FROM ").append(_table);

        _builder.backup();

        if(_builder_bef.length()>0){
            _builder.insert(_builder_bef);
        }

        _builder.insert(StringUtils.releaseBuilder(sb));

        DbQuery rst = compile();

        if(_cache != null){
            rst.cache(_cache);
        }

        _builder.restore();

        return rst;
    }



    protected DbTran _tran = null;
    public T tran(DbTran transaction)
    {
        _tran = transaction;
        return (T)this;
    }

    public T tran()
    {
        _tran = _context.tran();
        return (T)this;
    }


    //编译（成DbQuery）
    private DbQuery compile() {
        DbQuery temp = new DbQuery(_context).sql(_builder);

        _builder.clear();

        if(_tran!=null) {
            temp.tran(_tran);
        }

        return temp.onCommandBuilt((cmd)->{
            cmd.isLog = _isLog;
            cmd.tag   = _table;
        });
    }

    private boolean _usingNull = WeedConfig.isUsingValueNull;
    public T usingNull(boolean isUsing){
        _usingNull = isUsing;
        return (T)this;
    }

    private boolean _usingExpression = WeedConfig.isUsingValueExpression;
    public T usingExpr(boolean isUsing){
        _usingExpression = isUsing;
        return (T)this;
    }

    private boolean isSqlExpr(String txt) {
        if (_usingExpression == false) {
            return false;
        }

        if (txt.startsWith("$")
                && txt.indexOf(" ") < 0
                && txt.length() < 100) { //不能出现空隔，且100字符以内。否则视为普通字符串值
            return true;
        } else {
            return false;
        }
    }

    //=======================
    //
    // 缓存控制相关
    //

    protected CacheUsing _cache = null;
    /*引用一个缓存服务*/
    public T caching(ICacheService service)
    {
        _cache = new CacheUsing(service);
        return (T)this;
    }
    /*是否使用缓存*/
    public T usingCache (boolean isCache)
    {
        _cache.usingCache(isCache);
        return (T)this;
    }
    /*使用缓存时间（单位：秒）*/
    public T usingCache (int seconds)
    {
        _cache.usingCache(seconds);
        return (T)this;
    }

    /*添加缓存标签*/
    public T cacheTag(String tag)
    {
        _cache.cacheTag(tag);
        return (T)this;
    }
}
