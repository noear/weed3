package org.noear.weed;

import org.noear.weed.annotation.Sql;
import org.noear.weed.xml.*;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class XmlSqlMapperHandler implements InvocationHandler {
    public static final XmlSqlMapperHandler g = new XmlSqlMapperHandler();

    @Override
    public Object invoke(Object proxy, Method method, Object[] vals) throws Throwable {
        Sql ann = method.getAnnotation(Sql.class);

        if (ann == null) {
            return XmlSqlHandlerForXml.forXml(proxy, method, vals);
        } else {
            return XmlSqlHandlerForAnn.forAnn(proxy, method, vals, ann);
        }
    }
}
