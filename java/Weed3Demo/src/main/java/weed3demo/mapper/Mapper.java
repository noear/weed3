package weed3demo.mapper;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.annotation.DbNamspace;

import java.util.List;
import java.util.Map;

@DbNamspace("org.xxx.xxx")
public interface Mapper {
    List<UserModel> user_get(String cols, long user_id, String mobile);
    void user_add_for(long user_id, String mobile);
}
