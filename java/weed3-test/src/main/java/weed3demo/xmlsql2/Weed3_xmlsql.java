package weed3demo.xmlsql2;

import java.math.*;
import java.sql.SQLException;
import java.util.*;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.xml.Namespace;

import weed3demo.mapper.UserModel;

@Namespace("weed3demo.xmlsql2")
public interface Weed3_xmlsql{
  /** 添加用户 */
  long user_add(int user_id) throws SQLException;
  /** 批量添加用户3 */
  long user_add_for(Collection<UserModel> list) throws SQLException;
  /** 删除一个用户 */
  void user_del(long user_id,int sex) throws SQLException;
  /** 更新一个用户，并清理相关相存 */
  void user_set(String mobile,int sex,String icon) throws SQLException;
  /** 获取一批符合条件的用户 */
  List<UserModel> user_get_list(int foList,long user_id,String cols,String mobile) throws SQLException;
  void user_cols1() throws SQLException;
  void user_cols2() throws SQLException;
  /** 获取一批符合条件的用户 */
  List<UserModel> user_get_list2(int foList,long user_id,String mobile) throws SQLException;
}
