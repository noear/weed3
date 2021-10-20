package org.noear.weed.dialect;

/**
 * MySQL数据库方言处理
 *
 * @author noear
 * @since 3.2
 * */
public class DbMySQLDialect extends DbDialectBase{
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }
}
