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
        String[] ss = tb.split("\\.");

        if(ss.length > 1){
            return "\"" + ss[0] + "\".\"" + ss[1].toUpperCase() + "\"";
        }else{
            return "\"" + ss[0].toUpperCase() + "\"";
        }
    }

    @Override
    public String columnFormat(String col) {
        String[] ss = col.split("\\.");

        if(ss.length > 1){
            if("*".equals(ss[1])){
                return "\"" + ss[0] + "\".*";
            }else {
                return "\"" + ss[0] + "\".\"" + ss[1] + "\"";
            }
        }else{
            return "\"" + ss[0] + "\"";
        }
    }

    @Override
    public void removeTablePrefix(SQLBuilder sqlB) {
        if (sqlB.indexOf("\"t0\".") > 0) {
            String tmp = sqlB.toString().replace("\"t0\".", "");
            sqlB.builder.setLength(0);
            sqlB.append(tmp);
        }
    }

    @Override
    public void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {

        sqlB.insert(0, "SELECT t.* FROM (SELECT ROWNUM WD3_ROW_NUM,x.* FROM (SELECT ");

        if(orderBy != null){
            sqlB.append(orderBy);
        }

        sqlB.append(") x  WHERE ROWNUM<=").append(start + size);
        sqlB.append(") t WHERE t.WD3_ROW_NUM >").append(start);
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
