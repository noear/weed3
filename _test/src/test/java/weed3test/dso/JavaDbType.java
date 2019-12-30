package weed3test.dso;

import org.noear.weed.wrap.ColumnWrap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaDbType {
    /*jdbc type 对应的java的type，参考JavaSqlTypeHandler和BeanProcessor*/
    public static Map<Integer, Class<?>> jdbcJavaTypes = new HashMap<Integer, Class<?>>(); // jdbc type to java
    // type/*生成java代码*/
    public static Map<Integer, String> mapping = new HashMap<Integer, String>();

    public static Map<String, Integer> jdbcTypeNames = new HashMap<String, Integer>();

    public final static String UNKNOW = "UNKNOW";
    public final static String SPECIAL = "SPECIAL";

    static {
        // 初始化jdbcJavaTypes：
        jdbcJavaTypes.put(new Integer(Types.LONGNVARCHAR), String.class); // -16
        // 字符串
        jdbcJavaTypes.put(new Integer(Types.NCHAR), String.class); // -15 字符串
        jdbcJavaTypes.put(new Integer(Types.NVARCHAR), String.class); // -9 字符串
        jdbcJavaTypes.put(new Integer(Types.ROWID), String.class); // -8 字符串
        jdbcJavaTypes.put(new Integer(Types.BIT), Boolean.class); // -7 布尔
        jdbcJavaTypes.put(new Integer(Types.TINYINT), Integer.class); // -6 数字
        jdbcJavaTypes.put(new Integer(Types.BIGINT), Long.class); // -5 数字
        jdbcJavaTypes.put(new Integer(Types.LONGVARBINARY), byte[].class); // -4
        // 二进制
        jdbcJavaTypes.put(new Integer(Types.VARBINARY), byte[].class); // -3 二进制
        jdbcJavaTypes.put(new Integer(Types.BINARY), byte[].class); // -2 二进制
        jdbcJavaTypes.put(new Integer(Types.LONGVARCHAR), String.class); // -1
        // 字符串
        // jdbcJavaTypes.put(new Integer(Types.NULL), String.class); // 0 /
        jdbcJavaTypes.put(new Integer(Types.CHAR), String.class); // 1 字符串
        jdbcJavaTypes.put(new Integer(Types.NUMERIC), BigDecimal.class); // 2 数字
        jdbcJavaTypes.put(new Integer(Types.DECIMAL), BigDecimal.class); // 3 数字
        jdbcJavaTypes.put(new Integer(Types.INTEGER), Integer.class); // 4 数字
        jdbcJavaTypes.put(new Integer(Types.SMALLINT), Integer.class); // 5 数字
        jdbcJavaTypes.put(new Integer(Types.FLOAT), BigDecimal.class); // 6 数字
        jdbcJavaTypes.put(new Integer(Types.REAL), BigDecimal.class); // 7 数字
        jdbcJavaTypes.put(new Integer(Types.DOUBLE), BigDecimal.class); // 8 数字
        jdbcJavaTypes.put(new Integer(Types.VARCHAR), String.class); // 12 字符串
        jdbcJavaTypes.put(new Integer(Types.BOOLEAN), Boolean.class); // 16 布尔
        // jdbcJavaTypes.put(new Integer(Types.DATALINK), String.class); // 70 /
        jdbcJavaTypes.put(new Integer(Types.DATE), Date.class); // 91 日期
        jdbcJavaTypes.put(new Integer(Types.TIME), Time.class); // 92 日期
        jdbcJavaTypes.put(new Integer(Types.TIMESTAMP), Timestamp.class); // 93 日期
//
		jdbcJavaTypes.put(Types.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
		jdbcJavaTypes.put(Types.TIME_WITH_TIMEZONE, Time.class);
        jdbcJavaTypes.put(new Integer(Types.OTHER), Object.class); // 1111 其他类型？
        // jdbcJavaTypes.put(new Integer(Types.JAVA_OBJECT), Object.class); //
        // 2000
        // jdbcJavaTypes.put(new Integer(Types.DISTINCT), String.class); // 2001
        // jdbcJavaTypes.put(new Integer(Types.STRUCT), String.class); // 2002
        // jdbcJavaTypes.put(new Integer(Types.ARRAY), String.class); // 2003
        jdbcJavaTypes.put(new Integer(Types.BLOB), byte[].class); // 2004 二进制
        jdbcJavaTypes.put(new Integer(Types.CLOB), String.class); // 2005 大文本
        // jdbcJavaTypes.put(new Integer(Types.REF), String.class); // 2006
        jdbcJavaTypes.put(new Integer(Types.SQLXML), SQLXML.class); // 2009
        jdbcJavaTypes.put(new Integer(Types.NCLOB), String.class); // 2011 大文本


        mapping.put(Types.BIGINT, "Long");
        mapping.put(Types.BINARY, "byte[]");
        mapping.put(Types.BIT, "Integer");
        mapping.put(Types.BLOB, "byte[]");
        mapping.put(Types.BOOLEAN, "Integer");
        mapping.put(Types.CHAR, "String");
        mapping.put(Types.CLOB, "String");
        mapping.put(Types.DATALINK, UNKNOW);
        mapping.put(Types.DATE, "Date");
        mapping.put(Types.DECIMAL, "SPECIAL");
        mapping.put(Types.DISTINCT, UNKNOW);
        mapping.put(Types.DOUBLE, "Double");
        mapping.put(Types.FLOAT, "Float");
        mapping.put(Types.INTEGER, "Integer");
        mapping.put(Types.JAVA_OBJECT, UNKNOW);
        mapping.put(Types.LONGNVARCHAR, "String");
        mapping.put(Types.LONGVARBINARY, "byte[]");
        mapping.put(Types.LONGVARCHAR, "String");
        mapping.put(Types.NCHAR, "String");
        mapping.put(Types.NVARCHAR, "String");
        mapping.put(Types.NCLOB, "String");
        mapping.put(Types.NULL, UNKNOW);
        // 根据长度制定Integer，或者Double
        mapping.put(Types.NUMERIC, SPECIAL);
        mapping.put(Types.OTHER, "Object");
        mapping.put(Types.REAL, "Double");
        mapping.put(Types.REF, UNKNOW);

        mapping.put(Types.SMALLINT, "Integer");
        mapping.put(Types.SQLXML, "SQLXML");
        mapping.put(Types.STRUCT, UNKNOW);
        mapping.put(Types.TIME, "Date");
        mapping.put(Types.TIMESTAMP, "Timestamp");
        mapping.put(Types.TINYINT, "Integer");
        mapping.put(Types.VARBINARY, "byte[]");
        mapping.put(Types.VARCHAR, "String");

        // jdk 8 support
        mapping.put(Types.REF_CURSOR, UNKNOW);
        mapping.put(Types.TIMESTAMP_WITH_TIMEZONE, "Timestamp");
        mapping.put(Types.TIME_WITH_TIMEZONE, "Timestamp");


        Field[] fields = java.sql.Types.class.getFields();
        for (int i = 0, len = fields.length; i < len; ++i) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                try {
                    String name = fields[i].getName().toLowerCase();
                    Integer value = (Integer) fields[i].get(java.sql.Types.class);
                    jdbcTypeNames.put(name, value);

                } catch (IllegalArgumentException e) {
                    //不可能发生
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    //不可能发生
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isDateType(Integer sqlType) {
        // 日期类型有特殊操作
        if (sqlType == Types.DATE || sqlType == Types.TIME || sqlType == Types.TIME_WITH_TIMEZONE
                || sqlType == Types.TIMESTAMP || sqlType == Types.TIMESTAMP_WITH_TIMEZONE) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(Integer sqlType) {
        if (sqlType == Types.BOOLEAN || sqlType == Types.BIT || sqlType == Types.INTEGER || sqlType == Types.TINYINT
                || sqlType == Types.SMALLINT) {
            return true;
        } else {
            return false;
        }
    }

    public static String getType(ColumnWrap cw){
        return getType(cw.getType(),cw.getSize(),cw.getDigit());
    }

    public static String getType(Integer sqlType, Integer size, Integer digit) {
        String type = mapping.get(sqlType);
        if (type.equals(SPECIAL)) {

            if (digit != null && digit != 0) {
                return "Double";
            } else {
                // 有可能是BigInt，但先忽略，这种情况很少，用户也可以手工改
                if (size >= 9) {
                    return "Long";
                } else {
                    return "Integer";
                }
            }
        } else {
            return type;
        }
    }


    public static boolean isJavaNumberType(int jdbcType) {
        Class<?> type = jdbcJavaTypes.get(jdbcType);
        return (type == null) ? false : (Number.class.isAssignableFrom(type));
    }
}
