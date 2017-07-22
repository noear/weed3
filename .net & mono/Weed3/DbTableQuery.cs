using System;
using System.Collections.Generic;

namespace Noear.Weed {

    /**
     * Created by noear on 14/11/12.
     *
     * $.       //表空间占位数（即数据库名）
     * $fcn     //SQL函数占位符
     * ?        //参数占位符
     * ?...     //数组型参数占位符
     */
    public class DbTableQuery : DbTableQueryBase<DbTableQuery> {
        public DbTableQuery(DbContext context) : base(context) {
        }

        DataItem _item;
        public DbTableQuery set(string name, object value) {
            if (_item == null) {
                _item = new DataItem();
            }

            _item.set(name, value);

            return this;
        }

        public int update() {
            if (_item == null)
                return 0;
            else
                return update(_item);
        }


        public long insert() {
            if (_item == null)
                return 0;
            else
                return insert(_item);
        }
        
        //public void insertList(List<GetHandler> valuesList) {
        //    if (_item != null) {
        //        insertList(_item, valuesList);
        //    }
        //}
    }
}
