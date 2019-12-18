package org.noear.weed;

import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.Property;
import org.noear.weed.utils.ClassWrap;
import org.noear.weed.utils.PropertyWrap;
import org.noear.weed.utils.StringUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;


/**
 * Created by noear on 14/11/12.
 *
 * $.       //当前表空间
 * $NOW()   //说明这里是一个sql 函数
 * ?
 * ?...     //说明这里是一个数组或查询结果
 */
public class DbTableQuery extends DbTableQueryBase<DbTableQuery> {
    protected DataItem _item = null; //会排除null数据

    public DbTableQuery(DbContext context) {
        super(context);
    }


    public DbTableQuery table(Class<?> tableClz) {
        return table(getTableName(tableClz));
    }

    /** 添加SQL 内关联语句 */
    public DbTableQuery innerJoin(Class<?> tableClz) {
        return innerJoin(getTableName(tableClz));
    }

    /** 添加SQL 左关联语句 */
    public DbTableQuery leftJoin(Class<?> tableClz) {
        return leftJoin(getTableName(tableClz));
    }

    /** 添加SQL 右关联语句 */
    public DbTableQuery rightJoin(Class<?> tableClz) {
        return rightJoin(getTableName(tableClz));
    }

    public <C,D> DbTableQuery onEq(Property<C,?> property1, Property<D,?> property2) {
        return onEq(getColumnName(property1), getColumnName(property2));
    }

    public <C> DbTableQuery groupBy(Property<C,?> property) {
        return groupBy(getColumnName(property));
    }

    public <C> DbTableQuery orderByAsc(Property<C,?> property) {
        return orderByAsc(getColumnName(property));
    }

    public <C> DbTableQuery orderByDesc(Property<C,?> property) {
        return orderByDesc(getColumnName(property));
    }

    public IQuery select(Serializable... sels) {
        StringBuilder sb = StringUtils.borrowBuilder();
        for (Serializable s : sels) {
            if (s instanceof String) {
                sb.append((String) s).append(",");
            } else if (s instanceof PropertyWrap) {
                PropertyWrap pw = (PropertyWrap) s;
                sb.append(pw.getSelectName(_clzArray)).append(",");
            } else if (s instanceof Class<?>) {
                int idx = addClass(ClassWrap.get((Class<?>) s));
                sb.append("t").append(idx).append(".*").append(",");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return super.select(StringUtils.releaseBuilder(sb));
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

    public <C> DbTableQuery set(Property<C,?> prop, Object value) {
        return set(PropertyWrap.get(prop).name, value);
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
     *
     * 请改用 upsert
     * */
    @Deprecated
    public void updateExt(String conditionFields)throws SQLException {
        if (_item != null) {
            upsert(_item, conditionFields);
        }
    }

    public long upsert(String conditionFields)throws SQLException {
        if (_item != null) {
            return upsert(_item, conditionFields);
        }else{
            return 0;
        }
    }
}