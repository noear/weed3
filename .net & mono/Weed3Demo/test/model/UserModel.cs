using Noear.Weed;
using System;

namespace Weed3Demo.test.model {
    public class UserModel : IBinder {
        public long UserID;
        public String Nickname;
        public int Sex;
        public String Icon;
        public String City;

        public void bind(GetHandlerEx s) {
            UserID   = s("UserID").value(0L);
            Nickname = s("Nickname").value("");
            Sex      = s("Sex").value(0);
            Icon     = s("Icon").value("");
            City     = s("City").value("");
        }

        public IBinder clone() {
            return new UserModel();
        }
    }
}
