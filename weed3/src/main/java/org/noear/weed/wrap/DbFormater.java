package org.noear.weed.wrap;

import org.noear.weed.DbContext;
import org.noear.weed.dialect.DbDialect;
import org.noear.weed.utils.StringUtils;

/** 提供格式处理 */
public class DbFormater{
    protected DbContext ctx;
    public DbFormater(DbContext ctx){
        this.ctx = ctx;
    }

    public DbDialect dba(){
        return ctx.dbAdapter();
    }


    public String formatSchema(String name){
        return dba().schemaFormat(name);
    }

    /**
     * 格式化字段（用于：set(..,v)）
     */
    public String formatColumn(String name) {
        if (dba().excludeFormat(name) || name.indexOf(")")>0) {
            return name;
        }

        return dba().columnFormat(name);
    }


    /**
     * 格式化多列（用于：select(..) orderBy(..) groupBy(..)）
     */
    public String formatMultipleColumns(String columns) {
        if(columns.indexOf(")")>0){
            return columns;
        }

        StringBuilder sb = StringUtils.borrowBuilder();
        //将列切分
        String[] ss = columns.split(",");

        for(int i=0,len=ss.length; i<len; i++){
            String name = ss[i].trim();

            if(name.indexOf(" ")>0){
                int idx = name.indexOf(" ");
                //类假：xxx_name name;xxx_name as name; name ASC; a.xxx_name name,DISTINCT name

                String left = name.substring(0,idx).trim();
                String left_up = left.toUpperCase();

                if("ALL".equals(left_up)
                        || "DISTINCT".equals(left_up)
                        || "DISTINCTROW".equals(left_up)
                        || "TOP".equals(left_up)){
                    sb.append(name).append(",");
                }else{
                    sb.append(format_column_do(left))
                            .append(name.substring(idx))
                            .append(",");
                }
            }else{
                sb.append(format_column_do(name)).append(",");
            }
        }

        if(sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }

        return StringUtils.releaseBuilder(sb);
    }


    private String format_column_do(String name){
        if (dba().excludeFormat(name) || name.equals("*") || name.indexOf(")") > 0) {
            return name;
        }

        return dba().columnFormat(name);
    }

    //格式化条件（用于：where() and() or()） //暂时不实现
    public String formatCondition(String condition){
        return condition;
    }

    /**
     * 格式化对象（用于：from(..), join(..)）
     */
    public String formatTable(String name) {
        if (dba().excludeFormat(name) || name.indexOf("(") > 0) {
            return name;
        }

        if (name.indexOf(" ") < 0) {
            return dba().tableFormat(name);
        } else {
            //类似：xxx_name name;xxx_name as name; name ASC;

            int idx = name.indexOf(" ");
            //schemaFormat,会保持原大小
            return dba().tableFormat(name.substring(0, idx)) + " " + dba().schemaFormat(name.substring(idx + 1));
        }
    }
}
