package org.noear.weed.generator.entity;

import org.noear.weed.wrap.ColumnWrap;

import java.sql.JDBCType;
import java.sql.SQLType;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

class SqlTypeMap {

    public static Map<Integer, SqlTypeEntity> mapping = new HashMap<Integer, SqlTypeEntity>();

    public final static String NUMERIC = "NUMERIC";

    private static void put(Integer sqlType, String javaType, String javaType2) {
        mapping.put(sqlType, new SqlTypeEntity(sqlType, javaType, javaType2));
    }

    static {
        put(Types.BIGINT, "Long", "long");
        put(Types.BINARY, "byte[]", "byte[]");
        put(Types.BIT, "Boolean", "boolean");
        put(Types.BLOB, "byte[]", "byte[]");
        put(Types.BOOLEAN, "Boolean", "boolean");
        put(Types.CHAR, "String", "String");
        put(Types.CLOB, "String", "String");
        put(Types.DATE, "Date", "Date");
        put(Types.DECIMAL, "BigDecimal", "BigDecimal");
        put(Types.DOUBLE, "Double", "double");
        put(Types.FLOAT, "Float", "float");
        put(Types.INTEGER, "Integer", "int");
        put(Types.JAVA_OBJECT, "Object", "Object");
        put(Types.LONGNVARCHAR, "String", "String");
        put(Types.LONGVARBINARY, "byte[]", "byte[]");
        put(Types.LONGVARCHAR, "String", "String");
        put(Types.NCHAR, "String", "String");
        put(Types.NVARCHAR, "String", "String");
        put(Types.NCLOB, "String", "String");
        // 根据长度制定Integer，或者Double
        put(Types.NUMERIC, NUMERIC, NUMERIC);
        put(Types.OTHER, "Object", "Object");
        put(Types.REAL, "Float", "float");

        put(Types.SMALLINT, "Integer", "int");
        put(Types.SQLXML, "SQLXML", "SQLXML");
        put(Types.TIME, "Date", "Date");
        put(Types.TIMESTAMP, "Date", "Date");
        put(Types.TINYINT, "Integer", "int");
        put(Types.VARBINARY, "byte[]", "byte[]");
        put(Types.VARCHAR, "String", "String");

        // jdk 8 support
        put(Types.TIMESTAMP_WITH_TIMEZONE, "Date", "Date");
        put(Types.TIME_WITH_TIMEZONE, "Date", "Date");
    }

    public static String getType(ColumnWrap cw) {
        return getType(cw.getType(), cw.getSize(), cw.getDigit());
    }

    public static String getType(Integer sqlType, Integer size, Integer digit) {
        SqlTypeEntity type = mapping.get(sqlType);

        if (type == null) {
            return "Unknown";
        }

        if (type.javaType.equals(NUMERIC)) {
            if (digit != null && digit != 0) {
                type = mapping.get(Types.DOUBLE);
            } else {
                // 有可能是BigInt，但先忽略，这种情况很少，用户也可以手工改
                if (size >= 9) {
                    type = mapping.get(Types.BIGINT);
                } else {
                    type = mapping.get(Types.INTEGER);
                }
            }
        }

        return type.javaType;
    }
}
