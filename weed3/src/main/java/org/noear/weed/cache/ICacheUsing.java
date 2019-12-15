package org.noear.weed.cache;

public interface ICacheUsing<Q> {
    Q usingCache(boolean isCache);

    Q usingCache(int seconds);

    Q refurbishCache();

    Q refurbishCache(boolean isRefubish);

    Q removeCache();
}
