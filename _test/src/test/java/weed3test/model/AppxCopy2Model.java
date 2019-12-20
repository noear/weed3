package weed3test.model;

import lombok.Data;
import org.noear.weed.annotation.Column;
import org.noear.weed.annotation.Exclude;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

//和表名不相同，须注解表名
@Data
@Table("appx_copy")
public class AppxCopy2Model {
    /** 应用ID */
    @PrimaryKey
    @Column("app_id")
    public Integer app_id;
    /** 应用访问KEY */
    public String app_key;
    /** （用于取代app id 形成的唯一key） //一般用于推广注册之类 */
    public String akey;
    /** 加入的用户组ID */
    public Integer ugroup_id;
    /** 加入的应用组ID */
    public Integer agroup_id;
    /** 应用名称 */
    public String name;
    /** 应用备注 */
    public String note;
    /** 是否开放设置 */
    public Integer ar_is_setting;
    /** 是否审核中(0: 没审核 ；1：审核中) */
    public Integer ar_is_examine;
    /** 审核 中的版本号 */
    public Integer ar_examine_ver;
    /**  */
    public Date log_fulltime;

    public LocalDateTime datetime1;
    public LocalDate date1;

    @Exclude
    public LocalTime time1;

    @Exclude
    public String agroup_name;
}
