package weed3demo.demo.model;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * Created by noear on 2017/7/22.
 */
public class UserInfoModel implements IBinder {
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
        user_id = s.get("user_id").value(0l);
        role    = s.get("role").value(0);
        mobile  = s.get("mobile").value("");
        udid    = s.get("udid").value("");
        city_id = s.get("city_id").value(0);
        name    = s.get("name").value("");
        icon    = s.get("icon").value("");

    }

    public IBinder clone() {
        return new UserInfoModel();
    }
}
