package noear.weed;

import noear.weed.cache.CacheUsing;
import noear.weed.cache.ICacheService;
import noear.weed.ext.Act2;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 14/11/12.
 */
public interface IQuery {
    public long getCount() throws SQLException;
    public Object getValue() throws SQLException;
    public <T> T getValue(T def) throws SQLException;
    public <T> T getValue(T def, Act2<CacheUsing, T> cacheCondition) throws SQLException;
    public <T extends IBinder> T getItem(T model) throws SQLException;
    public <T extends IBinder> T getItem(T model, Act2<CacheUsing, T> cacheCondition) throws SQLException;
    public <T extends IBinder> List<T> getList(T model) throws SQLException;
    public <T extends IBinder> List<T> getList(T model, Act2<CacheUsing, List<T>> cacheCondition) throws SQLException;
    public DataList getDataList() throws SQLException;
    public DataList getDataList(Act2<CacheUsing, DataList> cacheCondition) throws SQLException;
    public DataItem getDataItem() throws SQLException;
    public DataItem getDataItem(Act2<CacheUsing, DataList> cacheCondition) throws SQLException;

    public IQuery caching(ICacheService service);
    public IQuery usingCache(boolean isCache);
    public IQuery usingCache(int seconds);
    public IQuery cacheTag(String tag);
}
