package org.noear.weed.annotation;

import java.lang.annotation.*;

/**
 * 主键标识
 *
 * @author noear
 * @since 3.2
 * */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
}
