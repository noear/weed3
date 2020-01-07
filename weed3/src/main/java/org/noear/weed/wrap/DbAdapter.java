package org.noear.weed.wrap;

import org.noear.weed.*;
import org.noear.weed.ext.Fun1;
import org.noear.weed.utils.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface DbAdapter {

    default Object preChange(Object val) throws SQLException {return val;}
    default String preReview(String code){return code;}

    default ResultSet getTables(DatabaseMetaData md, String catalog, String schema) throws SQLException{ return md.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"}); }

    default boolean excludeFormat(String str) {
        return str.startsWith("`") || str.indexOf(".") > 0;
    }

    default String schemaFormat(String name){ return name;}
    default String tableFormat(String name){
        return "`" + name + "`";
    }
    default String columnFormat(String name){
        return "`" + name + "`";
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

    default <T extends GetHandler> boolean insertList(DbContext ctx, String table1, SQLBuilder sqlB, Fun1<Boolean,String> isSqlExpr, IDataItem cols, Collection<T> valuesList){
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append(" INSERT INTO ").append(table1).append(" (");
        for (String key : cols.keys()) {
            sb.append(ctx.formater().formatColumn(key)).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(") ");

        sb.append("VALUES");

        //记录当前长度用于后面比较
        int sb_len = sb.length();

        for (GetHandler item : valuesList) {
            sb.append("(");

            for (String key : cols.keys()) {
                Object val = item.get(key);

                if (val == null) {
                    sb.append("null,");
                } else {
                    if (val instanceof String) {
                        String val2 = (String) val;
                        if (isSqlExpr.run(val2)) { //说明是SQL函数
                            sb.append(val2.substring(1)).append(",");
                        } else {
                            sb.append("?,");
                            args.add(val);
                        }
                    } else {
                        sb.append("?,");
                        args.add(val);
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }

        //如果长度没有增加，说明没有数据
        if(sb_len == sb.length()){
            return false;
        }

        sb.deleteCharAt(sb.length() - 1);
        //sb.append("");

        sqlB.append(StringUtils.releaseBuilder(sb), args.toArray());

        return true;
    }
}
