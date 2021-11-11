package org.noear.weed.impl;

import org.noear.weed.IPage;

import java.util.List;

/**
 * @author noear 2021/11/11 created
 */
public class IPageImpl<T> implements IPage<T> {

    private final List<T> list;
    private final long total;

    public IPageImpl(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public long getTotal() {
        return total;
    }
}
