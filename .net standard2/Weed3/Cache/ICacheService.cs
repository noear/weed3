using System;

namespace Noear.Weed.Cache {
    public interface ICacheService {
         void store(String key, Object obj, int seconds);

         Object get(String key);

         void remove(String key);

         int getDefalutSeconds();

         String getCacheKeyHead();
    }
}
