package org.noear.weed;

import org.noear.weed.dialect.*;
import org.noear.weed.wrap.*;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DbContextMetaData implements Closeable {
    private String schema;
    private String catalog;
    private String productName;
    private String productVersion;
    private String url;

    private transient Map<String, TableWrap> tableAll = new HashMap<>();
    private transient DbType type = DbType.Unknown;
    private transient DbDialect dialect;

    /**
     * 获取链接字符串
     */
    public String getUrl() {
        return url;
    }

    /**
     * 获取产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 获取产品版本号
     */
    public String getProductVersion() {
        return productVersion;
    }

    //数据源
    private transient DataSource dataSource; //通过dataSourceSet写入


    /**
     * 获取数据源
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    protected void setDataSource(DataSource ds) {
        dataSource = ds;
    }

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return WeedConfig.connectionFactory.getConnection(getDataSource());
    }

    /**
     * 获取元信息链接
     */
    public Connection getMetaConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * 获取 schema
     */
    public String getSchema() {
        return schema;
    }

    protected void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 获取 catalog
     */
    public String getCatalog() {
        return catalog;
    }


    //数据集名称

    /**
     * 获取类型
     */
    public DbType getType() {
        init();
        return type;
    }

    /**
     * 获取方言
     */
    public DbDialect getDialect() {
        init();
        return dialect;
    }

    public void setDialect(DbDialect adapter) {
        init();
        dialect = adapter;
    }

    public Collection<TableWrap> getTableAll() {
        init();
        return tableAll.values();
    }

    public TableWrap getTable(String tableName) {
        init();

        for (Map.Entry<String, TableWrap> kv : tableAll.entrySet()) {
            if (tableName.equalsIgnoreCase(kv.getKey())) {
                return kv.getValue();
            }
        }

        return null;
    }

    public String getTablePk1(String tableName) {
        TableWrap tw = getTable(tableName);
        return tw == null ? null : tw.getPk1();
    }

    /**
     * 刷新
     */
    public synchronized void refresh() {
        initDo();
    }

    /**
     * 初始化
     */
    public synchronized void init() {
        if (dialect != null) {
            return;
        }
        initDo();
    }

    private void initPrintln(String x) {
        if (schema == null) {
            System.out.println("[Weed] Init: " + x);
        } else {
            System.out.println("[Weed] Init: " + x + " - " + schema);
        }
    }

    private void initDo() {
        //这段不能去掉
        initPrintln("Init metadata");

        Connection conn = null;
        try {
            initPrintln("Start testing database connectivity...");
            conn = getMetaConnection();
            DatabaseMetaData md = conn.getMetaData();


            url = md.getURL();
            productName = md.getDatabaseProductName();
            productVersion = md.getDatabaseProductVersion();

            if (dialect == null) {
                //1.
                setDatabaseType(url);

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

    private void setDatabaseType(String jdbcUrl) {
        if (jdbcUrl != null) {
            String pn = jdbcUrl.toLowerCase().replace(" ", "");

            if (pn.startsWith("jdbc:mysql:")) {
                type = DbType.MySQL;
                dialect = new DbMySQLDialect();
            } else if (pn.startsWith("jdbc:mariadb:")) {
                type = DbType.MariaDB;
                dialect = new DbMySQLDialect();
            } else if (pn.startsWith("jdbc:sqlserver:")) {
                type = DbType.SQLServer;
                dialect = new DbSQLServerDialect();
            } else if (pn.startsWith("jdbc:oracle:")) {
                type = DbType.Oracle;
                dialect = new DbOracleDialect();
            } else if (pn.startsWith("jdbc:postgresql:")) {
                type = DbType.PostgreSQL;
                dialect = new DbPostgreSQLDialect();
            } else if (pn.startsWith("jdbc:db2:")) {
                type = DbType.DB2;
                dialect = new DbDb2Dialect();
            } else if (pn.startsWith("jdbc:sqlite:")) {
                type = DbType.SQLite;
                dialect = new DbSQLiteDialect();
            } else if (pn.startsWith("jdbc:h2:")) {
                type = DbType.H2;
                dialect = new DbH2Dialect();
            } else if (pn.startsWith("jdbc:phoenix:")) {
                type = DbType.Phoenix;
                dialect = new DbPhoenixDialect();
            } else if (pn.startsWith("jdbc:clickhouse:")) {
                type = DbType.ClickHouse;
                dialect = new DbClickHouseDialect();
            } else if (pn.startsWith("jdbc:presto:")) {
                type = DbType.Presto;
                dialect = new DbPrestoDialect();
            } else {
                //做为默认
                dialect = new DbMySQLDialect();
            }
        } else {
            //默认为mysql
            //
            type = DbType.MySQL;
            dialect = new DbMySQLDialect();
        }
    }

    private void setSchema(Connection conn) throws SQLException {
        try {
            catalog = conn.getCatalog();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (schema != null) {
            return;
        }

        try {
            schema = conn.getSchema();

            if (schema == null) {
                schema = catalog;
            }

        } catch (Throwable e) {
            switch (type) {
                case PostgreSQL:
                    schema = "public";
                    break;
                case H2:
                    schema = "PUBLIC";
                    break;
                case SQLServer:
                    schema = "dbo";
                case Oracle:
                    schema = conn.getMetaData().getUserName();
                    break;
            }
        }
    }

    private void setTables(DatabaseMetaData md) throws SQLException {
        ResultSet rs = null;

        rs = getDialect().getTables(md, catalog, schema);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            String remarks = rs.getString("REMARKS");
            TableWrap tWrap = new TableWrap(name, remarks);
            tableAll.put(name, tWrap);
        }
        rs.close();

        for (String key : tableAll.keySet()) {
            TableWrap tWrap = tableAll.get(key);
            rs = md.getColumns(catalog, schema, key, "%");

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

            rs = md.getPrimaryKeys(catalog, schema, key);
            while (rs.next()) {
                String idName = rs.getString("COLUMN_NAME");
                tWrap.addPk(idName);
            }
            rs.close();
        }
    }

    @Override
    public void close() throws IOException {
        if (dataSource != null && dataSource instanceof Closeable) {
            ((Closeable) dataSource).close();
        }
    }
}
