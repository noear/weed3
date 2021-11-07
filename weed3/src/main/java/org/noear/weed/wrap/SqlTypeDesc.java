package org.noear.weed.wrap;

/**
 * @author noear 2021/2/12 created
 */
public class SqlTypeDesc {
    Integer sqlType;
    String javaType;
    String javaType2;

    public Integer getSqlType() {
        return sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getJavaType2() {
        return javaType2;
    }

    public SqlTypeDesc(Integer sqlType, String javaType, String javaType2) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.javaType2 = javaType2;
    }
}
