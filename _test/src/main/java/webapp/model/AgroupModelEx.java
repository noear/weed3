package webapp.model;

import lombok.Data;

//和表名不相同，须注解表名
@Data
public class AgroupModelEx extends AgroupModel{
    public String tag;
}
