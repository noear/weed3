package noear.weed;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by yuety on 14-9-10.
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
        String fn = null;

        for (DataItem r : rows) {
            T item = cls.newInstance();

            for(Field f : fields){
                fn = f.getName();

                if(r.exists(fn)){
                    f.set(item, r.get(fn));
                }
            }

            list.add((T)item);
        }
        return list;
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
