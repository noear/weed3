package weed3test.dso;

import org.noear.weed.annotation.Sql;
import weed3test.model.AppxModel;

public interface RenderMapper {
    @Sql("#beetl.sql")
    AppxModel appx_get_beetl(int id);

    @Sql("#enjoy.md")
    AppxModel appx_get_enjoy(int id);

    @Sql("#freemarker.sql")
    AppxModel appx_get_ftl(int id);

    @Sql("#velocity.md")
    AppxModel appx_get_velocity(int id);
}
