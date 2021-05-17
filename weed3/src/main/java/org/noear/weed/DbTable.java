package org.noear.weed;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Deprecated
public class DbTable extends DbTableQueryBase<DbTable> {
    protected DataItem _item = new DataItem(); //会排除null数据
    protected Map<String, Supplier<Object>> _gett = new LinkedHashMap<>();

    public DbTable(DbContext context) {
        super(context);
    }

    protected void set(String name, Supplier<Object> valueGetter) {
        _gett.put(name, valueGetter);
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
}
