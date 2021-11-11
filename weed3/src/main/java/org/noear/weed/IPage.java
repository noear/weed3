package org.noear.weed;

import java.util.List;

/**
 * @author noear 2021/11/11 created
 */
public interface IPage<T> {
    List<T> getList();

    long getTotal();
}
