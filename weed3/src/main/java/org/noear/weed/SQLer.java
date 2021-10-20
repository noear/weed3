package org.noear.weed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by noear on 14-6-12.
 * 数据库执行器
 */
class SQLer {

    private ResultSet rset;
    private PreparedStatement stmt;
    private Connection conn;

    private void tryClose() {
        try {
            if (rset != null) {
                rset.close();
                rset = null;
            }
        } catch (Exception ex) {
            WeedConfig.runExceptionEvent(null, ex);
        }

        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (Exception ex) {
            WeedConfig.runExceptionEvent(null, ex);
        }

        try {
            if (conn != null) {
                if (conn.getAutoCommit()) {
                    conn.close();
                }
                conn = null;
            }
        } catch (Exception ex) {
            WeedConfig.runExceptionEvent(null, ex);
        }
    }

    private Object getObject(Command cmd, String key) throws SQLException {
        return cmd.context.getDialect().preChange(rset.getObject(key));
    }

    private Object getObject(Command cmd, int idx) throws SQLException {
        return cmd.context.getDialect().preChange(rset.getObject(idx));
    }

    public Variate getVariate(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            rset = query(cmd);

            if (rset != null && rset.next())
                return new Variate(null, getObject(cmd, 1));
            else
                return null;//new Variate(null,null);
        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public <T extends IBinder> T getItem(Command cmd, T model) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            rset = query(cmd);

            if (rset != null && rset.next()) {
                model.bind((key) -> {
                    try {
                        return new Variate(key, getObject(cmd, key));
                    } catch (SQLException ex) {
                        WeedConfig.runExceptionEvent(cmd, ex);
                        return new Variate(key, null);
                    }
                });

                return model;
            } else
                return null;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public <T extends IBinder> List<T> getList(Command cmd, T model) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            List<T> list = new ArrayList<T>();

            rset = query(cmd);

            while (rset != null && rset.next()) {
                T item = (T) model.clone();

                if (WeedConfig.isDebug) {
                    if (model.getClass().isInstance(item) == false) {
                        throw new SQLException(model.getClass() + " clone error(" + item.getClass() + ")");
                    }
                }

                item.bind((key) -> {
                    try {
                        return new Variate(key, getObject(cmd, key));
                    } catch (SQLException ex) {
                        WeedConfig.runExceptionEvent(cmd, ex);
                        return new Variate(key, null);
                    }
                });

                list.add(item);
            }

            if (list.size() > 0)
                return list;
            else
                return null;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public DataItem getRow(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            DataItem row = new DataItem();

            rset = query(cmd);
            ResultSetMetaData meta = rset.getMetaData();

            if (rset != null && rset.next()) {

                int len = meta.getColumnCount();

                for (int i = 1; i <= len; i++) {
                    row.set(meta.getColumnLabel(i), getObject(cmd, i));
                }
            }

            if (row.count() > 0)
                return row;
            else
                return null;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    public DataList getTable(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            DataList table = new DataList();

            rset = query(cmd);
            ResultSetMetaData meta = rset.getMetaData();

            while (rset != null && rset.next()) {
                DataItem row = new DataItem();
                int len = meta.getColumnCount();

                for (int i = 1; i <= len; i++) {
                    row.set(meta.getColumnLabel(i), getObject(cmd, i));
                }

                table.addRow(row);
            }

            if (table.getRowCount() > 0)
                return table;
            else
                return null;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    //执行
    public int execute(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return 0;
        }

        try {
            if (false == buildCMD(cmd, false)) {
                return -1;
            }

            int rst = stmt.executeUpdate();

            //*.监听
            WeedConfig.runExecuteAftEvent(cmd);

            return rst;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    //批量执行
    public int[] executeBatch(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return null;
        }

        try {
            if (false == buildCMD0(cmd, false)) {
                return null;
            }

            for(Variate data: cmd.paramS){
                int idx = 1;
                Object[] ary = (Object[])data.value();
                //2.设置参数值
                for (Object v : ary) {
                    if (v == null) {
                        stmt.setNull(idx, Types.VARCHAR);
                    } else {
                        stmt.setObject(idx, v);
                    }
                    idx++;
                }
                stmt.addBatch();
            }


            int[] rst = stmt.executeBatch();

            //*.监听
            WeedConfig.runExecuteAftEvent(cmd);

            return rst;

        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    //插入
    public long insert(Command cmd) throws SQLException {
        if (cmd.context.isCompilationMode()) {
            return 0;
        }

        try {
            if (false == buildCMD(cmd, true)) {
                return -1;
            }

            stmt.executeUpdate();

            if (cmd.context.getDialect().supportsInsertGeneratedKey()) {
                try {
                    rset = stmt.getGeneratedKeys(); //乎略错误
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            //*.监听
            WeedConfig.runExecuteAftEvent(cmd);

            //这里，是与.execute()区别的地方
            if (rset != null && rset.next()) {
                Object tmp = getObject(cmd, 1);
                if (tmp instanceof Number) {
                    return ((Number) tmp).longValue();
                }
            }

            return 0l;
        } catch (SQLException ex) {
            WeedConfig.runExceptionEvent(cmd, ex);
            throw ex;
        } finally {
            tryClose();
        }
    }

    //查询
    private ResultSet query(Command cmd) throws SQLException {
        if (false == buildCMD(cmd, false)) {
            return null;
        }

        //3.执行
        ResultSet rst = stmt.executeQuery();

        //*.监听
        WeedConfig.runExecuteAftEvent(cmd);

        return rst;
    }

    private boolean buildCMD(Command cmd, boolean isInsert) throws SQLException {
        if (buildCMD0(cmd, isInsert) == false) {
            return false;
        }

        int idx = 1;
        //2.设置参数值
        for (Variate v : cmd.paramS) {
            if (v.getValue() == null) {
                stmt.setNull(idx, Types.VARCHAR);
            } else {
                stmt.setObject(idx, v.getValue());
            }
            idx++;
        }

        return true;
    }

    private boolean buildCMD0(Command cmd, boolean isInsert) throws SQLException {
        //*.监听
        if (WeedConfig.runExecuteBefEvent(cmd) == false) {
            return false;
        }

        //1.构建连接和命令(外部的c不能给conn)
        Connection c;
        if (cmd.tran == null) {
            c = conn = cmd.context.getConnection();
        } else {
            c = cmd.tran.getConnection(cmd.context); //事务时，conn 须为 null
        }

        if (cmd.text.indexOf("{call") >= 0)
            stmt = c.prepareCall(cmd.fullText());
        else {
            if (isInsert && cmd.context.getDialect().supportsInsertGeneratedKey())
                stmt = c.prepareStatement(cmd.fullText(), Statement.RETURN_GENERATED_KEYS);
            else
                stmt = c.prepareStatement(cmd.fullText());
        }

        WeedConfig.runExecuteStmEvent(cmd, stmt);

        return true;
    }
}
