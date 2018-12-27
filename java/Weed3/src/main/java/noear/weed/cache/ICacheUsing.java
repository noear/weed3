package noear.weed.cache;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheUsing<Q> {
    Q usingCache(boolean isCache);

    Q usingCache(int seconds);

    Q refurbishCache();

    Q refurbishCache(boolean isRefubish);

    Q removeCache();
}
