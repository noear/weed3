package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.GetHandler;
import org.noear.weed.IDataItem;
import org.noear.weed.SQLBuilder;
import org.noear.weed.ext.Fun1;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/10/20 created
 */
public abstract class DbDialectBase implements DbDialect {
    @Override
    public Object preChange(Object val) throws SQLException {
        return val;
    }

    @Override
    public String preReview(String code) {
        return code;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData md, String catalog, String schema) throws SQLException {
        return md.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
    }

    /**
     * 是否支持变量分页
     */
    @Override
    public boolean supportsVariablePaging() {
        return false;
    }


    @Override
    public boolean supportsInsertGeneratedKey() {
        return true;
    }

    /**
     * 排除格式化
     */
    @Override
    public boolean excludeFormat(String str) {
        return str.startsWith("`") || str.indexOf(".") > 0;
    }

    @Override
    public String schemaFormat(String name) {
        return name;
    }

    @Override
    public String tableFormat(String name) {
        return "`" + name + "`";
    }

    @Override
    public String columnFormat(String name) {
        return "`" + name + "`";
    }


    @Override
    public void buildSelectRangeCode(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
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

    @Override
    public void buildSelectTopCode(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int size) {
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


    @Override
    public void buildInsertOneCode(DbContext ctx, String table1, SQLBuilder sqlB, Fun1<Boolean, String> isSqlExpr, boolean _usingNull, IDataItem values) {
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        insertCmd(sb, table1);

        sb.append(" (");
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
                    sb.append("?,"); //充许插入null
                    args.add(null);
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
    }


    @Override
    public void insertCmd(StringBuilder sb, String table1) {
        sb.append("INSERT INTO ").append(table1).append(" ");
    }

    @Override
    public void updateCmd(StringBuilder sb, String table1) {
        sb.append("UPDATE ").append(table1).append(" SET ");
    }

    @Override
    public void deleteCmd(StringBuilder sb, String table1, boolean addFrom) {

        sb.append("DELETE ");

        if (addFrom) {
            sb.append(" FROM ").append(table1);
        } else {
            sb.append(table1);
        }

        sb.append(" ");
    }
}
