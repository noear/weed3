using Noear.Weed;
using Noear.Weed.Cache;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.cache {
    public class Cache1Demo {
        //普通的缓存控制
        public static void demo_cache() {
            DbContext db = DbConfig.pc_bcf;
            ICacheService cache = null;

            db.call("user_get").set("xxx", 1)
                .caching(cache)
                .usingCache(60 * 100)
                .getItem<UserInfoModel>();
            
        }

        //带条件判断的缓存控制
        public static void demo_cache_condition() {
            DbContext db = DbConfig.pc_bcf;
            ICacheService cache = null;

            db.call("user_get").set("xxx",1)
                .caching(cache)
                .usingCache(60 * 100)
                .getItem<UserInfoModel>((cu, t) => { 
                    if (t.user_id == 12)
                        cu.usingCache(false);
                });

        }

        //分割参数值，并实现对应分别缓存
        public static void demo_cache_split() {
            DbContext db = DbConfig.pc_bcf;
            ICacheService cache = null;
            

            var sp = db.call("user_get")
                       .set("user_ids", "1,2,3,4,5,6");

            sp.caching(cache).usingCache(60 * 100);//与上面分开写（不然,返回类型需要转换）

            sp.getListBySplit<UserInfoModel>("user_ids", t => t.user_id);
        }
    }
}
