package org.noear.weed.wrap;

public class DbMySQLDialect implements DbDialect {
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }
}
