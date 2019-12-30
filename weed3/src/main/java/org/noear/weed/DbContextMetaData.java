package org.noear.weed;

import org.noear.weed.wrap.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class DbContextMetaData {
    protected String _schema;
    protected String _catalog;

    private transient Map<String, TableWrap> _tables = new HashMap<>();
    private transient DbType _dbType = DbType.Unknown;
    private transient DbAdapter _dbAdapter;

    //数据源
    private transient DataSource __dataSource; //通过dataSourceSet写入
    /** 获取数据源 */
    public DataSource dataSource() {
        return __dataSource;
    }
    protected void dataSourceDoSet(DataSource ds){
        __dataSource = ds;
    }
    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return dataSource().getConnection();
    }

    //数据集名称

    public DbType dbType(){
        initMetaData();

        return _dbType;
    }

    public DbAdapter dbAdapter(){
        initMetaData();

        return _dbAdapter;
    }

    public Collection<TableWrap> dbTables(){
        initMetaData();
        return _tables.values();
    }

    public TableWrap getTableWrap(String name){
        initMetaData();

        for(Map.Entry<String,TableWrap> kv : _tables.entrySet()){
            if(name.equalsIgnoreCase(kv.getKey())){
                return kv.getValue();
            }
        }

        return null;
    }
    public String getTablePk1(String name) {
        TableWrap tw = getTableWrap(name);
        return tw == null ? null : tw.getPk1();
    }

    private void initMetaData(){
        if(_dbAdapter != null){
            return;
        }
        initMetaDataDo();
    }
    private synchronized void initMetaDataDo() {
        //这段不能去掉
        if(_dbAdapter != null){
            return;
        }

        System.out.println("Weed3::Init metadata");

        Connection conn = null;
        try {
            System.out.println("Weed3::Start testing database connectivity...");
            conn = getConnection();
            DatabaseMetaData md = conn.getMetaData();
            System.out.println("Weed3::The connection is successful");

            //1.
            setDatabaseType(md.getDatabaseProductName());

            //2.
            setSchema(conn);

            //3.
            setTables(md);

        } catch (Throwable ex) {
            System.out.println("Weed3::The connection error");
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
                _dbAdapter = new DbMySQLAdapter();
            } else if (pn.indexOf("mariadb") >= 0) {
                _dbType = DbType.MariaDB;
                _dbAdapter = new DbMySQLAdapter();
            } else if (pn.indexOf("sqlserver") >= 0) {
                _dbType = DbType.SQLServer;
                _dbAdapter = new DbSQLServerAdapter();
            } else if (pn.indexOf("oracle") >= 0) {
                _dbType = DbType.Oracle;
                _dbAdapter = new DbOracleAdapter();
            } else if (pn.indexOf("postgresql") >= 0) {
                _dbType = DbType.PostgreSQL;
                _dbAdapter = new DbPostgreSQLAdapter();
            } else if (pn.indexOf("db2") >= 0) {
                _dbType = DbType.DB2;
                _dbAdapter = new DbDb2Adapter();
            } else if (pn.indexOf("sqlite") >= 0) {
                _dbType = DbType.SQLite;
                _dbAdapter = new DbSQLiteAdapter();
            }else if (pn.indexOf("h2") >= 0) {
                _dbType = DbType.H2;
                _dbAdapter = new DbSQLiteAdapter();
            } else{
                //做为默认
                _dbAdapter = new DbMySQLAdapter();
            }
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
        } catch (Throwable e) {
            switch (_dbType) {
                case PostgreSQL:
                    _schema = "public";
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
        rs = md.getTables(_catalog, _schema, null, new String[]{"TABLE", "VIEW"});
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
                ColumnWrap cw = new ColumnWrap();
                cw.name = rs.getString("COLUMN_NAME");
                cw.type = rs.getInt("DATA_TYPE");
                cw.size = rs.getInt("COLUMN_SIZE");
                cw.remarks = rs.getString("REMARKS");
                Object o = rs.getObject("DECIMAL_DIGITS");
                if (o != null) {
                    cw.digit = ((Number) o).intValue();
                }

                tWrap.addColumn(cw);
            }
            rs.close();

            rs = md.getPrimaryKeys(_catalog,_schema,key);
            while (rs.next()) {
                String idName=rs.getString("COLUMN_NAME");
                tWrap.addPk(idName);
            }
            rs.close();
        }
    }
}
