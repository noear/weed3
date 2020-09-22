package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

/**
 * DB2数据库方言处理（BETWEEN AND :: >= + <=）
 *
 * @author noear
 * @since 3.2
 * */
public class DbDb2Dialect implements DbDialect {

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
    public void selectPage( DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
        StringBuilder sb = new StringBuilder();
        if (orderBy == null) {
            sb.append("SELECT ROWNUM _ROW_NUM, ");
        } else {
            sb.append("SELECT ROW_NUMBER() OVER(").append(orderBy).append(") AS _ROW_NUM, ");
        }

        sqlB.insert(0, sb);

        //
        //_ROW_NUM,是从1开始的
        //
        StringBuilder sb2 = new StringBuilder();
        sb2.append("SELECT _x.* FROM (").append(sqlB.builder).append(") _x ");
        sb2.append(" WHERE _x._ROW_NUM BETWEEN ")
                .append(start + 1).append(" AND ")
                .append(start + size);

        sqlB.builder = sb2;
    }

    @Override
    public void selectTop(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int size) {
        sqlB.insert(0,"SELECT ");

        if(sqlB.indexOf(" WHERE ") > 0){
            sqlB.append(" AND");
        }else{
            sqlB.append(" WHERE");
        }

        sqlB.append(" ROWNUM <= ")
                .append(size);

        if(orderBy!=null){
            sqlB.append(orderBy);
        }
    }
}
