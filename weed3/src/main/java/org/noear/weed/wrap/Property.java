package org.noear.weed.wrap;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by noear on 19-12-16.
 */
@FunctionalInterface
public interface Property<T, R> extends Function<T, R>, Serializable {
}