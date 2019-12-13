package webapp.model;

import org.noear.weed.annotation.PrimaryKey;

//和表名相同，不用注解表名
public class Appx {
    @PrimaryKey
    public int app_id;
    public int agroup_id;
    public String note;
    public String app_key;
    public int ar_is_examine;
}
