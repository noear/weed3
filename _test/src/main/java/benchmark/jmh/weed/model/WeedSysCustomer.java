package benchmark.jmh.weed.model;


import lombok.Data;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

@Data
@Table("sys_customer")
public class WeedSysCustomer {
    @PrimaryKey
    private Integer id;
    private String code;
    private String name;

    //@FetchMany("customerId")
    //private List<BeetlSysOrder> order;
}
