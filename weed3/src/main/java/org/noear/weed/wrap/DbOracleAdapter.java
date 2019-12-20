package org.noear.weed.wrap;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

/**
 * BETWEEN AND :: >= + <=
 * */
public class DbOracleAdapter implements DbAdapter{

    @Override
    public boolean excludeFormat(String str) {
        return str.startsWith("\"");
    }

    @Override
    public String schemaFormat(String sc) {
        return "\"" + sc + "\"";
    }

    @Override
    public String tableFormat(String tb) {
        String[] ss = tb.toUpperCase().split("\\.");

        if(ss.length > 1){
            return "\"" + ss[0] + "\".\"" + ss[0] + "\"";
        }else{
            return "\"" + ss[0] + "\"";
        }
    }

    @Override
    public String columnFormat(String col) {
        String[] ss = col.split("\\.");

        if(ss.length > 1){
            return "\"" + ss[0] + "\".\"" + ss[0] + "\"";
        }else{
            return "\"" + ss[0] + "\"";
        }
    }

    @Override
    public void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {
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
