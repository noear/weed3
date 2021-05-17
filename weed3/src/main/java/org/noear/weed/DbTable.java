package org.noear.weed;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Deprecated
public class DbTable extends DbTableQueryBase<DbTable> {
    protected DataItem _cols = new DataItem(); //会排除null数据
    protected Map<String, Supplier<Object>> _prop = new LinkedHashMap<>();

    public DbTable(DbContext context) {
        super(context);
    }

    protected void set(String name, Supplier<Object> valueGetter) {
        _cols.set(name, null);
        _prop.put(name, valueGetter);
    }

    //只会插入不是null的数据
    public long insert() throws SQLException {
        DataItem item = new DataItem();
        _prop.forEach((k, v) -> {
            if (v.get() != null) {
                item.set(k, v.get());
            }
        });

        return insert(item);
    }

    //只会更新不是null的数据
    public int update() throws SQLException {
        DataItem item = new DataItem();
        _prop.forEach((k, v) -> {
            if (v.get() != null) {
                item.set(k, v.get());
            }
        });

        return update(item);
    }

    //只会插入不是null的数据
    public long insert(GetHandler source) throws SQLException {
        return insert(DataItem.create(_cols, source));
    }

    //只会更新不是null的数据
    public void update(GetHandler source) throws SQLException {
        update(DataItem.create(_cols, source));
    }
}
