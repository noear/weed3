package weed3demo.xmlsql;

import java.util.Map;
import java.util.List;
import java.util.Collection;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.xml.Namespace;

@Namespace("weed3demo.xmlsql")
public interface DbUserApi{
  /** 添加用户 */
  long user_add(int user_id);
  /** 添加用户2 */
  long user_add_if(int user_id,String mobile);
  /** 批量添加用户3 */
  long user_add_for(Collection<weed3demo.mapper.UserModel> list);
  /** 删除一个用户 */
  void user_del(long user_id,int sex);
  /** 更新一个用户，并清理相关相存 */
  void user_set(String mobile,int sex,String icon,long user_id);
  /** 获取一批符合条件的用户 */
  List<weed3demo.mapper.UserModel> user_get(int foList,long user_id,String cols,String mobile);
}
