package org.noear.weed.dialect;

/**
 * @author noear 2021/10/21 created
 */
public class DbPrestoDialect extends DbDialectBase{
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public boolean supportsInsertGeneratedKey() {
        return false;
    }
}
