package webapp.model;

import lombok.Data;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

//和表名不相同，须注解表名
@Data
@Table("test")
public class TestModel {
    @PrimaryKey
    public Long id;
    public Integer v1;

    public TestModel(){}

    public TestModel(long id, int v1){
        this.id = id;
        this.v1 = v1;
    }
}
