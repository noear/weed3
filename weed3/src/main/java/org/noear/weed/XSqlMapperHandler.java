package org.noear.weed;

import org.noear.weed.annotation.Sql;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

class XSqlMapperHandler implements InvocationHandler {

    protected MethodHandles.Lookup lookup;
    protected DbContext db;
    protected XSqlMapperHandler(DbContext db){
        this.db = db;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class mapperClz = method.getDeclaringClass();

        if (method.isDefault()) {
            if (this.lookup == null) {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
                constructor.setAccessible(true);
                this.lookup = (MethodHandles.Lookup) constructor.newInstance(mapperClz, 2);
            }

            return this.lookup.unreflectSpecial(method, mapperClz).bindTo(proxy).invokeWithArguments(args);
        } else {
            Sql ann = method.getAnnotation(Sql.class);

            if (ann == null) {
                return XSqlHandlerForXml.forXml(db, proxy, mapperClz, method, args);
            } else {
                return XSqlHandlerForAnn.forAnn(db, proxy, mapperClz, method, args, ann);
            }
        }
    }
}
