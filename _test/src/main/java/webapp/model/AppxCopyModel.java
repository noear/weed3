package webapp.model;

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
public class AppxCopyModel {
    /** 应用ID */
    @PrimaryKey
    @Column("app_id")
    public Integer app_id;
    /**  */
    public Date log_fulltime;

    public LocalDateTime datetime1;
    public LocalDate date1;
    public LocalTime time1;

    @Exclude
    public String agroup_name;
}
