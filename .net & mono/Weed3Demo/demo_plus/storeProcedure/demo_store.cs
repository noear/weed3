using System;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo_ex.store {
    class demo_store {
        public void demo() {
            user_get_list sp = new user_get_list();

            sp.userID = 12;
            sp.sex = 1;//男的

        }

        public void demo2() {
            user_get sp = new user_get();

            sp.userID = 12;

            sp.getItem(new UserInfoModel());
        }

        public void demo3() {
            user_update sp = new user_update();

            sp.userID = 12;
            sp.city = "";
            sp.vipTime = DateTime.Now;

            sp.execute();
        }
    }
}
