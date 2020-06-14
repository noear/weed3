package org.noear.weed.wrap;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodWrap {
    private static Map<Method, MethodWrap> _cache = new ConcurrentHashMap<>();

    public static MethodWrap get(Method method) {
        MethodWrap mw = _cache.get(method);
        if (mw == null) {
            mw = new MethodWrap(method);
            MethodWrap l = _cache.putIfAbsent(method, mw);
            if (l != null) {
                mw = l;
            }
        }
        return mw;
    }

    public final Method method;
    public final Parameter[] parameters;
    public final Class<?> returnType;
    public Type returnGenericType;

    protected MethodWrap(Method m) {
        method = m;
        parameters = m.getParameters();
        returnType = m.getReturnType();
        returnGenericType = m.getGenericReturnType();
    }
}
