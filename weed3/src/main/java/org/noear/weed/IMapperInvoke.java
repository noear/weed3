package org.noear.weed;

import java.lang.reflect.Method;

public interface IMapperInvoke {
    Object call(Object proxy, DbContext db, String sqlid, Class<?> caller, Method method, Object[] args) throws Throwable;
}
