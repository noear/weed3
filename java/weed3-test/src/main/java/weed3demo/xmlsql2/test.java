package weed3demo.xmlsql2;

import java.math.*;
import java.sql.SQLException;
import java.util.*;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.xml.Namespace;

import weed3demo.mapper.UserModel;

@Namespace("weed3demo.xmlsql2")
public interface test{
    //添加用户
    long user_add(int user_id, String mobile) throws Exception;

    //更新用户
    long user_set(List<Integer> user_list) throws Exception;

    //添加用户
    long user_set2(List<Integer> user_list) throws Exception;

    //批量添加用户3
    long user_add_for(Collection<UserModel> list) throws Exception;

    //批量添加用户3
    long user_add_for2(Collection<UserModel> list) throws Exception;
}
