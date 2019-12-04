package org.noear.weed;

public enum OrderBy {
    ASC("ASC"),
    DESC("DESC");

    String name;
    OrderBy(String name){
        this.name = name;
    }
}
