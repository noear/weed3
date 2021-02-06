package weed3rdb.dso;

import org.noear.weed.BaseMapper;
import org.noear.weed.annotation.Sql;
import org.noear.weed.xml.Namespace;
import webapp.model.AppxModel;
import weed3rdb.DbUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Namespace("webapp.dso.SqlMapper")
public interface SqlMapper extends BaseMapper<AppxModel> {
    //随便取条数据的ID
    int appx_get() throws SQLException;

    //根据id取条数据
    AppxModel appx_get2(int app_id) throws SQLException;

    //取一批ID
    Map<String,Object> appx_get3(String tb, int app_id) throws SQLException;

    List<AppxModel> appx_getlist(int app_id) throws SQLException;

    List<Integer> appx_getids() throws SQLException;

    int appx_get_error();

    @Sql("select akey from appx where app_id=@app_id")
    String appx_get_key(int app_id);

    @Sql("select akey from appx where app_id=@{app_id}")
    String appx_get_key2(int app_id);

    @Sql("select akey from appx where app_id=?")
    String appx_get_key3(int app_id);

    @Sql("select akey from appx where agroup_id=@agroup_id AND app_id IN (@app_ids)")
    List<String> appx_get_key_list(int agroup_id,List<Integer> app_ids);

    @Sql("select akey from appx where agroup_id=@{agroup_id} AND app_id IN (@{app_ids})")
    List<String> appx_get_key_list2(int agroup_id,List<Integer> app_ids);

    default String test() throws SQLException {
        return DbUtil.db.table("appx").whereEq("app_id",2).select("akey").getValue("");
    }
}
