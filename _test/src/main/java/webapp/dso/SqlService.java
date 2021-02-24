package webapp.dso;

import java.math.*;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.aspect.annotation.Service;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.annotation.Db;

import webapp.model.AppxModel;

@Service
public class SqlService{
    @Inject
    webapp.dso.SqlMapper mapper;

    //随便取条数据的ID
    public int appx_get() throws SQLException{
        return mapper.appx_get();
    }

    //根据id取条数据
    public AppxModel appx_get2(int app_id) throws SQLException{
        return mapper.appx_get2(app_id);
    }

    //取一批ID
    public Map<String,Object> appx_get3(String tb, int app_id) throws SQLException{
        return mapper.appx_get3(tb,app_id);
    }

    public List<AppxModel> appx_getlist(int app_id) throws SQLException{
        return mapper.appx_getlist(app_id);
    }

    public List<Integer> appx_getids() throws SQLException{
        return mapper.appx_getids();
    }
}
