package noear.weed.cache;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheUsing<Q> {
    public Q usingCache(boolean isCache);

    public Q usingCache(int seconds);

    public Q refurbishCache();

    public Q refurbishCache(boolean isRefubish);

    public Q removeCache();

}
