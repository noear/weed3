package org.noear.weed;

import org.noear.weed.utils.StringUtils;

/** 提供格式处理 */
public class DbFormater implements IDbFormater{
    //字段格式符
    protected String _fieldFormat;
    protected String _fieldFormat_start;
    //对象格式符
    protected String _objectFormat;
    protected String _objectFormat_start;

    /**
     * 字段格式符设置
     */
    public void fieldFormatSet(String format) {
        _fieldFormat = format;
        if (format != null && format.length() > 1) {
            _fieldFormat_start = format.substring(0, 1);
        } else {
            _fieldFormat_start = "";
        }
    }

    /**
     * 对象格式符设置
     */
    public void objectFormatSet(String format) {
        _objectFormat = format;
        if (format != null && format.length() > 1) {
            _objectFormat_start = format.substring(0, 1);
        } else {
            _objectFormat_start = "";
        }
    }

    @Override
    public void formatSetBy(DbContext db) {
        switch (db.databaseType()){
            case MySQL:
                fieldFormatSet("`%`");
                objectFormatSet("`%`");
                break;
            case PostgreSQL:
                fieldFormatSet("\"%\"");
                objectFormatSet("\"%\"");
                break;
            case SQLServer:
                fieldFormatSet("\"%\"");
                objectFormatSet("\"%\"");
                break;
            case DB2:
        }
    }


    /**
     * 格式化字段（用于：set(..,v)）
     */
    public String formatField(String name) {
        if (StringUtils.isEmpty(_fieldFormat)) {
            return name;
        }

        if (name.startsWith(_fieldFormat_start) || name.indexOf(".") > 0 || name.indexOf(")")>0) {
            return name;
        }

        return _fieldFormat.replace("%", name);
    }


    /**
     * 格式化多列（用于：select(..) orderBy(..) groupBy(..)）
     */
    public String formatColumns(String columns) {
        if (StringUtils.isEmpty(_fieldFormat)) {
            return columns;
        }

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
        if (name.startsWith(_fieldFormat_start) || name.equals("*") || name.indexOf(".") > 0 || name.indexOf(")") > 0) {
            return name;
        }

        return _fieldFormat.replace("%", name);
    }

    //格式化条件（用于：where() and() or()） //暂时不实现
    public String formatCondition(String condition){
        return condition;
    }

    /**
     * 格式化对象（用于：from(..), join(..)）
     */
    public String formatObject(String name) {
        if (StringUtils.isEmpty(_objectFormat)) {
            return name;
        }

        if (name.startsWith(_objectFormat_start) || name.indexOf(".") > 0 || name.indexOf("(") > 0) {
            return name;
        }

        if (name.indexOf(" ") < 0) {
            return _objectFormat.replace("%", name);
        }

        StringBuilder sb = StringUtils.borrowBuilder();

        int idx = name.indexOf(" ");
        //类假：xxx_name name;xxx_name as name; name ASC;
        sb.append(_objectFormat.replace("%", name.substring(0,idx)))
                .append(name.substring(idx));

        return StringUtils.releaseBuilder(sb);
    }
}
