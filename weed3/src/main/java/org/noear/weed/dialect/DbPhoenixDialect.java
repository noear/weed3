package org.noear.weed.dialect;

/**
 * MySQL数据库方言处理
 *
 * @author noear
 * @since 3.2
 * */
public class DbPhoenixDialect implements DbDialect {
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public String tableFormat(String name) {
        return name;
    }

    @Override
    public String columnFormat(String name) {
        return name;
    }

    @Override
    public String insertCmd() {
        return "INSERT INTO";
    }

    @Override
    public boolean insertGeneratedKey() {
        return false;
    }
}
