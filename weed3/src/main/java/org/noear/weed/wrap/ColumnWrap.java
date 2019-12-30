package org.noear.weed.wrap;

public class ColumnWrap {
    public String name;
    public Integer type;
    public Integer size;
    public Integer digit;
    public String remarks;

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
}
