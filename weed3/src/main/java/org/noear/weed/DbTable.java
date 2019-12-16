package org.noear.weed;

import org.noear.weed.ext.Fun0;

import java.sql.SQLException;

/**
 * 下个大版本，将不再支持
 * */
@Deprecated
public class DbTable extends DbTableQueryBase<DbTable> {
    protected DataItemEx _item = new DataItemEx(); //会排除null数据

    public DbTable(DbContext context) {
        super(context);
    }

    protected void set(String name, Fun0<Object> valueGetter) {
        _item.set(name, valueGetter);
    }

    //只会插入不是null的数据
    public long insert() throws SQLException {
        return insert(_item);
    }

    //只会更新不是null的数据
    public int update() throws SQLException {
        return update(_item);
    }

    //只会插入不是null的数据
    public long insert(GetHandler source) throws SQLException {

        return insert(DataItem.create(_item, source));
    }

    //只会更新不是null的数据
    public void update(GetHandler source) throws SQLException {
        update(DataItem.create(_item, source));
    }

//    public <T> void insertList(List<T> valuesList, Fun1<GetHandler, T> hander) throws SQLException {
//        List<GetHandler> list2 = new ArrayList<>();
//
//        for (T item : valuesList) {
//            list2.add(hander.run(item));
//        }
//
//        insertList(list2);
//    }

//    public <T extends GetHandler> boolean insertList(List<T> valuesList) throws SQLException {
//        return insertList(_item, valuesList);
//    }
}
