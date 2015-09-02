using System;
using System.Collections.Generic;

namespace Noear.Weed {
    public class DataList {
        private List<DataItem> rows = null;

        private void tryInit() {
            if (rows == null)
                rows = new List<DataItem>();
        }

        public List<DataItem> getRows() {
            tryInit();

            return rows;
        }

        public DataItem getRow(int index) {
            return getRows()[index];
        }

        public void addRow(DataItem row) {
            tryInit();

            rows.Add(row);
        }

        public int getRowCount() {
            if (rows == null)
                return 0;
            else
                return rows.Count;
        }

        public void clear() {
            if (rows != null)
                rows.Clear();
        }

        //----------

        public List<T> toList<T>(T model) where T : IBinder {
            List<T> list = new List<T>();

            foreach (DataItem r in rows) {
                T item = (T)model.clone();

                item.bind((key) => {
                    return r.getVariate(key);
                });

                list.Add(item);
            }
            return list;
        }

        public List<T> toArray<T>(String column)  {
            List<T> list = new List<T>();

            foreach (DataItem r in rows) {
                list.Add((T)r.get(column));
            }
            return list;
        }
    }
}
