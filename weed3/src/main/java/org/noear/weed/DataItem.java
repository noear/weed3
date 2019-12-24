package org.noear.weed;

import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.EntityUtils;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by noear on 14-9-10.
 */
public class DataItem extends LinkedHashMap<String,Object> implements IDataItem {
    List<String> _keys = new ArrayList<>();

    public DataItem() {
    }

    public DataItem(Boolean isUsingDbNull) {
        _isUsingDbNull = isUsingDbNull;
    }

    @Override
    public int count() {
        return size();
    }

    @Override
    public boolean exists(String name) {
        return containsKey(name);
    }

    @Override
    public List<String> keys() {
        return _keys;
    }

    @Override
    public DataItem set(String name, Object value) {
        put(name, value);
        if (_keys.contains(name) == false) {
            _keys.add(name);
        }
        return this;
    }

    @Override
    public DataItem setIf(boolean condition, String name, Object value) {
        if (condition) {
            set(name, value);
        }
        return this;
    }

    @Override
    public Object get(int index) {
        return get(_keys.get(index));
    }

    @Override
    public Object get(String name) {
        return super.get(name);
    }

    @Override
    public Variate getVariate(String name) {
        if (containsKey(name)) {
            return new Variate(name, get(name));
        } else {
            return new Variate(name, null);
        }
    }

    @Override
    public void remove(String name) {
        super.remove(name);
        _keys.remove(name);
    }

    @Override
    public <T extends IBinder> T toItem(T item) {
        item.bind((key) -> getVariate(key));

        return item;
    }

    @Override
    public short getShort(String name) {
        return (short) get(name);
    }

    @Override
    public int getInt(String name) {
        return (int) get(name);
    }

    @Override
    public long getLong(String name) {
        return (long) get(name);
    }

    @Override
    public double getDouble(String name) {
        return (double) get(name);
    }

    @Override
    public float getFloat(String name) {
        return (float) get(name);
    }

    @Override
    public String getString(String name) {
        return (String) get(name);
    }

    @Override
    public boolean getBoolean(String name) {
        return (boolean) get(name);
    }

    @Override
    public Date getDateTime(String name) {
        return (Date) get(name);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        for (Map.Entry<String, Object> kv : entrySet()) {
            Object val = kv.getValue();

            if (val == null && _isUsingDbNull) {
                action.accept(kv.getKey(), "$NULL");
            } else {
                action.accept(kv.getKey(), val);
            }
        }
    }

    private boolean _isUsingDbNull = false;

    //============================
    public static IDataItem create(IDataItem schema, GetHandler source) {
        DataItem item = new DataItem();
        for (String key : schema.keys()) {
            Object val = source.get(key);
            if (val != null) {
                item.set(key, val);
            }
        }
        return item;
    }

    /**
     * 从map加载数据
     */
    public DataItem setMap(Map<String, Object> data) {
        data.forEach((k, v) -> {
            set(k, v);
        });

        return this;
    }

    public DataItem setMapIf(Map<String, Object> data, Fun2<Boolean, String, Object> condition) {
        data.forEach((k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });

        return this;
    }

    /**
     * 从Entity 加载数据
     */
    public DataItem setEntity(Object obj) {
        EntityUtils.fromEntity(obj, (k, v) -> {
            set(k, v);
        });
        return this;
    }

    public DataItem setEntityIf(Object obj, Fun2<Boolean, String, Object> condition) {
        EntityUtils.fromEntity(obj, (k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });
        return this;
    }

    /**
     * 获取map
     */
    public Map<String, Object> getMap() {
        return this;
    }


    /**
     * 转为Entity
     */
    public <T> T toEntity(Class<T> cls) {
        return EntityUtils.toEntity(cls, this);
    }
}
