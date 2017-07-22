package noear.weed;

import noear.weed.ext.Act1;
import noear.weed.ext.Fun0;
import noear.weed.ext.Fun1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 14/11/12.
 *
 * $.       //表空间占位数（即数据库名）
 * $fcn     //SQL函数占位符
 * ?        //参数占位符
 * ?...     //数组型参数占位符
 */
public class DbTableQuery extends DbTableQueryBase<DbTableQuery> {
    protected DataItem _item = null; //会排除null数据

    public DbTableQuery(DbContext context) {
        super(context);
    }


    public DbTableQuery set(String name, Object value) {
        if (_item == null) {
            _item = new DataItem();
        }

        _item.set(name, value);

        return this;
    }


    //只会插入不是null的数据
    public long insert() throws SQLException {
        if (_item == null)
            return 0;
        else
            return insert(_item);
    }

    //只会更新不是null的数据
    public int update() throws SQLException {
        if (_item == null)
            return 0;
        else
            return update(_item);
    }



    public <T> void insertList(List<T> valuesList, Fun1<GetHandler, T> hander) throws SQLException {
        if (_item != null) {
            List<GetHandler> list2 = new ArrayList<>();

            for (T item : valuesList) {
                list2.add(hander.run(item));
            }

            insertList(list2);
        }
    }

    public <T extends GetHandler> void insertList(List<T> valuesList) throws SQLException {
        if (_item != null) {
            insertList(_item, valuesList);
        }
    }
}