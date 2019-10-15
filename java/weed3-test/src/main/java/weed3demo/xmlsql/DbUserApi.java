package weed3demo.xmlsql;

import java.util.Map;
import java.util.List;
import java.util.Collection;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.xml.Namespace;

@Namespace("weed3demo.xmlsql")
public interface DbUserApi{
  int user_add(int user_id);
  int xxx(int user_id,String mobile);
  int user_add_if(int user_id,String mobile);
  void user_add_for(Collection<weed3demo.mapper.UserModel> list);
  void user_del(long user_id,int sex);
  void user_set(String mobile,int sex,String icon,long user_id);
  List<weed3demo.mapper.UserModel> user_get(int foList,long user_id,String cols,String mobile);
}
