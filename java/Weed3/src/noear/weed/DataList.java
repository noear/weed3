package noear.weed;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 14-9-10.
 */
public class DataList implements Serializable {
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
        if (rows == null)
            return 0;
        else
            return rows.size();
    }

    public void clear()
    {
        if (rows != null)
            rows.clear();
    }

    //----------

    public <T extends IBinder>  List<T> toList(T model) throws SQLException{
        List<T> list = new ArrayList<T>();

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

    public <T> List<T> toArray(String columnName)
    {
        List<T> list = new ArrayList<T>();

        for (DataItem r : rows) {
            list.add((T)r.get(columnName));
        }
        return list;
    }

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
}
