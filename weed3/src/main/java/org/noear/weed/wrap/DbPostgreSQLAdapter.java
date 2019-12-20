package org.noear.weed.wrap;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

public class DbPostgreSQLAdapter implements DbAdapter{

    @Override
    public String tableFormat(String tb) {
        return "\"" + tb + "\"";
    }

    @Override
    public String columnFormat(String col) {
        return "\"" + col + "\"";
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
