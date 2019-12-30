package org.noear.weed.wrap;

public class ColumnWrap {
    private final String name;
    private final Integer type;
    private final Integer size;
    private final Integer digit;
    private final String remarks;

    public ColumnWrap(String name, Integer type, Integer size, Integer digit, String remarks) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.digit = digit;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "ColumnWrap{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", digit=" + digit +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return SqlTypeMap.getType(type, size, digit);
    }

    public Integer getSize() {
        return size;
    }

    public Integer getDigit() {
        return digit;
    }

    public String getRemarks() {
        return remarks;
    }
}
