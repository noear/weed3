using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Weed3Demo.demo.model {
    [Serializable]
    public class UserInfoModel : IBinder {
        public long user_id;
        public int role;
        public String mobile;
        public String udid;
        public int city_id;
        public String name;
        public String icon;

        
        public void bind(GetHandlerEx s) {
            //1.source:数据源
            //
            user_id = s("user_id").value<long>(0);
            role    = s("role").value<short>(0);
            mobile  = s("mobile").value("");
            udid    = s("udid").value("");
            city_id = s("city_id").value<int>(0);
            name    = s("name").value("");
            icon    = s("icon").value("");

        }

        public IBinder clone() {
            return new UserInfoModel();
        }
    }
}
