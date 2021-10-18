package org.noear.weed;

import org.noear.weed.dialect.*;
import org.noear.weed.wrap.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DbContextMetaData {
    private String _schema;
    private String _catalog;

    private transient Map<String, TableWrap> _tables = new HashMap<>();
    private transient DbType _dbType = DbType.Unknown;
    private transient DbDialect _dbDialect;

    //数据源
    private transient DataSource __dataSource; //通过dataSourceSet写入


    /**
     * 获取数据源
     */
    public DataSource dataSource() {
        return __dataSource;
    }

    protected void dataSourceDoSet(DataSource ds) {
        __dataSource = ds;
    }

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return WeedConfig.connectionFactory.getConnection(dataSource());
    }

    /**
     * 获取元信息链接
     */
    public Connection getMetaConnection() throws SQLException {
        return dataSource().getConnection();
    }

    /**
     * 获取 schema
     */
    public String schema() {
        return _schema;
    }

    protected void schemaSet(String schema) {
        _schema = schema;
    }

    /**
     * 获取 catalog
     */
    public String catalog() {
        return _catalog;
    }


    //数据集名称

    /**
     * 获取类型
     */
    public DbType type() {
        init();
        return _dbType;
    }

    /**
     * 获取方言
     */
    public DbDialect dialect() {
        init();
        return _dbDialect;
    }

    public void dialectSet(DbDialect adapter) {
        init();
        _dbDialect = adapter;
    }

    public Collection<TableWrap> tables() {
        init();
        return _tables.values();
    }

    public TableWrap table(String tableName) {
        init();

        for (Map.Entry<String, TableWrap> kv : _tables.entrySet()) {
            if (tableName.equalsIgnoreCase(kv.getKey())) {
                return kv.getValue();
            }
        }

        return null;
    }

    public String tablePk1(String tableName) {
        TableWrap tw = table(tableName);
        return tw == null ? null : tw.getPk1();
    }

    public void refresh() {
        initDo();
    }

    public synchronized void init() {
        if (_dbDialect != null) {
            return;
        }
        initDo();
    }

    private void initPrintln(String x) {
        if (_schema == null) {
            System.out.println("[Weed] Init: " + x);
        } else {
            System.out.println("[Weed] Init: " + x + " - " + _schema);
        }
    }

    private synchronized void initDo() {
        //这段不能去掉
        initPrintln("Init metadata");

        Connection conn = null;
        try {
            initPrintln("Start testing database connectivity...");
            conn = getMetaConnection();
            DatabaseMetaData md = conn.getMetaData();


            if (_dbDialect == null) {
                //1.
                setDatabaseType(md.getDatabaseProductName());

                //2.
                setSchema(conn);
            }

            initPrintln("The connection is successful");

            //3.
            setTables(md);

        } catch (Throwable ex) {
            initPrintln("The connection error");
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void setDatabaseType(String driverName) {
        if (driverName != null) {
            String pn = driverName.toLowerCase().replace(" ", "");

            if (pn.indexOf("mysql") >= 0) {
                _dbType = DbType.MySQL;
                _dbDialect = new DbMySQLDialect();
            } else if (pn.indexOf("mariadb") >= 0) {
                _dbType = DbType.MariaDB;
                _dbDialect = new DbMySQLDialect();
            } else if (pn.indexOf("sqlserver") >= 0) {
                _dbType = DbType.SQLServer;
                _dbDialect = new DbSQLServerDialect();
            } else if (pn.indexOf("oracle") >= 0) {
                _dbType = DbType.Oracle;
                _dbDialect = new DbOracleDialect();
            } else if (pn.indexOf("postgresql") >= 0) {
                _dbType = DbType.PostgreSQL;
                _dbDialect = new DbPostgreSQLDialect();
            } else if (pn.indexOf("db2") >= 0) {
                _dbType = DbType.DB2;
                _dbDialect = new DbDb2Dialect();
            } else if (pn.indexOf("sqlite") >= 0) {
                _dbType = DbType.SQLite;
                _dbDialect = new DbSQLiteDialect();
            } else if (pn.indexOf("h2") >= 0) {
                _dbType = DbType.H2;
                _dbDialect = new DbH2Dialect();
            } else if (pn.indexOf("phoenix") >= 0) {
                _dbType = DbType.Phoenix;
                _dbDialect = new DbPhoenixDialect();
            } else {
                //做为默认
                _dbDialect = new DbMySQLDialect();
            }
        } else {
            //默认为mysql
            //
            _dbType = DbType.MySQL;
            _dbDialect = new DbMySQLDialect();
        }
    }

    private void setSchema(Connection conn) throws SQLException {
        try {
            _catalog = conn.getCatalog();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (_schema != null) {
            return;
        }

        try {
            _schema = conn.getSchema();

            if (_schema == null) {
                _schema = _catalog;
            }

        } catch (Throwable e) {
            switch (_dbType) {
                case PostgreSQL:
                    _schema = "public";
                    break;
                case H2:
                    _schema = "PUBLIC";
                    break;
                case SQLServer:
                    _schema = "dbo";
                case Oracle:
                    _schema = conn.getMetaData().getUserName();
                    break;
            }
        }
    }

    private void setTables(DatabaseMetaData md) throws SQLException {
        ResultSet rs = null;

        rs = dialect().getTables(md, _catalog, _schema);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            String remarks = rs.getString("REMARKS");
            TableWrap tWrap = new TableWrap(name, remarks);
            _tables.put(name, tWrap);
        }
        rs.close();

        for (String key : _tables.keySet()) {
            TableWrap tWrap = _tables.get(key);
            rs = md.getColumns(_catalog, _schema, key, "%");

            while (rs.next()) {
                int digit = 0;
                Object o = rs.getObject("DECIMAL_DIGITS");
                if (o != null) {
                    digit = ((Number) o).intValue();
                }

                ColumnWrap cw = new ColumnWrap(
                        rs.getString("COLUMN_NAME"),
                        rs.getInt("DATA_TYPE"),
                        rs.getInt("COLUMN_SIZE"),
                        digit,
                        rs.getString("REMARKS")
                );

                tWrap.addColumn(cw);
            }
            rs.close();

            rs = md.getPrimaryKeys(_catalog, _schema, key);
            while (rs.next()) {
                String idName = rs.getString("COLUMN_NAME");
                tWrap.addPk(idName);
            }
            rs.close();
        }
    }
}
