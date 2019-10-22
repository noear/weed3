package weed3demo.xmlsql2;

import java.math.*;
import java.util.*;
import org.noear.weed.SQLBuilder;
import org.noear.weed.xml.XmlSqlFactory;
public class test_xml{
    private static final String _namespace="weed3demo.xmlsql2";
    public test_xml(){
        XmlSqlFactory.register(_namespace + ".user_add",this::user_add);
        XmlSqlFactory.register(_namespace + ".user_add3",this::user_add3);
        XmlSqlFactory.register(_namespace + ".user_add_for",this::user_add_for);
        XmlSqlFactory.register(_namespace + ".user_set",this::user_set);
        XmlSqlFactory.register(_namespace + ".user_add_for2",this::user_add_for2);
    }
    public SQLBuilder user_add(Map map){
        weed3demo.mapper.UserModel m = (weed3demo.mapper.UserModel)map.get("m");
        int sex = (int)map.get("sex");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(user_id,mobile,sex) VALUES(?,?,?) ",m.user_id,m.mobile,sex);

        map.put("m.user_id", m.user_id);

        return sb;
    }

    public SQLBuilder user_add3(Map map){
        weed3demo.mapper.UserModel m = (weed3demo.mapper.UserModel)map.get("m");
        int sex = (int)map.get("sex");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user ");

        SQLBuilder sb1 = new SQLBuilder();  /*trim node*/
        sb1.append("user_id,sex, ");
        if(m.mobile != null){ /*if node*/
            sb1.append("mobile, ");
        }
//        if(m.icon != null){ /*if node*/
//            sb1.append("icon, ");
//        }
        sb1.trimEnd(",");
        sb1.addPrefix("(");
        sb1.addSuffix(")");
        sb.append(sb1);

        sb.append("VALUES ");

        SQLBuilder sb2 = new SQLBuilder();  /*trim node*/
        sb2.append("?,?, ",m.user_id,sex);
        if(m.mobile != null){ /*if node*/
            sb2.append("?, ",m.mobile);
        }
//        if(m.icon != null){ /*if node*/
//            sb2.append("?, ",m.icon);
//        }
        sb2.trimEnd(",");
        sb2.addPrefix("(");
        sb2.addSuffix(")");
        sb.append(sb2);


        map.put("m.user_id", m.user_id);

        return sb;
    }

    public SQLBuilder user_add_for(Map map){
        Collection<weed3demo.mapper.UserModel> list = (Collection<weed3demo.mapper.UserModel>)map.get("list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(id,mobile,sex) VALUES ");
        { /*for node*/
            int m_index = 0;
            Iterator<weed3demo.mapper.UserModel> m_iterator = list.iterator();
            while (m_iterator.hasNext()){
                weed3demo.mapper.UserModel m = m_iterator.next();

                sb.append("(?,?,?) ",m.user_id,m.mobile,m.sex);

                if(m_iterator.hasNext()){
                    sb.append("#");
                }
                m_index++;
            }
        }

        return sb;
    }

    public SQLBuilder user_set(Map map){
        List<Integer> user_list = (List<Integer>)map.get("user_list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("UPDATE user SET sex=1 WHERE id IN(?...) ",user_list);

        return sb;
    }

    public SQLBuilder user_add_for2(Map map){
        Collection<weed3demo.mapper.UserModel> list = (Collection<weed3demo.mapper.UserModel>)map.get("list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(id,mobile,sex) VALUES ");
        { /*for node*/
            int m_index = 0;
            Iterator<weed3demo.mapper.UserModel> m_iterator = list.iterator();
            while (m_iterator.hasNext()){
                weed3demo.mapper.UserModel m = m_iterator.next();

                if(m.users != null){ /*if node*/
                    { /*for node*/
                        int m2_index = 0;
                        Iterator<weed3demo.mapper.UserModel> m2_iterator = m.users.iterator();
                        while (m2_iterator.hasNext()){
                            weed3demo.mapper.UserModel m2 = m2_iterator.next();

                            sb.append("(?,?,?) ",m2.user_id,m2.mobile,m2.sex);

                            m2_index++;
                        }
                    }
                }

                m_index++;
            }
        }

        return sb;
    }
}