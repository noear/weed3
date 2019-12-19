package org.noear.weed;

public interface IDbFormater {
    /** 字段格式符设置 */
    void fieldFormatSet(String format);
    /** 对象格式符设置 */
    void objectFormatSet(String format);

    void formatSetBy(DbContext db);

    /** 格式化字段（用于：set(..,v)） */
    String formatField(String name);
    /** 格式化多列（用于：select(..) orderBy(..) groupBy(..)） */
    String formatColumns(String columns);
    /** 格式化条件（用于：where(..) and(..) or(..)）  */
    String formatCondition(String condition);
    /** 格式化对象（用于：from(..), join(..)） */
    String formatObject(String name);
}
