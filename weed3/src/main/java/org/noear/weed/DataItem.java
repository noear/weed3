package org.noear.weed;

import org.noear.weed.ext.Act2;
import org.noear.weed.ext.Fun1;
import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.EntityUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by noear on 14-9-10.
 */
public class DataItem implements IDataItem, Iterable<Map.Entry<String,Object>>{
    Map<String,Object> _data = new LinkedHashMap<>();
    List<String> _keys = new ArrayList<>();

    public DataItem() { }
    public DataItem(Boolean isUsingDbNull) { _isUsingDbNull = isUsingDbNull; }

    @Override
    public int count(){
        return _data.size();
    }
    @Override
    public void clear(){
        _data.clear();
    }
    @Override
    public boolean exists(String name){
        return _data.containsKey(name);
    }
    @Override
    public List<String> keys(){
        return _keys;
    }

    @Override
    public IDataItem set(String name,Object value)
    {
        _data.put(name, value);
        if(_keys.contains(name) == false) {
            _keys.add(name);
        }
        return this;
    }

    @Override
    public Object get(int index){
        return get(_keys.get(index));
    }
    @Override
    public Object get(String name){
        return _data.get(name);
    }
    @Override
    public Variate getVariate(String name)
    {
        if (_data.containsKey(name)) {
            return new Variate(name, get(name));
        }
        else {
            return new Variate(name, null);
        }
    }

    @Override
    public void remove(String name){
        _data.remove(name);
        _keys.remove(name);
    }

    @Override
    public <T extends IBinder> T toItem(T item)
    {
        item.bind((key) -> getVariate(key));

        return item;
    }

    @Override
    public short getShort(String name){
        return (short)get(name);
    }

    @Override
    public int getInt(String name){
        return (int)get(name);
    }

    @Override
    public long getLong(String name){
        return (long)get(name);
    }

    @Override
    public double getDouble(String name){
        return (double)get(name);
    }

    @Override
    public float getFloat(String name){
        return (float)get(name);
    }

    @Override
    public String getString(String name){
        return (String)get(name);
    }

    @Override
    public boolean getBoolean(String name){
        return (boolean)get(name);
    }

    @Override
    public Date getDateTime(String name){
        return (Date)get(name);
    }

    @Override
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

            if(val == null) {
                jw.WriteNull();
                continue;
            }

            if(val instanceof String) {
                jw.WriteValue((String) val);
                continue;
            }

            if(val instanceof Date) {
                jw.WriteValue((Date) val);
                continue;
            }

            if(val instanceof Boolean) {
                jw.WriteValue((Boolean) val);
                continue;
            }

            if(val instanceof Integer) {
                jw.WriteValue((Integer) val);
                continue;
            }

            if(val instanceof Long) {
                jw.WriteValue((Long) val);
                continue;
            }

            double val2 = new Variate(null, val).doubleValue(0);
            jw.WriteValue(val2);

        }
        jw.WriteObjectEnd();
    }

//    private static final String TEMP_ENCODING = "ISO-8859-1";
//    private static final String DEFAULT_ENCODING = "UTF-8";

//    public String serialize() throws Exception {
//        String data = null;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream out = new ObjectOutputStream(byteStream);
//            try {
//                out.writeObject(this);
//                data = byteStream.toString(TEMP_ENCODING);//必须是ISO-8859-1
//                data = java.net.URLEncoder.encode(data, DEFAULT_ENCODING);
//
//            } finally {
//                out.close();
//            }
//        }finally {
//            byteStream.close();
//        }
//
//        return data;
//    }
//
//    public String trySerialize(){
//        try{
//            return serialize();
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }

//    public static DataItem unserialize(String data) throws Exception{
//        if(data == null){
//            return null;
//        }
//
//        DataItem item = null;
//        data = java.net.URLDecoder.decode(data, DEFAULT_ENCODING);
//        ByteArrayInputStream byteStream = new ByteArrayInputStream(data.getBytes(TEMP_ENCODING));
//        try {
//            ObjectInputStream inp = new ObjectInputStream(byteStream);
//            try {
//                item = (DataItem) inp.readObject();
//            } finally {
//                byteStream.close();
//            }
//        }finally {
//            byteStream.close();
//        }
//
//        return item;
//    }

//    public static DataItem tryUnserialize(String data){
//        try{
//            return unserialize(data);
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return _data.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<String, Object>> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<String, Object> entry : _data.entrySet()) {
            action.accept(entry);
        }
    }

    @Override
    public Spliterator<Map.Entry<String, Object>> spliterator() {
        return _data.entrySet().spliterator();
    }


    /** 从map加载数据 */
    public DataItem setMap(Map<String,Object> data){
        data.forEach((k,v)->{
            set(k,v);
        });

        return this;
    }

    public DataItem setMapIf(Map<String,Object> data, Fun2<Boolean,String,Object> condition) {
        data.forEach((k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });

        return this;
    }

    /** 从Entity 加载数据 */
    public void setEntity(Object obj)  {
        EntityUtils.fromEntity(obj,(k, v)->{
            set(k, v);
        });
    }

    public void setEntityIf(Object obj, Fun2<Boolean,String,Object> condition) {
        EntityUtils.fromEntity(obj, (k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });
    }

    /** 获取map */
    public Map<String,Object> getMap(){
        return _data;
    }


    /**
     *  从Entity 加载数据
     *
     *  可改用：setEntity
     *  */
    @Deprecated
    public void fromEntity(Object obj)  {
        EntityUtils.fromEntity(obj,(k, v)->{
            set(k, v);
        });
    }

    /** 转为Entity */
    public  <T> T toEntity(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        return EntityUtils.toEntity(cls,fields,this);
    }
}
