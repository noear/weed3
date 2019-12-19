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
 * $.       //当前表空间
 * $NOW()   //说明这里是一个sql 函数
 * ?
 * ?...     //说明这里是一个数组或查询结果
 */
public class DbTableQueryBase<T extends DbTableQueryBase> extends WhereBase<T> implements ICacheController<DbTableQueryBase> {

    String _table;
    SQLBuilder _builder_bef;
    int _isLog=0;

    public DbTableQueryBase(DbContext context) {
        super(context);
        _builder_bef = new SQLBuilder();
    }


    /** 标记是否记录日志 */
    public T log(boolean isLog) {
        _isLog = isLog ? 1 : -1;
        return (T) this;
    }

    /**
     * 通过表达式构建自己（请使用build替代）
     * */
    @Deprecated
    public T expre(Act1<T> action){
        action.run((T)this);
        return (T)this;
    }

    /** 通过表达式构建自己 */
    public T build(Act1<T> builder){
        builder.run((T)this);
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

    /** 添加SQL with 语句（要确保数据库支持）
     *
     * 例：db.table("user u")
     *       .with("a","select type num from group by type")
     *       .where("u.type in(select a.type) and u.type2 in (select a.type)")
     *       .select("u.*")
     *       .getMapList();
     * */
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

    public T with(String name, SelectQ select) {
        if (_builder_bef.length() < 6) {
            _builder_bef.append(" WITH ");
        }else{
            _builder_bef.append("," );
        }

        _builder_bef.append(formatField(name))
                .append(" AS (")
                .append(select)
                .append(") ");

        return (T) this;
    }



    /** 添加 FROM 语句 */
    public T from(String table){
        _builder.append(" FROM ").append(table);
        return (T)this;
    }


    /** 执行插入并返回自增值，使用dataBuilder构建的数据 */
    public long insert(Act1<IDataItem> dataBuilder) throws SQLException
    {
        DataItem item = new DataItem();
        dataBuilder.run(item);

        return insert(item);
    }

    /** 执行插入并返回自增值，使用data数据 */
    public long insert(IDataItem data) throws SQLException {
        if (data == null || data.count() == 0) {
            return 0;
        }

        if(_table.indexOf(" ")>0) {
            _table = _table.split(" ")[0];
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

    /** 执行批量合并插入，使用集合数据 */
    public boolean insertList(List<DataItem> valuesList) throws SQLException {
        if (valuesList == null) {
            return false;
        }

        return insertList(valuesList.get(0), valuesList);
    }

    /** 执行批量合并插入，使用集合数据（由dataBuilder构建数据） */
    public <T> boolean insertList(Collection<T> valuesList, Act2<T,DataItem> dataBuilder) throws SQLException {
        List<DataItem> list2 = new ArrayList<>();

        for (T values : valuesList) {
            DataItem item = new DataItem();
            dataBuilder.run(values, item);

            list2.add(item);
        }



        if (list2.size() > 0) {
            return insertList(list2.get(0), list2);
        }else{
            return false;
        }
    }


    protected <T extends GetHandler> boolean insertList(IDataItem cols, Collection<T> valuesList)throws SQLException {
        if (valuesList == null || valuesList.size() == 0) {
            return false;
        }

        if (cols == null || cols.count() == 0) {
            return false;
        }

        if(_table.indexOf(" ")>0) {
            _table = _table.split(" ")[0];
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


    /** 使用data的数据,根据约束字段自动插入或更新
     *
     * 请改用 upsert
     * */
    @Deprecated
    public void updateExt(IDataItem data, String conditionFields) throws SQLException {
        upsert(data, conditionFields);
    }

    /**
     * 使用data的数据,根据约束字段自动插入或更新
     * */
    public long upsert(IDataItem data, String conditionFields) throws SQLException {
        String[] ff = conditionFields.split(",");

        if (ff.length == 0) {
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

            return this.update(data);
        } else {
            return this.insert(data);
        }
    }

    /** 执行更新并返回影响行数，使用dataBuilder构建的数据 */
    public int update(Act1<IDataItem> dataBuilder) throws SQLException
    {
        DataItem item = new DataItem();
        dataBuilder.run(item);

        return update(item);
    }

    /** 执行更新并返回影响行数，使用set接口的数据 */
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

    /** 执行删除，并返回影响行数 */
    public int delete() throws SQLException {
        StringBuilder sb  = StringUtils.borrowBuilder();

        sb.append("DELETE ");

        if(_builder.indexOf(" FROM ")<0){
            sb.append(" FROM ").append(_table);
        }else{
            sb.append(_table);
        }

        _builder.insert(StringUtils.releaseBuilder(sb));

        if(WeedConfig.isDeleteMustConditional && _builder.indexOf(" WHERE ")<0){
            throw new RuntimeException("Lack of delete condition!!!");
        }

        return compile().execute();
    }

    /** 添加SQL 内关联语句 */
    public T innerJoin(String table) {
        _builder.append(" INNER JOIN ").append(formatObject(table));
        return (T)this;
    }

    /** 添加SQL 左关联语句 */
    public T leftJoin(String table) {
        _builder.append(" LEFT JOIN ").append(formatObject(table));
        return (T)this;
    }

    /** 添加SQL 右关联语句 */
    public T rightJoin(String table) {
        _builder.append(" RIGHT JOIN ").append(formatObject(table));
        return (T)this;
    }

    /** 添加无限制代码 */
    public T append(String code,  Object... args){
        _builder.append(code, args);
        return (T)this;
    }

    public T on(String code) {
        _builder.append(" ON ").append(code);
        return (T)this;
    }

    public T onEq(String column1, String column2) {
        _builder.append(" ON ").append(column2).append("=").append(column2);
        return (T) this;
    }



    protected int limit_start, limit_rows;

    /** 添加SQL limit语句 */
    public T limit(int start, int rows) {
        limit_start = start;
        limit_rows = rows;
        //_builder.append(" LIMIT " + start + "," + rows + " ");
        return (T)this;
    }

    protected int limit_top = 0;
    /** 添加SQL limit语句 */
    public T limit(int rows) {
        limit_top = rows;
        //_builder.append(" LIMIT " + rows + " ");
        return (T)this;
    }

    public T top(int rows) {
        limit_top = rows;
        //_builder.append(" LIMIT " + rows + " ");
        return (T)this;
    }




    public boolean exists() throws SQLException {

        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append("SELECT 1 FROM ").append(_table);

        _builder.backup();

        _builder.insert(StringUtils.releaseBuilder(sb));

        switch (_context.databaseType()){
            case SQLServer:
                _builder.append(" TOP 1");
                break;

            default:
                _builder.append(" LIMIT 1");
                break;
        }


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

    public long count(String code) throws SQLException{
        return select(code).getVariate().longValue(0l);
    }

    public IQuery select(String columns) {
        select_do(columns);

        DbQuery rst = compile();

        if(_cache != null){
            rst.cache(_cache);
        }

        _builder.restore();

        return rst;
    }

    public SelectQ selectQ(String columns) {

        select_do(columns);

        return new SelectQ(_builder);
    }

    private void select_do(String columns) {
        StringBuilder sb = StringUtils.borrowBuilder();

        //1.构建sql
        if (_hint != null) {
            sb.append(_hint);
            _hint = null;
        }

        sb.append("SELECT ");

        DbPaging.def.preProcessing(this, sb);

        sb.append(formatColumns(columns)).append(" FROM ").append(_table);

        _builder.backup();

        _builder.insert(StringUtils.releaseBuilder(sb));


        _builder = DbPaging.def.postProcessing(this,_builder);

        if (_builder_bef.length() > 0) {
            _builder.insert(_builder_bef);
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
    /** 充许使用null插入或更新 */
    public T usingNull(boolean isUsing){
        _usingNull = isUsing;
        return (T)this;
    }

    private boolean _usingExpression = WeedConfig.isUsingValueExpression;
    /** 充许使用$表达式构建sql */
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
    /** 使用一个缓存服务 */
    public T caching(ICacheService service)
    {
        _cache = new CacheUsing(service);
        return (T)this;
    }
    /** 是否使用缓存 */
    public T usingCache (boolean isCache)
    {
        _cache.usingCache(isCache);
        return (T)this;
    }
    /** 使用缓存时间（单位：秒）*/
    public T usingCache (int seconds)
    {
        _cache.usingCache(seconds);
        return (T)this;
    }

    /** 为缓存添加标签 */
    public T cacheTag(String tag)
    {
        _cache.cacheTag(tag);
        return (T)this;
    }
}
