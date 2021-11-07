package org.noear.weed.wrap;

public class ColumnWrap {
    private final String name;
    private final Integer sqlType;
    private final Integer size;
    private final Integer digit;
    private final String isNullable;
    private final String remarks;

    public ColumnWrap(String name, Integer sqlType, Integer size, Integer digit, String isNullable, String remarks) {
        this.name = name;
        this.sqlType = sqlType;
        this.size = size;
        this.digit = digit;
        this.isNullable = isNullable;
        this.remarks = remarks;
    }



    public String getName() {
        return name;
    }

    public Integer getSqlType() {
        return sqlType;
    }

    /**
     * 获取类型印射
     * */
    public SqlTypeDesc getSqlTypeDesc(){
        return SqlTypeUtil.getTypeDesc(this);
    }

    public Integer getSize() {
        return size;
    }

    public Integer getDigit() {
        return digit;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public String getRemarks() {
        return remarks;
    }
}
