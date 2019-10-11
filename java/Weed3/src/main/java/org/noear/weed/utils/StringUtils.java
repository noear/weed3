package org.noear.weed.utils;

import java.util.Stack;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }



    private static final Stack<StringBuilder> builders = new Stack();
    private static final int MaxCachedBuilderSize = 8192;
    private static final int MaxIdleBuilders = 8;

    //借用StringBuilder（基于Stack管理）
    public static StringBuilder borrowBuilder() {
        synchronized(builders) {
            return builders.empty() ? new StringBuilder(8192) : (StringBuilder)builders.pop();
        }
    }

    //释放StringBuilder（基于Stack管理）
    public static String releaseBuilder(StringBuilder sb) {
        if(sb==null){
            return null;
        }

        String string = sb.toString();
        if (sb.length() > 8192) {
            sb = new StringBuilder(8192);
        } else {
            sb.delete(0, sb.length());
        }

        synchronized(builders) {
            builders.push(sb);

            while(builders.size() > 8) {
                builders.pop();
            }

            return string;
        }
    }
}
