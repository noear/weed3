package org.noear.weed.dialect;

import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbH2Dialect implements DbDialect {
    //top,page 和mysql一样

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
        return md.getTables(null, null, "TABLE%", new String[]{"TABLE"});
    }

    @Override
    public String preReview(String code) {
        if (code.indexOf("CREATE TABLE") >= 0) {
            return code.replace("ENGINE=InnoDB ", "")
                    .replace("USING BTREE", "")
                    .replace("USING HASH", "")
                    .replaceAll("`\\(\\d+\\)\\)", "`)")
                    .replace("CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ", "");

        }

        if(code.indexOf("information_schema.")>=0){
            return  code.toUpperCase();
        }

        return code;
    }

    @Override
    public boolean supportsVariablePaging() {
        return true;
    }
}
