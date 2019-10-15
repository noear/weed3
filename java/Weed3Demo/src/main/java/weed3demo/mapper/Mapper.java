package weed3demo.mapper;

import org.noear.weed.xml.Namespace;

import java.util.List;

@Namespace("org.xxx.xxx")
public interface Mapper {
    List<UserModel> user_get(String cols, long user_id, String mobile);
    void user_add_for(long user_id, String mobile);
}
