package org.noear.weed.ext;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by noear on 19-12-16.
 */
public interface Property<T, R> extends Function<T, R>, Serializable {
}