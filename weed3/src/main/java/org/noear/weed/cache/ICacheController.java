package org.noear.weed.cache;

public interface ICacheController<T> {
    //使用哪个缓存服务
    T caching(ICacheService service);
    //是否使用缓存
    T usingCache(boolean isCache);
    //使用缓存并设置时间
    T usingCache(int seconds);
    //为缓存添加标签
    T cacheTag(String tag);
}
