package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbSQLiteDialect implements DbDialect {
    @Override
    public ResultSet getTables(DatabaseMetaData md, String catalog, String schema) throws SQLException {
        return md.getTables(null, null, null, new String[]{"TABLE"});
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
