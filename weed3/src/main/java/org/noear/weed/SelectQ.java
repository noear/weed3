package org.noear.weed;

public class SelectQ extends SQLBuilder {
    protected SelectQ(SQLBuilder sqlBuilder){
        builder.append(sqlBuilder.builder);
        paramS.addAll(sqlBuilder.paramS);
    }
}
