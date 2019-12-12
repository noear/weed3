package org.noear.weed;

import org.noear.weed.ext.Fun2;

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

    private void item_init(){
        if (_item == null) {
            _item = new DataItem();
        }
    }

    public DbTableQuery set(String name, Object value) {
        item_init();
        _item.set(name, value);

        return this;
    }

    public DbTableQuery setIf(boolean condition, String name, Object value){
        if(condition){
            set(name,value);
        }

        return this;
    }

    public DbTableQuery setMap(Map<String,Object> data) {
        item_init();
        _item.setMap(data);

        return this;
    }

    public DbTableQuery setMapIf(Map<String,Object> data, Fun2<Boolean,String,Object> condition) {
        item_init();
        _item.setMapIf(data, condition);

        return this;
    }

    public DbTableQuery setEntity(Object data) {
        item_init();
        _item.setEntity(data);

        return this;
    }

    public DbTableQuery setEntityIf(Object data, Fun2<Boolean,String,Object> condition) {
        item_init();

        _item.setEntityIf(data, condition);

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