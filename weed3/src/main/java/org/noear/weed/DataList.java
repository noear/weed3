package org.noear.weed;


import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.ClassWrap;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by noear on 14-9-10.
 *
 * getRow,addRow,getRowCount 是为跨平台设计的接口，不能去掉
 */
public class DataList extends ArrayList<DataItem> implements Serializable {
    public List<DataItem> getRows() {
        return this;
    }

    public DataItem getRow(int index) {
        return get(index);
    }

    public void addRow(DataItem row) {
        add(row);
    }

    public int getRowCount() {
        return size();
    }

    public void clear() {
        this.clear();
    }

    public DataItem getLastRow() {
        if (getRowCount() > 0) {
            return get(getRowCount() - 1);
        } else {
            return null;
        }
    }

    //----------

    //转为map数组
    public List<Map<String, Object>> getMapList() {
        return (List) this;
    }

    /**
     * 将所有列转为类做为数组的数据（类为：IBinder 子类）
     */
    public <T extends IBinder> List<T> toList(T model) throws SQLException {
        List<T> list = new ArrayList<T>(getRowCount());

        for (DataItem r : this) {
            T item = (T) model.clone();

            if (WeedConfig.isDebug) {
                if (model.getClass().isInstance(item) == false) {
                    throw new SQLException(model.getClass() + " clone error (" + item.getClass() + ")");
                }
            }

            item.bind((key) -> r.getVariate(key));

            list.add(item);
        }
        return list;
    }

    /**
     * 将所有列转为类做为数组的数据
     */
    public <T> List<T> toEntityList(Class<T> clz) {
        List<T> list = new ArrayList<T>(getRowCount());

        ClassWrap clzWrap = ClassWrap.get(clz);

        for (DataItem r : this) {
            T item = clzWrap.toEntity(r);
            list.add((T) item);
        }
        return list;
    }

    /**
     * 选1列做为MAP的key，并把行数据做为val
     */
    public Map<String, Object> toMap(String keyColumn) {
        return toMap(keyColumn, null);
    }

    /**
     * 选两列做为MAP的数据
     */
    public Map<String, Object> toMap(String keyColumn, String valColumn) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(valColumn)) {
            for (DataItem r : this) {
                map.put(r.get(keyColumn).toString(), r);
            }
        } else {
            for (DataItem r : this) {
                map.put(r.get(keyColumn).toString(), r.get(valColumn));
            }
        }

        return map;
    }

    /**
     * 选一列做为SET的数据
     */
    public <T> Set<T> toSet(String column) {
        Set<T> set = new HashSet<>();

        for (DataItem r : this) {
            set.add((T) r.get(column));
        }
        return set;
    }


    /**
     * 选一列做为数组的数据
     */
    public <T> List<T> toArray(String columnName) {
        List<T> list = new ArrayList<T>();

        for (DataItem r : this) {
            list.add((T) r.get(columnName));
        }
        return list;
    }

    /**
     * 选一列做为数组的数据
     */
    public <T> List<T> toArray(int columnIndex) {
        List<T> list = new ArrayList<T>();

        for (DataItem r : this) {
            list.add((T) r.get(columnIndex));
        }
        return list;
    }
}
