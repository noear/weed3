using System;
using System.Collections.Generic;
using System.Text;


namespace Noear.Weed {

    /**
     * Created by noear on 14/11/12.
     *
     * $.       //当前表空间
     * $NOW()   //说明这里是一个sql 函数
     * ?        //参数占位符
     * ?...     //数据型或子查询占位符（说明这里是一个数组或查询结果)
     */
    public abstract class DbTableQueryBase<T> where T : DbTableQueryBase<T> {

        String _table;
        DbContext _context;
        SQLBuilder _builder;

        public DbTableQueryBase(DbContext context) {
            _context = context;
            _builder = new SQLBuilder();
        }

        protected internal T table(String table) { //相当于 from
            if (table.IndexOf('.') > 0)
                _table = table;
            else
                _table = "$." + table;

            return (T)this;
        }

        //使用 ?... 支持数组参数
        public T where(String where, params Object[] args) {
            _builder.append(" WHERE ").append(where, args);
            return (T)this;
        }

        public T and(String and, params Object[] args) {
            _builder.append(" AND ").append(and, args);
            return (T)this;
        }

        public T or(String or, params Object[] args) {
            _builder.append(" OR ").append(or, args);
            return (T)this;
        }

        public T begin() {
            _builder.append(" ( ");
            return (T)this;
        }

        public T end() {
            _builder.append(" ) ");
            return (T)this;
        }

        public long insert(IDataItem data) {
            if (data == null || data.count() == 0)
                return 0;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append(" INSERT INTO ").Append(_table).Append(" (");

            foreach (var key in data.keys()) {
                sb.Append(key).Append(",");
            }

            sb.DeleteCharAt(sb.Length - 1);

            sb.Append(") VALUES (");
            data.forEach((key, value) => {
                if (value is String) {
                    String val2 = (String)value;
                    if (val2[0] == '$') { //说明是SQL函数
                        sb.Append(val2.Substring(1)).Append(",");
                    }
                    else {
                        sb.Append("?,");
                        args.Add(value);
                    }
                }
                else {
                    sb.Append("?,");
                    args.Add(value);
                }
            });

            sb.DeleteCharAt(sb.Length - 1);
            sb.Append(");");

            _builder.append(sb.ToString(), args);

            return compile().insert();
        }

        public void insertAll(List<IDataItem> rowsValue) {
            foreach (IDataItem row in rowsValue) {
                insert(row);
            }
        }

        public int update(IDataItem data) {
            if (data == null || data.count() == 0)
                return 0;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE ").Append(_table).Append(" SET ");

            data.forEach((key, value) => {
                if (value is String) {
                    String val2 = (String)value;
                    if (val2[0] == '$') {
                        sb.Append(key).Append("=").Append(val2.Substring(1)).Append(",");
                    }
                    else {
                        sb.Append(key).Append("=?,");
                        args.Add(value);
                    }
                }
                else {
                    sb.Append(key).Append("=?,");
                    args.Add(value);
                }
            });

            sb.DeleteCharAt(sb.Length - 1);

            _builder.insert(sb.ToString(), args.ToArray());

            return compile().execute();
        }

        public int delete() {
            _builder.insert("DELETE FROM " + _table);
            return compile().execute();
        }

        public T innerJoin(String table) {
            _builder.append(" INNER JOIN ").append(table);
            return (T)this;
        }

        public T leftJoin(String table) {
            _builder.append(" LEFT JOIN ").append(table);
            return (T)this;
        }

        public T on(String on) {
            _builder.append(" ON ").append(on);
            return (T)this;
        }

        public T groupBy(String groupBy) {
            _builder.append(" GROUP BY ").append(groupBy);
            return (T)this;
        }

        public T orderBy(String orderBy) {
            _builder.append(" ORDER BY ").append(orderBy);
            return (T)this;
        }

        public T limit(int start, int rows) {
            _builder.append(" LIMIT " + start + "," + rows + " ");
            return (T)this;
        }

        public T limit(int rows) {
            _builder.append(" LIMIT " + rows + " ");
            return (T)this;
        }

        public T top(int num) {
            _builder.append(" TOP " + num + " ");
            return (T)this;
        }

        public IQuery select(String columns) {

            StringBuilder sb = new StringBuilder();

            //1.构建sql
            sb.Append("SELECT ").Append(columns).Append(" FROM ").Append(_table);
            
            _builder.insert(sb.ToString());

            return compile();
        }


        //编译（成DbQuery）
        private DbQuery compile() {
            return new DbQuery(_context).sql(_builder);
        }
    }
}
