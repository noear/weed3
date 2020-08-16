package org.noear.weed.wrap;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

public class DbSQLiteDialect implements DbDialect {
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
