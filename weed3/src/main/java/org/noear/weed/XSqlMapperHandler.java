package org.noear.weed;

import org.noear.weed.xml.Namespace;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

class XSqlMapperHandler implements InvocationHandler {

    protected MethodHandles.Lookup lookup;
    protected DbContext db;
    protected Class<?> mapperInf;

    protected XSqlMapperHandler(DbContext db, Class<?> mapperInf) {
        this.db = db;
        this.mapperInf = mapperInf;
    }

    private static IMapperInvoke annInvoke = new XSqlInvokeForAnn();
    private static IMapperInvoke xmlInvoke = new XSqlInvokeForXml();
    private static IMapperInvoke basInvoke = new XSqlInvokeForBas();

    protected static UnsupportedOperationException UOE = new UnsupportedOperationException();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class caller = method.getDeclaringClass();

        if (method.isDefault()) {
            if (this.lookup == null) {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
                constructor.setAccessible(true);
                this.lookup = constructor.newInstance(caller, MethodHandles.Lookup.PRIVATE);
            }

            return this.lookup.unreflectSpecial(method, caller).bindTo(proxy).invokeWithArguments(args);
        } else {
            String sqlid = getSqlid(caller, method);

            //1.尝试有@Sql注解的
            Object tmp = annInvoke.call(proxy, db, sqlid, caller, method, args);

            if (UOE.equals(tmp)) {
                //2.尝试有xml的
                tmp = xmlInvoke.call(proxy, db, sqlid, caller, method, args);

                if (UOE.equals(tmp)) {
                    //3.尝试BaseMapper
                    tmp = basInvoke.call(proxy, db, sqlid, caller, method, args);

                    if (UOE.equals(tmp)) {
                        //4.尝试Object的
                        if (Object.class == caller) {
                            String name = method.getName();
                            switch (name){
                                case "toString": return "Weed mapper "+mapperInf;
                                case "hashCode": return System.identityHashCode(proxy);
                            }
                        }

                        if (UOE.equals(tmp)) {
                            throw new RuntimeException("Xmlsql does not exist:@" + sqlid);
                        }
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
