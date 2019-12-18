package weed3test.model;

import lombok.Data;
import org.noear.weed.annotation.Column;
import org.noear.weed.annotation.Exclude;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

//和表名不相同，须注解表名
@Data
@Table("test")
public class TestModel {
    @PrimaryKey
    public Integer id;
    public Integer v1;
}
