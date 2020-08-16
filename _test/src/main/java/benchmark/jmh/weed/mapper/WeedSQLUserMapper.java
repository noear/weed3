package benchmark.jmh.weed.mapper;


import benchmark.jmh.weed.model.WeedSQLSysUser;
import org.noear.weed.BaseMapper;
import org.noear.weed.annotation.Sql;
import org.noear.weed.xml.Namespace;

import java.util.List;

@Namespace("benchmark.jmh.weed.mapper")
public interface WeedSQLUserMapper extends BaseMapper<WeedSQLSysUser> {
    @Sql("select * from sys_user where id = ?")
    WeedSQLSysUser selectById(Integer id);

    @Sql("select * from sys_user where id = @{id}")
    WeedSQLSysUser selectTemplateById(Integer id);

    WeedSQLSysUser userSelect(Integer id);

    List<WeedSQLSysUser> queryPage(String code, int start, int end);
}