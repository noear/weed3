package noear.weed;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by noear on 14-6-12.
 * 数据库执行器
 */
public class SQLer {

    private ResultSet rset;
    private PreparedStatement stmt;
    private Connection conn;

    private  void tryClose()
    {
        try { if (rset != null){ rset.close(); rset=null;}} catch (Exception ex) {
            WeedLog.logException(null, ex);};
        try { if (stmt != null){ stmt.close(); stmt=null;}} catch (Exception ex) {
            WeedLog.logException(null, ex);};
        try { if (conn != null){ conn.close(); conn=null;}} catch (Exception ex) {
            WeedLog.logException(null, ex);};
    }

    private Object getDefault(DbType type)
    {
        if(type == DbType.String || type == type.DateTime)
            return null;
        else if(type == DbType.Int64)
            return 0L;
        else if(type == DbType.Double || type == DbType.Single)
            return 0.0;
        else if(type == DbType.Boolean)
            return false;
        else
            return 0;
    }

    public <T> T getValue(T def,DbCommand cmd,DbTran transaction) throws SQLException {
        Object temp = getObject(cmd, transaction);

        if (temp == null)
            return def;
        else {
            if (temp.getClass() == BigInteger.class) {
                BigInteger t2 = (BigInteger) temp;
                return (T) new Long(t2.longValue());
            } else {
                return (T) temp;
            }
        }
    }

    public Object getObject(DbCommand cmd,DbTran transaction) throws SQLException{
        try {
            rset = query(cmd,transaction);

            if (rset.next())
                return rset.getObject(1);
            else
                return null;
        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public <T extends IBinder> T getItem(DbCommand cmd, DbTran transaction,T model) throws SQLException
    {
        try {
            rset = query(cmd,transaction);

            if(rset.next()) {
                model.bind((key)->{
                    try {
                        return new Variate(key,rset.getObject(key));
                    }catch (SQLException ex){
                        WeedLog.logException(cmd, ex);
                        return new Variate(key,null);
                    }
                });

                return model;
            }
            else
                return model;
        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        }
        finally {
            tryClose();
        }
    }

    public <T extends IBinder> List<T> getList(DbCommand cmd, DbTran transaction,T model) throws SQLException
    {
        List<T> list = new ArrayList<T>();
        try {
            rset = query(cmd,transaction);

            while (rset.next()) {
                T item = (T)model.clone();

                item.bind((key)->{
                    try {
                        return new Variate(key,rset.getObject(key));
                    }catch (SQLException ex){
                        WeedLog.logException(cmd, ex);
                        return new Variate(key,null);
                    }
                });

                list.add(item);
            }
            return list;

        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        }
        finally {
            tryClose();
        }
    }

    public DataItem getRow(DbCommand cmd,DbTran transaction) throws SQLException
    {
        DataItem row = new DataItem();

        try {
            rset = query(cmd,transaction);
            ResultSetMetaData meta = rset.getMetaData();

            if (rset.next()) {

                int len = meta.getColumnCount();

                for(int i=1;i<=len;i++) {
                    row.set(meta.getColumnName(i), rset.getObject(i));
                }
            }

            return row;

        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        }
        finally {
            tryClose();
        }
    }

    public DataList getTable(DbCommand cmd,DbTran transaction) throws SQLException
    {
        DataList table = new DataList();

        try {
            rset = query(cmd,transaction);
            ResultSetMetaData meta = rset.getMetaData();

            while (rset.next()) {
                DataItem row = new DataItem();
                int len = meta.getColumnCount();

                for(int i=1;i<=len;i++) {
                    row.set(meta.getColumnName(i), rset.getObject(i));
                }

                table.addRow(row);
            }
            return table;

        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        }
        finally {
            tryClose();
        }
    }

    //执行
    public int execute(DbCommand cmd,DbTran transaction)  throws SQLException {
        try {
            if (transaction == null)
                buildCMD(cmd, null, false);
            else
                buildCMD(cmd, transaction.connection, false);

            return stmt.executeUpdate();

        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public long insert(DbCommand cmd,DbTran transaction)  throws SQLException {
        try {
            if (transaction == null)
                buildCMD(cmd, null, true);
            else
                buildCMD(cmd, transaction.connection, true);

            stmt.executeUpdate();

            rset = stmt.getGeneratedKeys();
            if (rset.next())
                return rset.getLong(1);//从1开始
            else
                return 0l;

        } catch (SQLException ex) {
            WeedLog.logException(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    //查询
    private ResultSet query(DbCommand cmd, DbTran transaction) throws SQLException {
        if (transaction == null)
            buildCMD(cmd, null, false);
        else
            buildCMD(cmd, transaction.connection, false);

        //3.执行
        return stmt.executeQuery();
    }

    private void buildCMD(DbCommand cmd, Connection c, boolean isInsert) throws SQLException {
        //1.构建连接和命令(外部的c不能给conn)
        if (c == null)
            c = conn = cmd.context.getConnection();

        if (cmd.text.indexOf("{call")>=0)
            stmt = c.prepareCall(cmd.text);
        else {
            if(isInsert)
                stmt = c.prepareStatement(cmd.text, Statement.RETURN_GENERATED_KEYS);
            else
                stmt = c.prepareStatement(cmd.text);
        }

        int idx = 1;
        //2.设置参数值
        for (Variate p : cmd.paramS) {
            stmt.setObject(idx, p.getValue());
            idx++;
        }
    }
}
