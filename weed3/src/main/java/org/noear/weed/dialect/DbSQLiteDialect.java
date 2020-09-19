package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbSQLiteDialect implements DbDialect {
    @Override
    public Object preChange(Object val) throws SQLException {
        if (val instanceof Clob) {
            Clob clob = ((Clob) val);
            return clob.getSubString(1, (int) clob.length());
        } else if (val instanceof Byte) {
            return ((Byte) val).byteValue() > 0;
        } else {
            return val;
        }
    }

    @Override
    public ResultSet getTables(DatabaseMetaData md, String catalog, String schema) throws SQLException {
        return md.getTables(null, null, null, new String[]{"TABLE"});
    }

    @Override
    public String preReview(String code) {
        if (code.indexOf("CREATE TABLE") >= 0) {
            code = code.replace("ENGINE=InnoDB ", "")
                    .replace("USING BTREE", "")
                    .replace("USING HASH", "")
                    .replaceAll("`\\(\\d+\\)\\)", "`)")
                    .replace("CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ", "")
                    .replace(" int NOT NULL AUTO_INCREMENT", " INTEGER PRIMARY KEY AUTOINCREMENT")
                    .replace(" bigint NOT NULL AUTO_INCREMENT", " INTEGER PRIMARY KEY AUTOINCREMENT");

            return code.replaceAll("\\sCOMMENT\\s+'[^']*'", "");
        }

        if(code.indexOf("information_schema.")>=0){
            return  code.toUpperCase();
        }

        if(code.indexOf("TRUNCATE TABLE ") >=0){
            return code.replace("TRUNCATE TABLE ", "DELETE FROM");
        }

        return code;
    }

    @Override
    public void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
        sqlB.insert(0,"SELECT ");

        if(orderBy!=null){
            sqlB.append(orderBy);
        }

        sqlB.append(" LIMIT ")
                .append(size)
                .append(" OFFSET ")
                .append(start);
    }

    //top 和mysql一样
}
