package org.noear.weed.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author noear 2021/6/1 created
 */
public final class ThrowableUtils {
    /**
     * 获取异常打印信息
     * */
    public static String throwableToString(Throwable ex){
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

    /**
     * 包装异常
     * */
    public static RuntimeException throwableWrap(Throwable ex){
        if(ex instanceof RuntimeException){
            return  (RuntimeException)ex;
        }else {
            return new RuntimeException(ex);
        }
    }

    /**
     * 解包异常
     * */
    public static Throwable throwableUnwrap(Throwable ex) {
        Throwable th = ex;

        while (true) {
            if (th instanceof InvocationTargetException) {
                th = ((InvocationTargetException) th).getTargetException();
            } else if (th instanceof UndeclaredThrowableException) {
                th = ((UndeclaredThrowableException) th).getUndeclaredThrowable();
            } else if (th.getClass() == RuntimeException.class) {
                if (th.getMessage() == null && th.getCause() != null) {
                    th = th.getCause();
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return th;
    }

    public static boolean throwableHas(Throwable ex, Class<? extends Throwable> clz) {
        Throwable th = ex;

        while (true) {
            if (clz.isAssignableFrom(th.getClass())) {
                return true;
            }

            if (th instanceof InvocationTargetException) {
                th = ((InvocationTargetException) th).getTargetException();
            } else if (th instanceof UndeclaredThrowableException) {
                th = ((UndeclaredThrowableException) th).getUndeclaredThrowable();
            } else if (th.getCause() != null) {
                th = th.getCause();
            } else {
                break;
            }
        }

        return false;
    }
}
