package org.noear.weed;

import java.lang.reflect.Method;

public interface IMapperInvoke {
    Object call(DbContext db, Object proxy, String sqlid, Class<?> mapperClz, Method method, Object[] args) throws Throwable;
}
