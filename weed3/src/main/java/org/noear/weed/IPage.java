package org.noear.weed;

import java.util.List;

/**
 * @author noear 2021/11/11 created
 */
public interface IPage<T> {
    /**
     * 记录
     * */
    List<T> getList();

    /**
     * 总记录数
     * */
    long getTotal();

    /**
     * 页长
     * */
    int getSize();

    /**
     * 总而数
     * */
    long getPages();
}
