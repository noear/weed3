package weed3demo.mapper;

import org.noear.weed.annotation.DbNamspace;

import java.util.List;

@DbNamspace("org.xxx.xxx")
public interface Mapper {
    List<UserModel> user_get(String cols, long user_id, String mobile);
}
