package org.noear.weed.annotation;

import java.lang.annotation.*;

/**
 * 表标识; 可继承
 * */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value(); //别名
}
