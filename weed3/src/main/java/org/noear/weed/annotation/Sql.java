package org.noear.weed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL注解
 *
 * @author noear
 * @since 3.2
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {
    String value() default "";      //代码
    String caching() default "";    //缓存服务
    String cacheClear() default ""; //清除缓存
    String cacheTag() default "";   //缓存标签
    int usingCache() default 0;     //缓存时间
}
