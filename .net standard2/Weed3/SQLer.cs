using System;
using System.Data.Common;
using System.Collections.Generic;

namespace Noear.Weed {

    /**
     * Created by noear on 14-6-12.
     * 数据库执行器
     */
    internal class SQLer {

        private DbReader reader;
        private DbConnection conn;

        private void tryClose() {
            try { if (reader != null) { reader.Close(); reader = null; } } catch (Exception ex) { WeedConfig.logException(null, ex); };
            try { if (conn != null) { conn.Close(); conn = null; } } catch (Exception ex) { WeedConfig.logException(null, ex); };
        }

        
        public Variate getVariate(Command cmd, DbTran transaction) {
            try {
                reader = query(cmd, transaction);
                if (reader.Read())
                    return new Variate(null, reader[0]); //也可能从1开始
                else
                    return null;//new Variate(null, null);
            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        public T getItem<T>(T model, Command cmd, DbTran transaction) where T : class,IBinder {
            //T model = BinderMapping.getBinder<T>();
            try {
                reader = query(cmd, transaction);
                if (reader.Read()) {
                    model.bind((key) => {
                        return new Variate(key, reader[key]);
                    });

                    return model;
                }
                else
                    return null;
            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        public List<T> getList<T>(T model, Command cmd, DbTran transaction) where T : IBinder {
            List<T> list = new List<T>();
            try {
                reader = query(cmd, transaction);
                while (reader.Read()) {
                    model.bind((key) => {
                        return new Variate(key, reader[key]);
                    });

                    list.Add(model);
                    model = (T)model.clone();
                }

                if (list.Count > 0)
                    return list;
                else
                    return null;
            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        public DataItem getRow(Command cmd, DbTran transaction) {
            DataItem row = new DataItem();

            try {
                reader = query(cmd, transaction);
                if (reader.Read()) {
                    int len = reader.FieldCount;

                    for (int i = 0; i < len; i++) {
                        row.set(reader.GetName(i), reader[i]);
                    }
                }

                if (row.count() > 0)
                    return row;
                else
                    return null;

            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        public DataList getTable(Command cmd, DbTran transaction) {
            DataList table = new DataList();

            try {
                reader = query(cmd, transaction);

                while (reader.Read()) {
                    DataItem row = new DataItem();
                    int len = reader.FieldCount;

                    for (int i = 0; i < len; i++) {
                        row.set(reader.GetName(i), reader[i]);
                    }

                    table.addRow(row);
                }

                if (table.getRowCount() > 0)
                    return table;
                else
                    return null;

            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        //执行
        public int execute(Command cmd, DbTran transaction) {
            try {
                DbCommand dbcmd = null;
                if (transaction == null)
                    dbcmd = buildCMD(cmd, null, false);
                else
                    dbcmd = buildCMD(cmd, transaction.connection, false);

                return dbcmd.ExecuteNonQuery();

            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        public long insert(Command cmd, DbTran transaction) {
            try {
                DbCommand dbcmd = null;
                if (transaction == null)
                    dbcmd = buildCMD(cmd, null, true);
                else
                    dbcmd = buildCMD(cmd, transaction.connection, true);

                if (dbcmd.CommandText.IndexOf("@@IDENTITY") > 0) {
                    var obj = dbcmd.ExecuteScalar();

                    if (obj is ulong)
                        return (long)((ulong)obj);
                    else if (obj is long)
                        return (long)obj;
                    else
                        return 0;
                }
                else
                    return dbcmd.ExecuteNonQuery();

            }
            catch (Exception ex) {
                WeedConfig.logException(cmd, ex);
                throw ex;
            }
            finally {
                tryClose();
            }
        }

        //查询
        private DbReader query(Command cmd, DbTran transaction) {
            DbCommand dbcmd = null;
            if (transaction == null)
                dbcmd = buildCMD(cmd, null, false);
            else
                dbcmd = buildCMD(cmd, transaction.connection, false);
            
            //3.执行
            return new DbReader(dbcmd.ExecuteReader());//stmt.executeQuery();
        }

        private DbCommand buildCMD(Command cmd, DbConnection c, bool isInsert) {
            //0.监听
            WeedConfig.logExecute(cmd);

            //1.构建连接和命令(外部的c不能给conn)
            if (c == null) {
                c = conn = cmd.context.getConnection();
                c.Open();
            }
            
            DbCommand dbcmd = c.CreateCommand();
            dbcmd.CommandText = cmd.text;
            if (cmd.text.IndexOf(' ') < 0) //没有空隔的是存储过程 
                dbcmd.CommandType = System.Data.CommandType.StoredProcedure;
            else 
                dbcmd.CommandText = cmd.text;

            if (cmd.paramS != null) {
                //2.设置参数值
                foreach (Variate p in cmd.paramS) {
                    var pm = dbcmd.CreateParameter();
                    pm.ParameterName = p.getName();
                    pm.Value = p.getValue();
                    pm.DbType = p.getType();
                    dbcmd.Parameters.Add(pm);
                }
            }

            return dbcmd;
        }
    }
}
