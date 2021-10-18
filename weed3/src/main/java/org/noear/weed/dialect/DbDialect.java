package org.noear.weed.dialect;

import org.noear.weed.*;
import org.noear.weed.ext.Fun1;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据库方言处理
 *
 * @author noear
 * @since 3.2
 * */
public interface DbDialect {

    default Object preChange(Object val) throws SQLException {
        return val;
    }

    default String preReview(String code) {
        return code;
    }

    default ResultSet getTableAll(DatabaseMetaData md, String catalog, String schema) throws SQLException {
        return md.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
    }

    /**
     * 是否支持变量分页
     */
    default boolean supportsVariablePaging() {
        return false;
    }

    /**
     * 排除格式化
     */
    default boolean excludeFormat(String str) {
        return str.startsWith("`") || str.indexOf(".") > 0;
    }

    default String schemaFormat(String name) {
        return name;
    }

    default String tableFormat(String name) {
        return "`" + name + "`";
    }

    default String columnFormat(String name) {
        return "`" + name + "`";
    }


    default void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
        sqlB.insert(0, "SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }

        if (supportsVariablePaging()) {
            sqlB.append(" LIMIT ?,?");
            sqlB.paramS.add(start);
            sqlB.paramS.add(size);
        } else {
            sqlB.append(" LIMIT ").append(start).append(",").append(size);
        }
    }

    default void selectTop(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int size) {
        sqlB.insert(0, "SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }

        if (supportsVariablePaging()) {
            sqlB.append(" LIMIT ?");
            sqlB.paramS.add(size);
        } else {
            sqlB.append(" LIMIT ").append(size);
        }
    }

    default String insertCmd() {
        return "INSERT INTO";
    }

    default <T extends GetHandler> boolean insertItem(DbContext ctx, String table1, SQLBuilder sqlB, Fun1<Boolean, String> isSqlExpr, boolean _usingNull, IDataItem values) {
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        sb.append(" ").append(insertCmd()).append(" ").append(table1).append(" (");
        values.forEach((key, value) -> {
            if (value == null) {
                if (_usingNull == false) {
                    return;
                }
            }

            sb.append(ctx.formater().formatColumn(key)).append(",");
        });

        sb.deleteCharAt(sb.length() - 1);

        sb.append(") ");
        sb.append("VALUES");
        sb.append("(");

        values.forEach((key, value) -> {
            if (value == null) {
                if (_usingNull) {
                    sb.append("null,"); //充许插入null
                }
            } else {
                if (value instanceof String) {
                    String val2 = (String) value;
                    if (isSqlExpr.run(val2)) { //说明是SQL函数
                        sb.append(val2.substring(1)).append(",");
                    } else {
                        sb.append("?,");
                        args.add(value);
                    }
                } else {
                    sb.append("?,");
                    args.add(value);
                }
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        sqlB.append(sb.toString(), args.toArray());

        return true;
    }

    default <T extends GetHandler> boolean insertList(DbContext ctx, String table1, SQLBuilder sqlB, Fun1<Boolean, String> isSqlExpr, IDataItem cols, Collection<T> valuesList) {
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        sb.append(" ").append(insertCmd()).append(" ").append(table1).append(" (");
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
        if (sb_len == sb.length()) {
            return false;
        }

        sb.deleteCharAt(sb.length() - 1);
        //sb.append("");

        sqlB.append(sb.toString(), args.toArray());

        return true;
    }

    default boolean insertGeneratedKey(){
        return true;
    }
}
