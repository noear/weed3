package org.noear.weed.annotation;

import java.lang.annotation.*;

/**
 * 主键标识
 * */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
}
