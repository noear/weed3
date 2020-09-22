package org.noear.weed.cache;

/**
 * 缓存使用控制接口
 *
 * @author noear
 * @since 3.0
 */
public interface ICacheUsing<Q> {
    Q usingCache(boolean isCache);

    Q usingCache(int seconds);

    Q refurbishCache();

    Q refurbishCache(boolean isRefubish);

    Q removeCache();
}
