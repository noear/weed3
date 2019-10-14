package weed3demo.mapper;

import org.noear.weed.SQLBuilder;

import java.util.Map;

public interface IXmlSqlBuilder {
    SQLBuilder build(Map map);
}
