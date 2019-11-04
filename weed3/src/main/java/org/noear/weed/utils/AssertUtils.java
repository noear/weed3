package org.noear.weed.utils;


public class AssertUtils {
    private AssertUtils() {
    }


    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void noNullElements(Object[] objects, String msg) {
        Object[] var2 = objects;
        int var3 = objects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object obj = var2[var4];
            if (obj == null) {
                throw new IllegalArgumentException(msg);
            }
        }

    }

    public static void notEmpty(String string, String msg) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void fail(String msg) {
        throw new IllegalArgumentException(msg);
    }
}