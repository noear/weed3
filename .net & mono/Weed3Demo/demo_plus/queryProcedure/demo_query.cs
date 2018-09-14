using System;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo_plus.queryProcedure {
    class demo_query {
        public void demo() {
            user_get_list2 sp = new user_get_list2();

            sp.userID = 12;
            sp.sex = 1;//男的

            sp.getList(new UserInfoModel());
        }

        public void demo2() {
            user_get2 sp = new user_get2();

            sp.userID = 12;

            sp.getItem(new UserInfoModel());
        }

        public void demo3() {
            user_update2 sp = new user_update2();

            sp.userID = 12;
            sp.city = "";
            sp.vipTime = DateTime.Now;

            sp.execute();
        }
    }
}
