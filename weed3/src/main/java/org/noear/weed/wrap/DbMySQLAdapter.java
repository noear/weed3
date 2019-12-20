package org.noear.weed.wrap;

public class DbMySQLAdapter implements DbAdapter {

    @Override
    public String tableFormat(String tb) {
        return "`" + tb + "`";
    }

    @Override
    public String columnFormat(String col) {
        return "`" + col + "`";
    }
}
