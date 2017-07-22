using System;
using System.Collections.Generic;

namespace Noear.Weed {
    public class DbTable : DbTableQueryBase<DbTable> {
        DataItemEx _item = new DataItemEx(); //会排除null数据

        public DbTable(DbContext context) : base(context) {
        }

        protected void set(String name, Func<Object> valueGetter) {
            _item.set(name, valueGetter);
        }

        //只会插入不是null的数据
        public long insert() {
            return insert(_item); 
        }

        //只会更新不是null的数据
        public int update() {
            return update(_item);
        }

        //只会插入不是null的数据
        public long insert(GetHandler source) {
            
            return insert(DataItem.create(_item,source));
        }

        //只会更新不是null的数据
        public void update(GetHandler source) {
            update(DataItem.create(_item, source));
        }

        //public void insertList<T>(List<T> valuesList, Func<T, GetHandler> hander) {
        //    List<GetHandler> list2 = new List<GetHandler>();

        //    foreach (T item in valuesList) {
        //        list2.Add(hander(item));
        //    }

        //    insertList(list2);
        //}

        //public void insertList(List<GetHandler> valuesList) {
        //    insertList(_item, valuesList);
        //}
    }
}
