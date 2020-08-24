package org.noear.weed.wrap;


import org.noear.weed.WeedConfig;
import org.noear.weed.utils.NameUtils;

import java.lang.reflect.Field;

public class NameStrategy {
    public String classToTableName(Class<?> clz) {
        return clz.getSimpleName();
    }

    public String fieldToColumnName(Class<?> clz, Field f) {
        if (WeedConfig.isUsingUnderlineColumnName) {
            return NameUtils.toUnderlineString(f.getName());
        } else {
            return f.getName();
        }
    }
}
