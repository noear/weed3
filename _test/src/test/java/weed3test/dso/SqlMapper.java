package weed3test.dso;

import org.noear.weed.BaseMapper;
import org.noear.weed.annotation.Sql;
import org.noear.weed.xml.Namespace;
import weed3test.DbUtil;
import weed3test.model.AppxModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Namespace("webapp.dso")
public interface SqlMapper extends BaseMapper<AppxModel> {
    //随便取条数据的ID
    int appx_get() throws Exception;

    //根据id取条数据
    AppxModel appx_get2(int app_id) throws Exception;

    //取一批ID
    Map<String,Object> appx_get3(String tb, int app_id) throws Exception;

    List<AppxModel> appx_getlist(int app_id) throws Exception;

    List<Integer> appx_getids() throws Exception;

    int appx_get_error();

    @Sql("select akey from appx where app_id=@app_id")
    String appx_get_key(int app_id);

    @Sql("select akey from appx where app_id=?")
    String appx_get_key2(int app_id);

    default String test() throws SQLException {
        return DbUtil.db.table("appx").where("app_id=2").select("akey").getValue("");
    }
}