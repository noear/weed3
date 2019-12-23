package weed3test.render;

import org.noear.weed.annotation.Sql;

public interface TestMapper {
    @Sql("#tml/beetl.btl.sql")
    AppxModel appx_get_beetl(int id);

    @Sql("#tml/enjoy.enj.sql")
    AppxModel appx_get_enjoy(int id);

    @Sql("#tml/freemarker.ftl")
    AppxModel appx_get_ftl(int id);

    @Sql("#tml/velocity.vm")
    AppxModel appx_get_velocity(int id);
}
