using Noear.Weed.Cache;
using System;
using System.Collections.Generic;

namespace Noear.Weed {
    public interface IQuery {
        Object getValue();
        T getValue<T>(T def);

        Variate getVariate();
        Variate getVariate(Action<CacheUsing, Variate> cacheCondition);

        T getItem<T>(T model) where T : class, IBinder;
        T getItem<T>(T model, Action<CacheUsing, T> cacheCondition) where T : class, IBinder;

        List<T> getList<T>(T model) where T : class, IBinder;
        List<T> getList<T>(T model, Action<CacheUsing, List<T>> cacheCondition) where T : class, IBinder;

        DataList getDataList();
        DataList getDataList(Action<CacheUsing, DataList> cacheCondition);
        DataItem getDataItem();
        DataItem getDataItem(Action<CacheUsing, DataList> cacheCondition);

        List<T> getArray<T>(String column);

        IQuery caching(ICacheService service);
        IQuery usingCache(bool isCache);
        IQuery usingCache(int seconds);
        IQuery cacheTag(String tag);
    }
}
