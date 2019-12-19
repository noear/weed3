package org.noear.weed;

import org.noear.weed.ext.DatabaseType;

/**
 * 分页组件（可以替换 def 进行更新）
 * */
public class DbPaging {
    /**
     * 前缀处理
     */
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
                        String tb = q._table.split(" ")[0].replace("$.","").trim();
                        String pk = q._context.getTablePk1(tb);

                        if(pk == null){
                            throw new RuntimeException("Please add orderBy");
                        }

                        sb.append(" ROW_NUMBER() OVER(ORDER BY ").append(pk).append(") AS _ROW_NUM, ");
                    }else{
                        sb.append(" ROW_NUMBER() OVER(ORDER BY ").append(q._orderBy).append(") AS _ROW_NUM, ");
                    }
                }
                break;

                case Oracle: {
                    if (q._orderBy == null) {
                        sb.append(" ROWNUM _ROW_NUM, ");
                    } else {
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
     */
    public SQLBuilder postProcessing(DbTableQueryBase q, SQLBuilder sqlB) {
        if (q.limit_top > 0) {
            switch (q.databaseType()) {
                case SQLServer:
                    break;

                case Oracle:
                case DB2: {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SELECT _x.* FROM (").append(sqlB.builder).append(") _x ");
                    sb2.append(" WHERE _x._ROW_NUM <= ").append(q.limit_top);

                    sqlB.builder = sb2;
                }
                break;

                case PostgreSQL:
                case MariaDB:
                case MySQL:
                case SQLite:
                default://MariaDB, MySQL,SQLite,PostgreSQL
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

                    if(q._orderBy != null) {
                        String tmp = "ORDER BY " + q._orderBy.trim();
                        int idx = sqlB.builder.lastIndexOf(tmp);
                        if (idx > 0) {
                            sqlB.builder.replace(idx, idx + tmp.length(), "");
                        }
                    }

                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SELECT _x.* FROM (").append( sqlB.builder).append(") _x ");
                    sb2.append(" WHERE _x._ROW_NUM BETWEEN ")
                            .append(q.limit_start + 1).append(" AND ")
                            .append(q.limit_start + q.limit_rows);

                    sqlB.builder = sb2;

                    break;

                case SQLite:
                case PostgreSQL:
                    sqlB.append(" LIMIT ")
                            .append(q.limit_rows)
                            .append(" OFFSET ")
                            .append(q.limit_start);
                    break;

                case MySQL:
                case MariaDB:
                default: //MariaDB, MySQL
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
