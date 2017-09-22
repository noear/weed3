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
        bool _isLog = false;

        public DbTableQueryBase(DbContext context) {
            _context = context;
            _builder = new SQLBuilder();
        }

        public T log(Boolean isLog) {
            _isLog = isLog;
            return (T)this;
        }

        public T expre(Action<T> action) {
            action((T)this);
            return (T)this;
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

        public T from(String table) {
            _builder.append(" FROM ").append(table);
            return (T)this;
        }

        public long insert(IDataItem data) {
            if (data == null || data.count() == 0)
                return 0;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append(" INSERT INTO ").Append(_table).Append(" (");

            data.forEach((key, value) => {
                if (value == null)
                    return;

                sb.Append(_context.field(key)).Append(",");
            });

            sb.DeleteCharAt(sb.Length - 1);

            sb.Append(") VALUES (");
            data.forEach((key, value) => {
                if (value == null)
                    return;

                if (value is String) {
                    String val2 = (String)value;
                    if (val2.Length>0 && val2[0] == '$') { //说明是SQL函数
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
            sb.Append("); SELECT @@IDENTITY;");

            _builder.append(sb.ToString(), args.ToArray());

            return compile().insert();
        }

        public bool insertList<X>(List<X> valuesList, Action<X, DataItem> hander) {
            List<DataItem> list2 = new List<DataItem>();

            foreach (X values in valuesList) {
                DataItem item = new DataItem();
                hander(values, item);

                list2.Add(item);
            }

            if (list2.Count > 0) {
                return insertList(list2);
            } else {
                return false;
            }
        }

        public bool insertList(List<DataItem> valuesList) {
            if (valuesList == null || valuesList.Count == 0)
                return false;

            List<GetHandler> list2 = new List<GetHandler>();
            foreach (IDataItem values in valuesList) {
                list2.Add(values.get);
            }

            return insertList(valuesList[0], list2);
        }

        protected bool insertList(IDataItem cols, List<GetHandler> valuesList) {
            if (valuesList == null || valuesList.Count == 0)
                return false;

            if (cols == null || cols.count() == 0)
                return false;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append(" INSERT INTO ").Append(_table).Append(" (");
            foreach (String key in cols.keys()) {
                sb.Append(_context.field(key)).Append(",");
            }
            sb.DeleteCharAt(sb.Length - 1);

            sb.Append(") ");

            sb.Append("VALUES");

            foreach (GetHandler item in valuesList) {
                sb.Append("(");

                foreach (String key in cols.keys()) {
                    Object val = item(key);

                    if (val is String) {
                        String val2 = (String)val;
                        if (val2.IndexOf('$') == 0) { //说明是SQL函数
                            sb.Append(val2.Substring(1)).Append(",");
                        } else {
                            sb.Append("?,");
                            args.Add(val);
                        }
                    } else {
                        sb.Append("?,");
                        args.Add(val);
                    }
                }
                sb.DeleteCharAt(sb.Length - 1);
                sb.Append("),");
            }

            sb.DeleteCharAt(sb.Length - 1);
            sb.Append(";");

            _builder.append(sb.ToString(), args.ToArray());

            return compile().execute() > 0;
        }


        public long insert(Action<IDataItem> fun) {
            DataItem item = new DataItem();
            fun(item);

            return insert(item);
        }

        public int update(Action<IDataItem> fun) {
            DataItem item = new DataItem();
            fun(item);

            return update(item);
        }


        public int update(IDataItem data) {
            if (data == null || data.count() == 0)
                return 0;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE ").Append(_table).Append(" SET ");

            data.forEach((key, value) => {
                if (value == null)
                    return;

                if (value is String) {
                    String val2 = (String)value;
                    if (val2.Length >0 && val2[0] == '$') {
                        sb.Append(_context.field(key)).Append("=").Append(val2.Substring(1)).Append(",");
                    }
                    else {
                        sb.Append(_context.field(key)).Append("=?,");
                        args.Add(value);
                    }
                }
                else {
                    sb.Append(_context.field(key)).Append("=?,");
                    args.Add(value);
                }
            });

            sb.DeleteCharAt(sb.Length - 1);

            _builder.insert(sb.ToString(), args.ToArray());

            return compile().execute();
        }

        public bool updateList<X>(String pk, List<X> valuesList, Action<X, DataItem> hander) {
            List<DataItem> list2 = new List<DataItem>();

            foreach (X values in valuesList) {
                DataItem item = new DataItem();
                hander(values, item);

                list2.Add(item);
            }

            if (list2.Count > 0) {
                return updateList(pk, list2);
            } else {
                return false;
            }
        }

        public bool updateList(String pk, List<DataItem> valuesList) {
            if (valuesList == null || valuesList.Count == 0)
                return false;

            List<GetHandler> list2 = new List<GetHandler>();
            foreach (IDataItem values in valuesList) {
                list2.Add(values.get);
            }

            return updateList(pk, valuesList[0], list2);
        }

        protected bool updateList(String pk, IDataItem cols, List<GetHandler> valuesList) {
            if (valuesList == null || valuesList.Count == 0)
                return false;

            if (cols == null || cols.count() == 0)
                return false;

            List<Object> args = new List<Object>();
            StringBuilder sb = new StringBuilder();

            sb.Append(" INSERT INTO ").Append(_table).Append(" (");
            foreach (String key in cols.keys()) {
                sb.Append(_context.field(key)).Append(",");
            }
            sb.Remove(sb.Length - 1, 1);

            sb.Append(") ");

            sb.Append("VALUES");

            foreach (GetHandler item in valuesList) {
                sb.Append("(");

                foreach (String key in cols.keys()) {
                    Object val = item(key);

                    if (val is String) {
                        String val2 = (String)val;
                        if (val2.IndexOf('$') == 0) { //说明是SQL函数
                            sb.Append(val2.Substring(1)).Append(",");
                        } else {
                            sb.Append("?,");
                            args.Add(val);
                        }
                    } else {
                        sb.Append("?,");
                        args.Add(val);
                    }
                }
                sb.Remove(sb.Length - 1, 1);
                sb.Append("),");
            }

            sb.Remove(sb.Length - 1, 1);
            sb.Append(" ON DUPLICATE KEY UPDATE");
            foreach (String key in cols.keys()) {
                if (pk.Equals(key))
                    continue;

                sb.Append(" ").Append(key).Append("=VALUES(").Append(key).Append("),");
            }
            sb.Remove(sb.Length - 1, 1);
            sb.Append(";");

            _builder.append(sb.ToString(), args.ToArray());

            return compile().execute() > 0;
        }

        public int delete() {
            StringBuilder sb = new StringBuilder();

            sb.Append("DELETE ");
            
            if (_builder.indexOf(" FROM ") < 0) {
                sb.Append(" FROM ").Append(_table);
            } else {
                sb.Append(_table);
            }

            _builder.insert(sb.ToString());

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

        private int _top;
        public T top(int num) {
            _top = num;
            //_builder.append(" TOP " + num + " ");
            return (T)this;
        }

        public bool exists() {
            return exists(null);
        }

        public bool exists(Action<IQuery> expre)  {
            IQuery q = limit(1).select("1");

            expre?.Invoke(q);

            return q.getValue() != null;
        }

        String _hint = null;
        public T hint(String hint) {
            _hint = hint;
            return (T)this;
        }

        public long count()  {
        return count("COUNT(*)");
    }

        public long count(String expr) {
            return select(expr).getVariate().longValue(0);
        }

        public IQuery select(String columns) {

            StringBuilder sb = new StringBuilder();

            //1.构建sql
            sb.Append("SELECT ");

            if (_top > 0) {
                sb.Append(" TOP ").Append(_top).Append(" ");
            }

            sb.Append(columns).Append(" FROM ").Append(_table);


            _builder.backup();
            _builder.insert(sb.ToString());

            var rst = compile();

            _builder.restore();

            return rst;
        }


        protected DbTran _tran = null;
        public DbTableQueryBase<T> tran(DbTran transaction) {
            _tran = transaction;
            return (T)this;
        }

        public DbTableQueryBase<T> tran() {
            _tran = _context.tran();
            return (T)this;
        }

        //编译（成DbQuery）
        private DbQuery compile() {
            var temp = new DbQuery(_context).sql(_builder);

            _builder.clear();

            if (_tran != null)
                temp.tran(_tran);

            return temp.onCommandBuilt((cmd)=>{
                cmd.tag = _table;
                cmd.isLog = _isLog;
            });
        }
    }
}
