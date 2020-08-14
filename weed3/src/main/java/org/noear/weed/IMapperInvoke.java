package org.noear.weed;

import org.noear.weed.wrap.MethodWrap;

import java.sql.SQLException;

public interface IMapperInvoke {
    Object call(Object proxy, DbContext db, String sqlid, Class<?> caller, MethodWrap mWrap, Object[] args) throws Throwable;
}
