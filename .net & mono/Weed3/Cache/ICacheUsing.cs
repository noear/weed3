
namespace Noear.Weed.Cache {
    public interface ICacheUsing<Q> {
        Q usingCache(bool isCache);

        Q usingCache(int seconds);

        Q refurbishCache();

        Q refurbishCache(bool isRefubish);

        Q removeCache();
    }
}
