package noear.weed;

import noear.weed.ext.Act2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuety on 14-9-10.
 */
public class DataItem implements IDataItem{
    HashMap<String,Object> _data = new HashMap<>();

    public DataItem() { }
    public DataItem(Boolean isUsingDbNull) { _isUsingDbNull = isUsingDbNull; }

    public int count(){
        return _data.size();
    }
    public void clear(){
        _data.clear();
    }
    public boolean exists(String name){
        return _data.containsKey(name);
    }
    public Iterable<String> keys(){
        return _data.keySet();
    }

    public IDataItem set(String name,Object value)
    {
        _data.put(name, value);
        return this;
    }

    public Object get(String name){
        return _data.get(name);
    }
    public Variate getVariate(String name)
    {
        if (_data.containsKey(name))
            return new Variate(name, get(name));
        else
            return new Variate(name, null);
    }

    public void remove(String name){
        _data.remove(name);
    }

    public <T extends IBinder> T toItem(T item)
    {
        item.bind((key) -> getVariate(key));

        return item;
    }

    public short getShort(String name){
        return (short)get(name);
    }

    public int getInt(String name){
        return (int)get(name);
    }

    public long getLong(String name){
        return (long)get(name);
    }

    public double getDouble(String name){
        return (double)get(name);
    }

    public float getFloat(String name){
        return (float)get(name);
    }

    public String getString(String name){
        return (String)get(name);
    }

    public boolean getBoolean(String name){
        return (boolean)get(name);
    }

    public Date getDateTime(String name){
        return (Date)get(name);
    }

    public void forEach(Act2<String, Object> callback)
    {
        for(Map.Entry<String,Object> kv : _data.entrySet()){
            Object val = kv.getValue();

            if(val == null && _isUsingDbNull){
                callback.run(kv.getKey(), "$NULL");
            }else {
                callback.run(kv.getKey(), val);
            }
        }
    }

    private boolean _isUsingDbNull=false;

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

    public String toJson(){
        _JsonWriter jw = new _JsonWriter();

        buildJson(jw);

        return jw.toJson();
    }

    protected void buildJson(_JsonWriter jw){
        jw.WriteObjectStart();
        for(String key : keys()){
            Object val = get(key);

            jw.WritePropertyName(key);

            if(val == null)
                jw.WriteNull();

            if(val instanceof String)
                jw.WriteValue((String)val);

            if(val instanceof Date)
                jw.WriteValue((Date)val);

            if(val instanceof Boolean)
                jw.WriteValue((Boolean)val);

            if(val instanceof Integer)
                jw.WriteValue((Integer)val);

            if(val instanceof Long)
                jw.WriteValue((Long)val);

            jw.WriteValue(new Variate(null, val).doubleValue(0));

        }
        jw.WriteObjectEnd();
    }
}
