package org.noear.weed.wrap;

import org.noear.weed.utils.ThrowableUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyWrap implements Serializable {
    private static Map<String, Class<?>> _clzCache = new ConcurrentHashMap<>();
    private static Class<?> getClz(String implClz) {
        Class<?> clz = _clzCache.get(implClz);
        if (clz == null) {
            try {
                clz = Class.forName(implClz.replace("/", "."));
                Class<?> l = _clzCache.putIfAbsent(implClz, clz);
                if (l != null) {
                    clz = l;
                }
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        return clz;
    }

    /** 属性 */
    public final Property property;
    /** Class wrap */
    public final ClassWrap clzWrap;
    /** 原始字段名 */
    public final String name;

    /** 别名 */
    private String _alias;
    public PropertyWrap alias(String alias){
        _alias = alias;
        return this;
    }

    public PropertyWrap(PropertyWrap pw){
        property = pw.property;
        clzWrap = pw.clzWrap;
        name = pw.name;
    }

    public PropertyWrap(Property p, String implClz, String name) {
        this.property = p;
        this.clzWrap = ClassWrap.get(getClz(implClz));

        this.name = clzWrap.getFieldWrap(name).name;
    }

    public String getColumnName(List<ClassWrap> cl) {
        if (cl == null) {
            return name;
        } else {
            int idx = cl.indexOf(clzWrap);
            if (idx < 0) {
                return name;
            } else {
                return "t" + idx + "." + name;
            }
        }
    }

    public String getSelectName(List<ClassWrap> cl) {
        if (_alias == null) {
            return getColumnName(cl);
        } else {
            return getColumnName(cl) + " " + _alias;
        }
    }



    private static Map<Property, PropertyWrap> _popCache = new ConcurrentHashMap<>();

    public static <C> PropertyWrap get(Property<C, ?> p) {
        PropertyWrap tmp = _popCache.get(p);
        if (tmp == null) {
            tmp = wrap(p);
            PropertyWrap l = _popCache.putIfAbsent(p, tmp);
            if (l != null) {
                tmp = l;
            }
        }
        return tmp;
    }

    /** 将 Property 转为 PropertyWrap  */
    private static <C> PropertyWrap wrap(Property<C, ?> p) {
        try {
            Method fun = p.getClass().getDeclaredMethod("writeReplace");
            fun.setAccessible(Boolean.TRUE);
            SerializedLambda slambda = (SerializedLambda) fun.invoke(p);
            String method = slambda.getImplMethodName();
            String attr = null;
            if (method.startsWith("get")) {
                attr = method.substring(3);
            } else {
                attr = method.substring(2);//is
            }
            return new PropertyWrap(p, slambda.getImplClass(), attr);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
