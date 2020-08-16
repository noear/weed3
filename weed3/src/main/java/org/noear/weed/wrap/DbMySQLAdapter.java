package org.noear.weed.wrap;

public class DbMySQLAdapter implements DbAdapter {
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }
}
