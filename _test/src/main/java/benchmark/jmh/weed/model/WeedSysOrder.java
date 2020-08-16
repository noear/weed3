package benchmark.jmh.weed.model;

import lombok.Data;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

@Data
@Table("sys_order")
public class WeedSysOrder {
    @PrimaryKey
    private Integer id;
    private String name;
    private Integer customerId;
}
