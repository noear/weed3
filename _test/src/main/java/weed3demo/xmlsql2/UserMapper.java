package weed3demo.xmlsql2;

import java.math.*;
import java.time.*;
import java.util.*;

import org.noear.weed.BaseMapper;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.annotation.Db;
import org.noear.weed.xml.Namespace;

@Db("db_sword")
@Namespace("weed3demo.xmlsql2")
public interface UserMapper{
    //更新白名单信息
    int user_whitelist_update(Date update_fulltime, long user_id, String name, int is_reg, int auth_status, int order_status, double ava_amt, int update_date, String mobile) throws Exception;
}
