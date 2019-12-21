package weed3test.model;

import lombok.Data;
import org.noear.weed.annotation.Column;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;


@Data
@Table("appx")
public class Appx2Model {
    /** 应用ID */
    @PrimaryKey
    @Column("app_id")
    private Integer app_id;
    /** 应用访问KEY */
    private String app_key;
    /** （用于取代app id 形成的唯一key） //一般用于推广注册之类 */
    private String akey;

}