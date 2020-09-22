package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

/**
 * PostgreSQL数据库方言处理（BETWEEN AND :: >= + <=）
 *
 * @author noear
 * @since 3.2
 * */
public class DbPostgreSQLDialect implements DbDialect {
    @Override
    public boolean excludeFormat(String str) {
        return str.startsWith("\"") || str.indexOf(".") > 0;
    }

    @Override
    public String tableFormat(String tb) {
        return "\"" + tb + "\"";
    }

    @Override
    public String columnFormat(String col) {
        return "\"" + col + "\"";
    }

    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
        sqlB.insert(0, "SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }

        if (supportsVariablePaging()) {
            sqlB.append(" LIMIT ? OFFSET ?");
            sqlB.paramS.add(size);
            sqlB.paramS.add(start);
        } else {
            sqlB.append(" LIMIT ")
                    .append(size)
                    .append(" OFFSET ")
                    .append(start);
        }
    }

    //top 和mysql一样
}
