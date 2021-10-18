package org.noear.weed.dialect;

import org.noear.weed.DbContext;
import org.noear.weed.SQLBuilder;

import java.sql.Clob;
import java.sql.SQLException;

/**
 * Oracle数据库方言处理（BETWEEN AND :: >= + <=）
 *
 * @author noear
 * @since 3.2
 * */
public class DbOracleDialect implements DbDialect {

    @Override
    public Object preChange(Object val) throws SQLException {
        if(val instanceof Clob){
            Clob clob = ((Clob) val);
            return clob.getSubString(1,(int)clob.length());
        } else{
            return val;
        }
    }

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
                return "\"" + ss[0].toUpperCase() + "\".*";
            }else {
                return "\"" + ss[0] + "\".\"" + ss[1].toUpperCase() + "\"";
            }
        }else{
            return "\"" + ss[0].toUpperCase() + "\"";
        }
    }

    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public void selectPage(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int start, int size) {

        sqlB.insert(0, "SELECT t.* FROM (SELECT ROWNUM WD3_ROW_NUM,x.* FROM (SELECT ");

        if (orderBy != null) {
            sqlB.append(orderBy);
        }

        if (supportsVariablePaging()) {
            sqlB.append(") x  WHERE ROWNUM<=?");
            sqlB.append(") t WHERE t.WD3_ROW_NUM >?");
            sqlB.paramS.add(start + size);
            sqlB.paramS.add(start);
        } else {
            sqlB.append(") x  WHERE ROWNUM<=").append(start + size);
            sqlB.append(") t WHERE t.WD3_ROW_NUM >").append(start);
        }
    }

    @Override
    public void selectTop(DbContext ctx, String table1, SQLBuilder sqlB, StringBuilder orderBy, int size) {
        sqlB.insert(0,"SELECT ");

        if(sqlB.indexOf(" WHERE ") > 0){
            sqlB.append(" AND");
        }else{
            sqlB.append(" WHERE");
        }

        if(supportsVariablePaging()) {
            sqlB.append(" ROWNUM <= ?");
            sqlB.paramS.add(size);
        }else{
            sqlB.append(" ROWNUM <= ").append(size);
        }

        if(orderBy!=null){
            sqlB.append(orderBy);
        }
    }
}
