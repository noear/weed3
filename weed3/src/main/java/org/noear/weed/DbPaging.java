package org.noear.weed;

import org.noear.weed.ext.DatabaseType;

public class DbPaging {
    public static DbPaging def = new DbPaging();

    /**
     * 前缀处理
     * */
    public void preProcessing(DbTableQueryBase q, StringBuilder sb) {
        if (q.limit_top > 0) {
            if (q.databaseType() == DatabaseType.SQLServer) {
                sb.append(" TOP ").append(q.limit_top).append(" ");
            }
            return;
        }

        if (q.limit_rows > 0) {
            switch (q.databaseType()) {
                case SQLServer: {
                    if (q._orderBy == null) {
                        throw new RuntimeException("Please add orderBy");
                    }

                    sb.append(" ROW_NUMBER() OVER(ORDER BY ").append(q._orderBy).append(") AS _ROW_NUM, ");
                }
                break;

                case Oracle:{
                    if (q._orderBy == null) {
                        sb.append(" ROWNUM _ROW_NUM, ");
                    }else{
                        sb.append(" ROW_NUMBER() OVER(ORDER BY ").append(q._orderBy).append(") AS _ROW_NUM, ");
                    }
                }
                    break;
            }
            return;
        }
    }

    /**
     * 后缀处理
     * */
    public SQLBuilder postProcessing(DbTableQueryBase q, SQLBuilder sqlB){
        if (q.limit_top > 0) {
            switch (q.databaseType()){
                case SQLServer:
                    break;

                case Oracle: {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SELECT _x.* (").append(sqlB.builder).append(") _x ");
                    sb2.append(" WHERE _x._ROW_NUM<=").append(q.limit_top);

                    sqlB.builder = sb2;
                } break;

                default:
                    sqlB.append(" LIMIT ").append(q.limit_top).append(" ");
                    break;
            }
            return sqlB;
        }

        if (q.limit_rows > 0) {
            switch (q.databaseType()) {
                case SQLServer:
                case Oracle:
                case DB2:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SELECT _x.* (").append(sqlB.builder).append(") _x ");
                    sb2.append(" WHERE _x._ROW_NUM BETWEEN ")
                            .append(q.limit_start).append(" AND ")
                            .append( q.limit_start + q.limit_rows);

                    sqlB.builder = sb2;

                    break;

                case PostgreSQL:
                    sqlB.append(" LIMIT ")
                            .append(q.limit_rows)
                            .append(" OFFSET ")
                            .append(q.limit_start);
                    break;

                default:
                    sqlB.append(" LIMIT ")
                            .append(q.limit_start)
                            .append(",")
                            .append(q.limit_rows);
                    break;
            }

            return sqlB;
        }

        return sqlB;
    }
}
