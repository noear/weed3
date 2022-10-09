package org.noear.weed;

import org.noear.weed.wrap.ClassWrap;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by noear on 14-9-10.
 *
 * getRow,addRow,getRowCount 是为跨平台设计的接口，不能去掉
 * 不能转为继承自List
 * 否则，嵌入别的引擎时，会变转为不可知的ListAdapter，让扩展的方法失效
 */
public class DataList implements Serializable,Iterable<DataItem> {
    ArrayList<DataItem> _rows = new ArrayList<>();

    public List<DataItem> getRows() {
        return _rows;
    }

    public DataItem getRow(int index) {
        return getRows().get(index);
    }

    public void addRow(DataItem row) {

        _rows.add(row);
    }

    public int getRowCount() {
        return _rows.size();
    }

    public void clear() {
        _rows.clear();
    }

    public DataItem getLastRow() {
        if (getRowCount() > 0) {
            return _rows.get(getRowCount() - 1);
        } else {
            return null;
        }
    }

    //----------

    //转为map数组
    public List<Map<String, Object>> getMapList() {
        List<Map<String, Object>> list = new ArrayList<>(getRowCount());

        for (DataItem r : _rows) {
            list.add(r.getMap());
        }

        return list;
    }

    /**
     * 将所有列转为类做为数组的数据（类为：IBinder 子类）
     */
    @Deprecated
    public <T extends IBinder> List<T> toList(T model)  {
        List<T> list = new ArrayList<T>(getRowCount());

        for (DataItem r : _rows) {
            T item = (T) model.clone();

            if (WeedConfig.isDebug) {
                if (model.getClass().isInstance(item) == false) {
                    throw new IllegalArgumentException(model.getClass() + " clone error (" + item.getClass() + ")");
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
        ClassWrap classWrap = ClassWrap.get(clz);
        List<T> list = new ArrayList<T>(getRowCount());

        if (IBinder.class.isAssignableFrom(clz)) {
            IBinder mod = classWrap.newInstance();

            for (DataItem r : _rows) {
                IBinder item = mod.clone();
                item.bind(key -> r.getVariate(key));
                list.add((T) item);
            }
        } else {
            for (DataItem r : _rows) {
                T item = classWrap.toEntity(r);
                list.add((T) item);
            }

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

        if (valColumn == null || valColumn.length() == 0) {
            for (DataItem r : _rows) {
                map.put(r.get(keyColumn).toString(), r);
            }
        } else {
            for (DataItem r : _rows) {
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

        for (DataItem r : _rows) {
            set.add((T) r.get(column));
        }
        return set;
    }

    public <T> Set<T> toSet(int columnIndex) {
        Set<T> set = new HashSet<>();

        for (DataItem r : _rows) {
            set.add((T) r.get(columnIndex));
        }
        return set;
    }


    /**
     * 选一列做为数组的数据
     */
    public <T> List<T> toArray(String columnName) {
        List<T> list = new ArrayList<T>();

        for (DataItem r : _rows) {
            list.add((T) r.get(columnName));
        }
        return list;
    }

    /**
     * 选一列做为数组的数据
     */
    public <T> List<T> toArray(int columnIndex) {
        List<T> list = new ArrayList<T>();

        for (DataItem r : _rows) {
            list.add((T) r.get(columnIndex));
        }
        return list;
    }

    @Override
    public Iterator<DataItem> iterator() {
        return _rows.iterator();
    }

    @Override
    public void forEach(Consumer<? super DataItem> action) {
        _rows.forEach(action);
    }

    @Override
    public Spliterator<DataItem> spliterator() {
        return _rows.spliterator();
    }
}
