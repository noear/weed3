package org.noear.weed;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class XSqlInvokeForBas implements IMapperInvoke {

    static Map<Object, BaseMapperWrap> _lib = new ConcurrentHashMap<>();

    static BaseMapperWrap getWrap(Object proxy, DbContext db) {
        BaseMapperWrap tmp = _lib.get(proxy.getClass());

        if (tmp == null) {
            tmp = new BaseMapperWrap(db, (BaseMapper) proxy);
            _lib.putIfAbsent(proxy.getClass(), tmp);
        }

        return tmp;
    }

    public Object call(Object proxy, DbContext db, String sqlid, Class<?> mapperClz, Method method, Object[] args) throws Throwable {
        if (BaseMapper.class == mapperClz) {
            Object tmp = getWrap(proxy, db);

            return method.invoke(tmp, args);
        }

        return XSqlMapperHandler.UOE;
    }
}
