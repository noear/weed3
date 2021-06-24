package org.noear.weed;

import org.noear.weed.cache.CacheUsing;
import org.noear.weed.cache.ICacheController;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.DbType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created by noear on 14/11/12.
 *
 * $.       //当前表空间
 * $NOW()   //说明这里是一个sql 函数
 * ?
 * ?...     //说明这里是一个数组或查询结果
 */
public class DbTableQueryBase<T extends DbTableQueryBase> extends WhereBase<T> implements ICacheController<DbTableQueryBase> {

    String _table_raw;
    String _table; //表名

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
        if (table.startsWith("#")) {
            _table = table.substring(1);

            _table_raw = _table;
        } else {
            _table_raw = table;

            if (table.indexOf('.') > 0) {
                _table = table;
            } else {
                if (WeedConfig.isUsingSchemaPrefix && _context.schema() != null) {
                    _table = fmtObject(_context.schema() + "." + table);
                } else {
                    _table = fmtObject(table); //"$." + table;
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

        _builder_bef.append(fmtColumn(name))
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

        _builder_bef.append(fmtColumn(name))
                .append(" AS (")
                .append(select)
                .append(") ");

        return (T) this;
    }



    /** 添加 FROM 语句 */
    public T from(String table){
        _builder.append(" FROM ").append(fmtObject(table));
        return (T)this;
    }


    private T join(String style, String table) {
        if (table.startsWith("#")) {
            _builder.append(style).append(table.substring(1));
        } else {
            if (WeedConfig.isUsingSchemaPrefix && _context.schema() != null) {
                _builder.append(style).append(fmtObject(_context.schema() + "." + table));
            } else {
                _builder.append(style).append(fmtObject(table));
            }
        }
        return (T) this;
    }

    /** 添加SQL 内关联语句 */
    public T innerJoin(String table) {
        return join(" INNER JOIN ",table);
    }

    /** 添加SQL 左关联语句 */
    public T leftJoin(String table) {
        return join(" LEFT JOIN ",table);
    }

    /** 添加SQL 右关联语句 */
    public T rightJoin(String table) {
        return join(" RIGHT JOIN ",table);
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
        _builder.append(" ON ").append(fmtColumn(column1)).append("=").append(fmtColumn(column2));
        return (T) this;
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

        _builder.clear();

        _context.dbDialect()
                .insertItem(_context, _table, _builder, this::isSqlExpr, _usingNull, data);

        return compile().insert();
    }

    /** 根据约束进行插入 */
    public long insertBy(IDataItem data, String conditionFields) throws SQLException {
        if (data == null || data.count() == 0) {
            return 0;
        }

        String[] ff = conditionFields.split(",");

        if (ff.length == 0) {
            throw new RuntimeException("Please enter constraints");
        }

        this.where("1=1");

        for (String f : ff) {
            this.andEq(f, data.get(f));
        }

        if (this.exists()) {
            return 0;
        }

        return insert(data);
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

        _context.dbDialect()
                .insertList(_context, _table, _builder, this::isSqlExpr, cols, valuesList);

        return compile().execute() > 0;
    }


    /** 使用data的数据,根据约束字段自动插入或更新
     *
     * 请改用 upsertBy
     * */
    @Deprecated
    public void updateExt(IDataItem data, String conditionFields) throws SQLException {
        upsertBy(data, conditionFields);
    }

    /** 使用data的数据,根据约束字段自动插入或更新
     *
     * 请改用 upsertBy
     * */
    @Deprecated
    public long upsert(IDataItem data, String conditionFields) throws SQLException {
        return upsertBy(data,conditionFields);
    }

    /**
     * 使用data的数据,根据约束字段自动插入或更新
     * */
    public long upsertBy(IDataItem data, String conditionFields) throws SQLException {
        if (data == null || data.count() == 0) {
            return 0;
        }

        String[] ff = conditionFields.split(",");

        if (ff.length == 0) {
            throw new RuntimeException("Please enter constraints");
        }

        this.where("1=1");
        for (String f : ff) {
            this.andEq(f, data.get(f));
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
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE ").append(_table).append(" SET ");

        updateItemsBuild0(data, sb, args);

        _builder.backup();
        _builder.insert(sb.toString(), args.toArray());

        if(WeedConfig.isUpdateMustConditional && _builder.indexOf(" WHERE ")<0){
            throw new RuntimeException("Lack of update condition!!!");
        }

        if(limit_top > 0) {
            if (dbType() == DbType.MySQL || dbType() == DbType.MariaDB) {
                _builder.append(" LIMIT ?", limit_top);
            }
        }

        int rst = compile().execute();

        _builder.restore();

        return rst;
    }

    private void updateItemsBuild0(IDataItem data, StringBuilder buf, List<Object> args) {
        data.forEach((key, value) -> {
            if (value == null) {
                if (_usingNull) {
                    buf.append(fmtColumn(key)).append("=null,");
                }
                return;
            }

            if (value instanceof String) {
                String val2 = (String) value;
                if (isSqlExpr(val2)) {
                    buf.append(fmtColumn(key)).append("=").append(val2.substring(1)).append(",");
                } else {
                    buf.append(fmtColumn(key)).append("=?,");
                    args.add(value);
                }
            } else {
                buf.append(fmtColumn(key)).append("=?,");
                args.add(value);
            }
        });

        buf.deleteCharAt(buf.length() - 1);
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

        if(limit_top > 0) {
            if (dbType() == DbType.MySQL || dbType() == DbType.MariaDB) {
                _builder.append(" LIMIT ?", limit_top);
            }
        }


        if(WeedConfig.isDeleteMustConditional && _builder.indexOf(" WHERE ")<0){
            throw new RuntimeException("Lack of delete condition!!!");
        }

        return compile().execute();
    }




    protected int limit_start, limit_size;

    /** 添加SQL paging语句 */
    public T limit(int start, int size) {
        limit_start = start;
        limit_size = size;
        //_builder.append(" LIMIT " + start + "," + rows + " ");
        return (T)this;
    }

    protected int limit_top = 0;
    /** 添加SQL top语句 */
    public T limit(int size) {
        limit_top = size;
        //_builder.append(" LIMIT " + rows + " ");
        return (T)this;
    }


    /** 添加SQL paging语句 */
    public T paging(int start, int size) {
        limit_start = start;
        limit_size = size;
        //_builder.append(" LIMIT " + start + "," + rows + " ");
        return (T)this;
    }

    /** 添加SQL top语句 */
    public T top(int size) {
        limit_top = size;
        //_builder.append(" LIMIT " + rows + " ");
        return (T)this;
    }




    @Deprecated
    public boolean exists() throws SQLException {
        return selectExists();
    }

    String _hint = null;
    public T hint(String hint){
        _hint = hint;
        return  (T)this;
    }

    @Deprecated
    public long count() throws SQLException{
        return selectCount();
        //return count("COUNT(*)");
    }

    @Deprecated
    public long count(String code) throws SQLException{
        return selectCount(code);
        //return selectDo(code).getVariate().longValue(0l);
    }

    @Deprecated
    public IQuery select(String columns) {
        return selectDo(columns);
    }

    protected IQuery selectDo(String columns) {
        select_do(columns, true);

        DbQuery rst = compile();

        if(_cache != null){
            rst.cache(_cache);
        }

        _builder.restore();

        return rst;
    }

    public boolean selectExists() throws SQLException {
        int bak = limit_top;
        limit(1);
        select_do(" 1 ", false);
        limit(bak);

        DbQuery rst = compile();

        if (_cache != null) {
            rst.cache(_cache);
        }

        _builder.restore();

        return rst.getValue() != null;
    }

    public long selectCount() throws SQLException{
        return selectCount("COUNT(*)");
    }
    public long selectCount(String column) throws SQLException{
        return selectDo(column).getVariate().longValue(0L);
    }

    public Object selectValue(String column) throws SQLException {
        return selectDo(column).getValue();
    }

    public <T> T selectValue(String column, T def) throws SQLException {
        return selectDo(column).getValue(def);
    }

    public <T> T selectItem(String columns, Class<T> clz) throws SQLException {
        return selectDo(columns).getItem(clz);
    }

    public <T> List<T> selectList(String columns, Class<T> clz) throws SQLException {
        return selectDo(columns).getList(clz);
    }

    public DataItem selectDataItem(String columns) throws SQLException {
        return selectDo(columns).getDataItem();
    }

    public DataList selectDataList(String columns) throws SQLException {
        return selectDo(columns).getDataList();
    }

    public Map<String,Object> selectMap(String columns) throws SQLException{
        return selectDo(columns).getMap();
    }

    public List<Map<String, Object>> selectMapList(String columns) throws SQLException{
        return selectDo(columns).getMapList();
    }

    public <T> List<T> selectArray(String column) throws SQLException{
        return selectDo(column).getArray();
    }


    public SelectQ selectQ(String columns) {
        select_do(columns, true);

        return new SelectQ(_builder);
    }

    private void select_do(String columns, boolean doFormat) {
        _builder.backup();

        //1.构建 xxx... FROM table
        StringBuilder sb = new StringBuilder(_builder.builder.length() + 100);
        sb.append(" ");//不能去掉
        if (doFormat) {
            sb.append(fmtMutColumns(columns)).append(" FROM ").append(_table);
        } else {
            sb.append(columns).append(" FROM ").append(_table);
        }
        sb.append(_builder.builder);

        _builder.builder = sb;

        //2.尝试构建分页
        if (limit_top > 0) {
            _context.dbDialect().selectTop(_context, _table_raw, _builder, _orderBy, limit_top);
        } else if (limit_size > 0) {
            _context.dbDialect().selectPage(_context, _table_raw, _builder, _orderBy, limit_start, limit_size);
        } else {
            _builder.insert(0, "SELECT ");
            if (_orderBy != null) {
                _builder.append(_orderBy);
            }
        }

        //3.构建hint
        if (_hint != null) {
            sb.append(_hint);
            _builder.insert(0, _hint);
        }

        //4.构建whith
        if (_builder_bef.length() > 0) {
            _builder.insert(_builder_bef);
        }
    }

    //编译（成DbQuery）
    private DbQuery compile() {
        DbQuery temp = new DbQuery(_context).sql(_builder);

        _builder.clear();

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
