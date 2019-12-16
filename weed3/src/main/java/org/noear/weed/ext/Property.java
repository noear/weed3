package org.noear.weed.ext;

import java.io.Serializable;
import java.util.function.Function;

public interface Property<T, R> extends Function<T, R>, Serializable {
}