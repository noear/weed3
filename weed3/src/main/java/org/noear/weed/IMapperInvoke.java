package org.noear.weed;

import java.lang.reflect.Method;

public interface IMapperInvoke {
    Object call(Object proxy, DbContext db, String sqlid, Class<?> mapperClz, Method method, Object[] args) throws Throwable;
}
