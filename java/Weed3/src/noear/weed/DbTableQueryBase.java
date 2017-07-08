package noear.weed;

import noear.weed.ext.Act1;
import noear.weed.ext.Fun1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 14/11/12.
 *
 * $.       //当前表空间
 * $NOW()   //说明这里是一个sql 函数
 * ?...     //说明这里是一个数组或查询结果
 */
public class DbTableQueryBase<T extends DbTableQueryBase>  {

    String _table;
    DbContext _context;
    SQLBuilder _builder;

    public DbTableQueryBase(DbContext context) {
        _context = context;
        _builder = new SQLBuilder();
    }

    public T expre(Act1<T> action){
        action.run((T)this);
        return (T)this;
    }

    protected T table(String table) { //相当于 from
        if (table.indexOf('.') > 0)
            _table = table;
        else
            _table = "$." + table;

        return (T)this;
    }

    //使用 ?... 支持数组参数
    public T where(String where, Object... args) {
        _builder.append(" WHERE ").append(where, args);
        return (T)this;
    }

    public T and(String and, Object... args) {
        _builder.append(" AND ").append(and, args);
        return (T)this;
    }

    public T or(String or, Object... args) {
        _builder.append(" OR ").append(or, args);
        return (T)this;
    }

    public T begin() {
        _builder.append(" ( ");
        return (T)this;
    }

    public T end() {
        _builder.append(" ) ");
        return (T)this;
    }

    public long insert(Fun1<IDataItem,IDataItem> fun) throws SQLException
    {
        DataItem item = new DataItem();

        return insert(fun.run(item));
    }


    public long insert(IDataItem data) throws SQLException{
        if (data == null || data.count() == 0)
            return 0;

        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        sb.append(" INSERT INTO ").append(_table).append(" (");
        data.forEach((key,value)->{
            if(value==null)
                return;

            sb.append(_context.field(key)).append(",");
        });

        sb.deleteCharAt(sb.length() - 1);

        sb.append(") VALUES (");
        data.forEach((key,value)->{
            if(value==null)
                return;

            if (value instanceof String) {
                String val2 = (String)value;
                if (val2.indexOf('$') == 0) { //说明是SQL函数
                    sb.append(val2.substring(1)).append(",");
                }
                else {
                    sb.append("?,");
                    args.add(value);
                }
            }
            else {
                sb.append("?,");
                args.add(value);
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");

        _builder.append(sb.toString(), args.toArray());

        return compile().insert();
    }

    public void insertAll(List<IDataItem> rowsValue) throws SQLException{
        for (IDataItem row : rowsValue) {
            insert(row);
        }
    }

    public int update(Fun1<IDataItem,IDataItem> fun) throws SQLException
    {
        DataItem item = new DataItem();

        return update(fun.run(item));
    }

    public int update(IDataItem data) throws SQLException{
        if (data == null || data.count() == 0)
            return 0;

        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE ").append(_table).append(" SET ");

        data.forEach((key,value)->{
            if(value==null)
                return;

            if (value instanceof String) {
                String val2 = (String)value;
                if (val2.indexOf('$') == 0) {
                    sb.append(_context.field(key)).append("=").append(val2.substring(1)).append(",");
                }
                else {
                    sb.append(_context.field(key)).append("=?,");
                    args.add(value);
                }
            }
            else {
                sb.append(_context.field(key)).append("=?,");
                args.add(value);
            }
        });

        sb.deleteCharAt(sb.length() - 1);

        _builder.insert(sb.toString(), args.toArray());

        return compile().execute();
    }

    public int delete() throws SQLException{
        _builder.insert("DELETE FROM " + _table);
        return compile().execute();
    }

    public T innerJoin(String table) {
        _builder.append(" INNER JOIN ").append(table);
        return (T)this;
    }

    public T leftJoin(String table) {
        _builder.append(" LEFT JOIN ").append(table);
        return (T)this;
    }

    public T on(String on) {
        _builder.append(" ON ").append(on);
        return (T)this;
    }

    public T groupBy(String groupBy) {
        _builder.append(" GROUP BY ").append(groupBy);
        return (T)this;
    }

    public T orderBy(String orderBy) {
        _builder.append(" ORDER BY ").append(orderBy);
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

    public T top(int num) {
        _builder.append(" TOP " + num + " ");
        return (T)this;
    }

    public boolean exists() throws SQLException {

        limit(1);

        return select("1").getValue() != null;
    }


    public IQuery select(String columns) {

        StringBuilder sb = new StringBuilder();

        //1.构建sql
        sb.append("SELECT ").append(columns).append(" FROM ").append(_table);

        _builder.backup();
        _builder.insert(sb.toString());

        IQuery rst = compile();

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

        if(_tran!=null)
            temp.tran(_tran);

        return temp;
    }
}
