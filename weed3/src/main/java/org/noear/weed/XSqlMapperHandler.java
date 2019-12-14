package org.noear.weed;

import org.noear.weed.xml.Namespace;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

class XSqlMapperHandler implements InvocationHandler {

    protected MethodHandles.Lookup lookup;
    protected DbContext db;

    protected XSqlMapperHandler(DbContext db) {
        this.db = db;
    }

    private static IMapperInvoke annInvoke = new XSqlInvokeForAnn();
    private static IMapperInvoke xmlInvoke = new XSqlInvokeForXml();
    private static IMapperInvoke basInvoke = new XSqlInvokeForBas();
    protected static UnsupportedOperationException UOE = new UnsupportedOperationException();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class mapperClz = method.getDeclaringClass();

        if (method.isDefault()) {
            if (this.lookup == null) {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
                constructor.setAccessible(true);
                this.lookup = constructor.newInstance(mapperClz, MethodHandles.Lookup.PRIVATE);
            }

            return this.lookup.unreflectSpecial(method, mapperClz).bindTo(proxy).invokeWithArguments(args);
        } else {
            String sqlid = getSqlid(mapperClz, method);

            Object tmp = annInvoke.call(proxy, db, sqlid, mapperClz, method, args);

            if (UOE.equals(tmp)) {
                tmp = xmlInvoke.call(proxy, db, sqlid, mapperClz, method, args);

                if (UOE.equals(tmp)) {
                    tmp = basInvoke.call(proxy, db, sqlid, mapperClz, method, args);

                    if (UOE.equals(tmp)) {
                        throw new RuntimeException("Xmlsql does not exist:" + sqlid);
                    }
                }
            }

            return tmp;
        }
    }

    public static String getSqlid(Class<?> mapperClz, Method method) {
        Namespace c_meta = mapperClz.getAnnotation(Namespace.class);
        String fun_name = method.getName();

        String sqlid = null;
        if (c_meta == null) {
            sqlid = mapperClz.getPackage().getName() + "." + fun_name;
        } else {
            sqlid = c_meta.value() + "." + fun_name;
        }

        return sqlid;
    }
}
