package org.noear.weed.wrap;


import org.noear.weed.WeedConfig;
import org.noear.weed.utils.NamingUtils;

import java.lang.reflect.Field;

public class NamingStrategy {
    public String classToTableName(Class<?> clz) {
        return clz.getSimpleName();
    }

    public String fieldToColumnName(Class<?> clz, Field f) {
        if (WeedConfig.isUsingUnderlineColumnName) {
            return NamingUtils.toUnderlineString(f.getName());
        } else {
            return f.getName();
        }
    }
}
