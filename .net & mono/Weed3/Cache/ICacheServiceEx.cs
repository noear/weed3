using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed.Cache {
    public interface ICacheServiceEx : ICacheService {
        CacheTags tags();
        void clear(String tag);
        void update<T>(String tag, Func<T, T> setter) where T : class;
    }
}
