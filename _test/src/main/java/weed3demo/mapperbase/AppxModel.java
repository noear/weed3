package weed3demo.mapperbase;

import lombok.Data;
import org.noear.weed.annotation.Name;
import org.noear.weed.annotation.PrimaryKey;

//和表名不相同，须注解表名
@Data
@Name("appx")
public class AppxModel {
    @PrimaryKey
    public Integer app_id;
    public Integer agroup_id;
    public String note;
    public String app_key;
    public Integer ar_is_examine;
    public Integer ar_is_setting;
}
