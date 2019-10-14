package weed3demo.mapper;

import java.util.Map;
import java.util.Collection;
import org.noear.weed.SQLBuilder;
import org.noear.weed.xml.XmlSqlFactory;

public class weed_xml_sql{
    private static final String _namespace="org.xxx.xxx";
    public weed_xml_sql(){
        XmlSqlFactory.register(_namespace + ".user_add",this::user_add);
        XmlSqlFactory.register(_namespace + ".user_add_if",this::user_add_if);
        XmlSqlFactory.register(_namespace + ".user_del",this::user_del);
        XmlSqlFactory.register(_namespace + ".user_set",this::user_set);
        XmlSqlFactory.register(_namespace + ".user_get",this::user_get);
    }
    public SQLBuilder user_add(Map map){
        int user_id=(int)map.get("user_id");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(user_id) VALUES(?) ",user_id);

        return sb;
    }

    public SQLBuilder user_add_if(Map map){
        int user_id=(int)map.get("user_id");
        String mobile=(String)map.get("mobile");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(user_id ");
        if(mobile !=null){
            sb.append(",mobile ");
        }
        sb.append(") VALUES(? ",user_id);
        if(mobile !=null){
            sb.append(",? ",mobile);
        }
        sb.append(") ");

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
        int foList=(int)map.get("foList");
        String cols=(String)map.get("cols");
        String mobile=(String)map.get("mobile");

        SQLBuilder sb = new SQLBuilder();

        sb.append("SELECT id,"+cols+" FROM user WHERE sex>1 AND mobile=? ",mobile);
        if(foList == 0){
            sb.append("AND type='article' ");
        }
        if(foList == 1){
            sb.append("AND type='colub' ");
        }
        if(foList == 2){
            sb.append("AND type='post' ");
        }

        return sb;
    }
}
