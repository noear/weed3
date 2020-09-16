package webapp.model;

import lombok.Data;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

//和表名不相同，须注解表名
@Data
public class AppxModel2 implements IBinder{
    public int app_id;
    public String app_key;
    public String akey;

    public int agroup_id;
    public int ugroup_id;

    public String name;
    public String note;

    private int _ar_is_examine;
    private int _ar_examine_ver;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        app_id = s.get("app_id").value(0);
        app_key = s.get("app_key").value(null);
        akey = s.get("akey").value(null);

        agroup_id = s.get("agroup_id").value(0);
        ugroup_id = s.get("ugroup_id").value(0);

        name = s.get("name").value(null);
        note = s.get("note").value(null);

        _ar_is_examine = s.get("ar_is_examine").value(0);
        _ar_examine_ver = s.get("ar_examine_ver").value(0);
    }

    public IBinder clone()
    {
        return new AppxModel2();
    }
}
