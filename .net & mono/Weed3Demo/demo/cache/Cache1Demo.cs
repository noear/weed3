using Noear.Weed;
using Noear.Weed.Cache;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.cache {
    public class Cache1Demo {
        //普通的缓存控制
        public static void demo_cache() {
            DbContext db = DbConfig.pc_bcf;
            ICacheServiceEx cache = null;

            db.call("user_get").set("xxx", 1)
                .caching(cache)
                .usingCache(60 * 1000)
                .getItem(new UserInfoModel());
            
        }

        public static void demo_cache3() {
            DbContext db = DbConfig.pc_bcf;
            ICacheServiceEx cache = null;

            //1.缓存并添加简易标签
            db.call("user_get").set("xxx", 1)
                .caching(cache)
                .cacheTag("user_"+ 1)
                .usingCache(60 * 1000)
                .getItem(new UserInfoModel());

            CacheTags tags = new CacheTags(cache);
            //2.1.可根据标签清除缓存
            tags.clear("user_" + 1);

            //2.2.可根据标签更新缓存
            tags.update<UserInfoModel>("user_" + 1, (m)=>{
                m.name = "xxx";
                return m;
            });

        }

        //带条件判断的缓存控制
        public static void demo_cache_condition() {
            DbContext db = DbConfig.pc_bcf;
            ICacheServiceEx cache = null;

            db.call("user_get").set("xxx",1)
                .caching(cache)
                .usingCache(60 * 100)
                .getItem(new UserInfoModel(), (cu, t) => { 
                    if (t.user_id == 0)
                        cu.usingCache(false);
                });
        }

        //分割参数值，并实现对应分别缓存
        public static void demo_cache_split() {
            DbContext db = DbConfig.pc_bcf;
            ICacheServiceEx cache = null;
            

            var sp = db.call("user_get")
                       .set("user_ids", "1,2,3,4,5,6");

            sp.caching(cache).usingCache(60 * 100);//与上面分开写（不然,返回类型需要转换）

            sp.getListBySplit(new UserInfoModel(), "user_ids", t => t.user_id);
        }
    }
}
