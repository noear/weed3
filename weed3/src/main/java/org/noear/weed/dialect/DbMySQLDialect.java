package org.noear.weed.dialect;

public class DbMySQLDialect implements DbDialect {
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }
}
