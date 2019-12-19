package org.noear.weed;

import org.noear.weed.ext.DatabaseType;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

 class DbContextBuilder {
    protected  static void  initMetaData(DbContext db) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            DatabaseMetaData md = conn.getMetaData();

            initDatabaseType(db, md.getDriverName());

        } catch (SQLException ex) {
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

    private static void initDatabaseType(DbContext db, String driverName){
        if (driverName != null) {
            String pn = driverName.toLowerCase().replace(" ", "");

            if (pn.indexOf("mysql") >= 0) {
                db._databaseType = DatabaseType.MySQL;
            } else if (pn.indexOf("mariadb") >= 0) {
                db._databaseType = DatabaseType.MariaDB;
            } else if (pn.indexOf("sqlserver") >= 0) {
                db._databaseType = DatabaseType.SQLServer;
            } else if (pn.indexOf("oracle") >= 0) {
                db._databaseType = DatabaseType.Oracle;
            } else if (pn.indexOf("postgresql") >= 0) {
                db._databaseType = DatabaseType.PostgreSQL;
            } else if (pn.indexOf("db2") >= 0) {
                db._databaseType = DatabaseType.DB2;
            }else if (pn.indexOf("sqlite") >= 0) {
                db._databaseType = DatabaseType.SQLite;
            }

            if (db._databaseType == DatabaseType.MySQL || db._databaseType == DatabaseType.MariaDB) {
                db.formater().fieldFormatSet("`%`");
                db.formater().objectFormatSet("`%`");
            } else {
                //SQLServer, PostgreSQL, DB2
                db.formater().fieldFormatSet("\"%\"");
                db.formater().objectFormatSet("\"%\"");
            }
        }
    }
}
