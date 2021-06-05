package org.noear.weed.utils;

import org.noear.weed.ext.Fun0Ex;

public class RunUtils {
    public static <T> T call(Fun0Ex<T, Exception> fun) {
        try {
            return fun.run();
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }
}
