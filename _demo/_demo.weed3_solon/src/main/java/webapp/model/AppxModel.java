package webapp.model;

import lombok.Data;
import lombok.Getter;
import org.noear.weed.annotation.Alias;
import org.noear.weed.annotation.PrimaryKey;

//和表名不相同，须注解表名
@Data
@Alias("appx")
public class AppxModel {
    @PrimaryKey
    public int app_id;
    public int agroup_id;
    public String note;
    public String app_key;
    public int ar_is_examine;
}
