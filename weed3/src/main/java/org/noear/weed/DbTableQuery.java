package org.noear.weed;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by noear on 14/11/12.
 *
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

    public DbTableQuery setMap(Map<String,Object> data) {
        if (_item == null) {
            _item = new DataItem();
        }

        _item.setMap(data);

        return this;
    }

//    public DbTableQuery setMap(Map<String,Object> data, String... cols) {
//        if (cols.length == 0) {
//            throw new RuntimeException("Please enter cols");
//        }
//
//        if (_item == null) {
//            _item = new DataItem();
//        }
//
//        for (String c : cols) {
//            if (data.containsKey(c)) {
//                _item.set(c, data.get(c));
//            }
//        }
//
//        return this;
//    }

    public DbTableQuery setEntity(Object data) throws ReflectiveOperationException{
        if (_item == null) {
            _item = new DataItem();
        }

        _item.fromEntity(data);

        return this;
    }



    /**
     * 执行插入并返回自增值，使用set接口的数据
     * （默认，只会插入不是null的数据）
     * */
    public long insert() throws SQLException {
        if (_item == null) {
            return 0;
        }
        else {
            return insert(_item);
        }
    }

    /**
     * 执行更新并返回影响行数，使用set接口的数据
     * （默认，只会更新不是null的数据）
     * */
    public int update() throws SQLException {
        if (_item == null) {
            return 0;
        }
        else {
            return update(_item);
        }
    }

    /**
     * 使用set接口的数据,根据约束字段自动插入或更新
     * （默认，只会更新不是null的数据）
     * */
    public void updateExt(String constraints)throws SQLException {
        if (_item != null) {
            updateExt(_item, constraints);
        }
    }


//    public <T extends GetHandler> boolean insertList(List<T> valuesList) throws SQLException {
//        return insertList(_item, valuesList);
//    }
}