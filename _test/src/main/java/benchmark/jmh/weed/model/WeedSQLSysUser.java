package benchmark.jmh.weed.model;

import lombok.Data;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

@Data
@Table("sys_user")
public class WeedSQLSysUser {
    @PrimaryKey
    private Integer id ;
    private String code ;
}
