package weed3demo.mapper;

import java.util.Map;
import java.util.Collection;
import org.noear.weed.SQLBuilder;

public class DbUserApi{
    public SQLBuilder user_add(Map map){
        int user_id=(int)map.get("user_id");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(user_id) VALUES(?) ",user_id);

        return sb;
    }

    public SQLBuilder user_add_list(Map map){
        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(id,mobile,sex) VALUES ");
        Iterable<DbUserApi> list=(Iterable<DbUserApi>)map.get("list");
        for(DbUserApi m : list){
            //sb.append("(?,?,?) ",m.user_id,m.mobile,m.sex);
        }

        return sb;
    }

    public SQLBuilder user_del(Map map){
        long user_id=(long)map.get("user_id");
        int sex=(int)map.get("sex");

        SQLBuilder sb = new SQLBuilder();

        sb.append("DELETE FROM user WHERE id=? ",user_id);
        if(sex > 0){
            sb.append("AND sex=? ",sex);
        }

        return sb;
    }

    public SQLBuilder user_set(Map map){
        String mobile=(String)map.get("mobile");
        int sex=(int)map.get("sex");
        String icon=(String)map.get("icon");

        SQLBuilder sb = new SQLBuilder();

        sb.append("UPDATE user SET mobile=?,sex=? ",mobile,sex);
        if(icon != null){
            sb.append("icon=? ",icon);
        }

        return sb;
    }

    public SQLBuilder user_get(Map map){
        String cols=(String)map.get("cols");
        String mobile=(String)map.get("mobile");

        SQLBuilder sb = new SQLBuilder();

        sb.append("SELECT id,"+cols+" FROM user WHERE sex=1 AND mobile=? ",mobile);

        return sb;
    }
}