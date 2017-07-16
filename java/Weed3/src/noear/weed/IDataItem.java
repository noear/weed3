package noear.weed;

import noear.weed.ext.Act2;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuety on 15/9/2.
 */
public interface IDataItem extends Serializable {
    int count();
    void clear();
    boolean exists(String name);
    Iterable<String> keys();

    IDataItem set(String name, Object value);
    Object get(String name);
    Variate getVariate(String name);
    <T extends IBinder> T toItem(T item);

    short getShort(String name);
    int getInt(String name);
    long getLong(String name);
    double getDouble(String name);
    float getFloat(String name);
    String getString(String name);
    boolean getBoolean(String name);
    Date getDateTime(String name);

    void forEach(Act2<String, Object> callback);
}
