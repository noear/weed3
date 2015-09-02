package noear.weed;

import noear.weed.ext.Fun0;

import java.sql.SQLException;

public class DbTable extends DbTableQueryBase<DbTable> {
    DataItemEx _item = new DataItemEx(true); //会排除null数据

    public DbTable(DbContext context)  {
        super(context);
    }

    protected void set(String name, Fun0<Object> valueGetter) {
        _item.set(name, valueGetter);
    }

    //只会插入不是null的数据
    public long insert() throws SQLException{
        return insert(_item);
    }

    //只会更新不是null的数据
    public int update() throws SQLException{
        return update(_item);
    }

    //只会插入不是null的数据
    public long insert(GetHandler source) throws SQLException{

        return insert(DataItem.create(_item, source));
    }

    //只会更新不是null的数据
    public void update(GetHandler source) throws SQLException{
        update(DataItem.create(_item, source));
    }
}
