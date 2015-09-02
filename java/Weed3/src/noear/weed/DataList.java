package noear.weed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 14-9-10.
 */
public class DataList {
    private ArrayList<DataItem> rows = null;

    private void tryInit() {
        if (rows == null)
            rows = new ArrayList<DataItem>();
    }

    public List<DataItem> getRows() {
        tryInit();

        return rows;
    }

    public DataItem getRow(int index){
        return getRows().get(index);
    }

    public void addRow(DataItem row) {
        tryInit();

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

    public <T extends IBinder>  List<T> toList(T model) {
        List<T> list = new ArrayList<T>();

        for (DataItem r : rows) {
            T item = (T) model.clone();

            item.bind((key) -> r.getVariate(key));

            list.add(item);
        }
        return list;
    }

    public <T> List<T> toArray(String column)
    {
        List<T> list = new ArrayList<T>();

        for (DataItem r : rows) {
            list.add((T)r.get(column));
        }
        return list;
    }
}
