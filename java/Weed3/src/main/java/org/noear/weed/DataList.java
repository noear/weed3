package org.noear.weed;

import org.noear.weed.utils.EntityUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by noear on 14-9-10.
 */
public class DataList implements Serializable,Iterable<DataItem> {
    private ArrayList<DataItem> rows = new ArrayList<DataItem>();


    public List<DataItem> getRows() {

        return rows;
    }

    public DataItem getRow(int index){
        return getRows().get(index);
    }

    public void addRow(DataItem row) {

        rows.add(row);
    }

    public int getRowCount() {
        if (rows == null) {
            return 0;
        }
        else {
            return rows.size();
        }
    }

    public void clear()
    {
        if (rows != null) {
            rows.clear();
        }
    }

    public DataItem getLastRow(){
        if(getRowCount()>0){
            return rows.get(getRowCount()-1);
        }else{
            return null;
        }
    }

    //----------

    //转为map数组
    public List<Map<String,Object>> getMapList(){
        List<Map<String,Object>> list  =new ArrayList<>(getRowCount());

        for (DataItem r : rows) {
            list.add(r.getMap());
        }

        return list;
    }

    public <T extends IBinder>  List<T> toList(T model) throws SQLException{
        List<T> list = new ArrayList<T>(getRowCount());

        for (DataItem r : rows) {
            T item = (T) model.clone();

            if(WeedConfig.isDebug){
                if(model.getClass().isInstance(item)==false){
                    throw new SQLException(model.getClass()+" clone error ("+item.getClass()+")");
                }
            }

            item.bind((key) -> r.getVariate(key));

            list.add(item);
        }
        return list;
    }

    /** 将所有列转为类做为数组的数据 */
    public <T>  List<T> toEntityList(Class<T> cls) throws ReflectiveOperationException {
        List<T> list = new ArrayList<T>(getRowCount());
        Field[] fields = cls.getDeclaredFields();

        for (DataItem r : rows) {
            T item = EntityUtil.toEntity(cls,fields,r);
            list.add((T)item);
        }
        return list;
    }

    /** 选两列做为MAP的数据 */
    public <T> Map<String,Object> toMap(String keyColumn,String valColumn)
    {
        Map<String,Object> map = new HashMap<>();

        for (DataItem r : rows) {
            map.put(r.getString(keyColumn),r.get(valColumn));
        }
        return map;
    }


    /** 选一列做为数组的数据 */
    public <T> List<T> toArray(String columnName)
    {
        List<T> list = new ArrayList<T>();

        for (DataItem r : rows) {
            list.add((T)r.get(columnName));
        }
        return list;
    }

    /** 选一列做为数组的数据 */
    public <T> List<T> toArray(int columnIndex)
    {
        List<T> list = new ArrayList<T>();

        for (DataItem r : rows) {
            list.add((T)r.get(columnIndex));
        }
        return list;
    }

    public String toJson(){
        _JsonWriter jw = new _JsonWriter();

        jw.WriteArrayStart();

        for(DataItem r: rows){
            r.buildJson(jw);
        }

        jw.WriteArrayEnd();

        return jw.toJson();
    }

    @Override
    public Iterator<DataItem> iterator() {
        return rows.iterator();
    }

    @Override
    public void forEach(Consumer<? super DataItem> action) {
        rows.forEach(action);
    }

    @Override
    public Spliterator<DataItem> spliterator() {
        return rows.spliterator();
    }
}
