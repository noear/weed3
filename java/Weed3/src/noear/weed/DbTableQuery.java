package noear.weed;

import java.sql.SQLException;

/**
 * Created by noear on 14/11/12.
 *
 * $.       //表空间占位数（即数据库名）
 * $fcn     //SQL函数占位符
 * ?        //参数占位符
 * ?...     //数组型参数占位符
 */
public class DbTableQuery extends DbTableQueryBase<DbTableQuery> {
    public DbTableQuery(DbContext context) {
        super(context);
    }

    DataItem _values_set;
    public DbTableQuery set(String name, Object value){
        if(_values_set==null){
            _values_set = new DataItem();
        }

        _values_set.set(name,value);

        return this;
    }

    public int update() throws SQLException
    {
        if(_values_set==null)
            return 0;
        else
            return update(_values_set);
    }


    public long insert() throws SQLException
    {
        if(_values_set==null)
            return 0;
        else
            return insert(_values_set);
    }
}