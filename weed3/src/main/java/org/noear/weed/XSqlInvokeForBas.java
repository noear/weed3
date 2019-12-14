package org.noear.weed;


import java.lang.reflect.Method;

class XSqlInvokeForBas implements IMapperInvoke {
    public Object call(DbContext db, Object proxy, String sqlid, Class<?> mapperClz, Method method, Object[] args) throws Throwable {
        if (BaseMapper.class == mapperClz) {
            Object tmp = new BaseMapperWrap(db, (BaseMapper) proxy){};

            return method.invoke(tmp, args);
        }

        return XSqlMapperHandler.UOE;
    }
}
