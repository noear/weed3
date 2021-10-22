package org.noear.weed.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author noear 2021/10/22 created
 */
public class Page<T> implements Serializable {
    final long total;
    final List<T> list;

    public long getTotal() {
        return total;
    }

    public List<T> getList() {
        return list;
    }

    public int getListSize() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public Page(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    @Override
    public String toString() {
        return "EsResult{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
