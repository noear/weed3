package weed3demo.xmlsql2;

import java.math.*;
import java.util.*;
import org.noear.weed.SQLBuilder;
import org.noear.weed.xml.XmlSqlFactory;

public class test_xml{
    private static final String _namespace="weed3demo.xmlsql2";
    public test_xml(){
        XmlSqlFactory.register(_namespace + ".user_add",this::user_add);
        XmlSqlFactory.register(_namespace + ".user_set",this::user_set);
        XmlSqlFactory.register(_namespace + ".user_set2",this::user_set2);
        XmlSqlFactory.register(_namespace + ".user_add_for",this::user_add_for);
    }
    public SQLBuilder user_add(Map map){
        int user_id=(int)map.get("user_id");
        String mobile=(String)map.get("mobile");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(user_id,mobile) VALUES(?,?) ",user_id,mobile);

        return sb;
    }

    public SQLBuilder user_set(Map map){
        List<Integer> user_list=(List<Integer>)map.get("user_list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("UPDATE user SET sex=1 WHERE id IN(?...) ",user_list);

        return sb;
    }

    public SQLBuilder user_set2(Map map){
        List<Integer> user_list=(List<Integer>)map.get("user_list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("UPDATE user SET sex=1 WHERE id IN(?...) ",user_list);

        return sb;
    }

    public SQLBuilder user_add_for(Map map){
        Collection<weed3demo.mapper.UserModel> list=(Collection<weed3demo.mapper.UserModel>)map.get("list");

        SQLBuilder sb = new SQLBuilder();

        sb.append("INSERT user(id,mobile,sex) VALUES ");
        {
            int m_index = 0;
            Iterator<weed3demo.mapper.UserModel> m_iterator = list.iterator();
            while (m_iterator.hasNext()){
                weed3demo.mapper.UserModel m = m_iterator.next();

                if(m.users != null){
                    {
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