package org.noear.weed.generator.entity;

/**
 * @author noear 2021/2/12 created
 */
public class SqlTypeEntity {
    Integer sqlType;
    String javaType;
    String javaType2;

    public SqlTypeEntity(Integer sqlType, String javaType, String javaType2) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.javaType2 = javaType2;
    }
}
