package org.noear.weed.generator.entity;

import org.noear.weed.wrap.ColumnWrap;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

class SqlTypeMap {

    public static Map<Integer, String> mapping = new HashMap<Integer, String>();

    public final static String UNKNOW = "UNKNOW";
    public final static String SPECIAL = "SPECIAL";
    public final static String TINYINT = "TINYINT";

    static {
        mapping.put(Types.BIGINT, "Long");
        mapping.put(Types.BINARY, "byte[]");
        mapping.put(Types.BIT, "Integer");
        mapping.put(Types.BLOB, "byte[]");
        mapping.put(Types.BOOLEAN, "Integer");
        mapping.put(Types.CHAR, "String");
        mapping.put(Types.CLOB, "String");
        mapping.put(Types.DATALINK, UNKNOW);
        mapping.put(Types.DATE, "Date");
        mapping.put(Types.DECIMAL, "BigDecimal");
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
        mapping.put(Types.TIMESTAMP, "Date");
        mapping.put(Types.TINYINT, "Integer");
        mapping.put(Types.VARBINARY, "byte[]");
        mapping.put(Types.VARCHAR, "String");

        // jdk 8 support
        mapping.put(Types.REF_CURSOR, UNKNOW);
        mapping.put(Types.TIMESTAMP_WITH_TIMEZONE, "Date");
        mapping.put(Types.TIME_WITH_TIMEZONE, "Date");

    }

    public static String getType(ColumnWrap cw) {
        return getType(cw.getType(), cw.getSize(), cw.getDigit());
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
        }

        if (type.equals(TINYINT)) {
            if (size != null && size == 1) {
                return "Boolean";
            }
        }

        return type;
    }
}
