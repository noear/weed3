package org.noear.weed.wrap;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

public interface DbAdapter {

    default boolean excludeFormat(String str) {
        return str.startsWith("`") || str.indexOf(".") > 0;
    }

    default String schemaFormat(String sc){ return sc;}
    default String tableFormat(String tb){
        return "`" + tb + "`";
    }
    default String columnFormat(String col){
        return "`" + col + "`";
    }

    default void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size){
        sqlB.insert(0,"SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }
        sqlB.append(" LIMIT ").append(start).append(",").append(size);
    }

    default void selectTop(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int size){
        sqlB.insert(0,"SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }
        sqlB.append(" LIMIT ").append(size);
    }
}
