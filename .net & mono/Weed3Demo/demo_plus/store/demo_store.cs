using Weed3Demo.demo.model;

namespace Weed3Demo.demo_ex.store {
    class demo_store {
        public void demo() {
            user_get_list sp = new user_get_list();

            sp.userID = 12;
            sp.sex = 1;//男的

            sp.getList<UserInfoModel>();
        }
    }
}
