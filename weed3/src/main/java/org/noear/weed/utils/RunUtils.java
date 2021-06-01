package org.noear.weed.utils;

import org.noear.weed.ext.Fun0Ex;

import java.net.URL;

public class RunUtils {
    public static <T> T call(Fun0Ex<T, Exception> fun) {
        try {
            return fun.run();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            throw ThrowableUtils.throwableWrap(ex);
        }
    }
}
