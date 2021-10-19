package org.noear.weed;

import java.io.Serializable;
import java.util.List;

/**
 * @author noear 2021/10/19 created
 */
public class Page<T> implements Serializable {
    public long count;
    public List<T> list;
}
