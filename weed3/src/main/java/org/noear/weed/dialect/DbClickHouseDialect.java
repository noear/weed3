package org.noear.weed.dialect;

/**
 * @author noear 2021/10/20 created
 */
public class DbClickHouseDialect extends DbDialectBase{
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public boolean supportsInsertGeneratedKey() {
        return false;
    }
}
