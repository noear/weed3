package org.noear.weed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL注解
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {
    String value() default ""; //别名
    String caching() default "";
    String cacheClear() default "";
    String cacheTag() default "";
    int usingCache() default 0;
}
