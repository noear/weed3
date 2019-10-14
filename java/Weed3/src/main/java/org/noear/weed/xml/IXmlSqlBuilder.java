package org.noear.weed.xml;

import org.noear.weed.SQLBuilder;

import java.util.Map;

public interface IXmlSqlBuilder {
    SQLBuilder build(Map map);
}
