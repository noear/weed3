package org.noear.weed;

import org.noear.weed.annotation.Sql;

import java.lang.reflect.*;

class XSqlMapperHandler implements InvocationHandler {
    public static final XSqlMapperHandler g = new XSqlMapperHandler();

    @Override
    public Object invoke(Object proxy, Method method, Object[] vals) throws Throwable {
        Sql ann = method.getAnnotation(Sql.class);

        if (ann == null) {
            return XSqlHandlerForXml.forXml(proxy, method, vals);
        } else {
            return XSqlHandlerForAnn.forAnn(proxy, method, vals, ann);
        }
    }
}
