package org.noear.weed.dialect;

/**
 * MySQL数据库方言处理
 *
 * @author noear
 * @since 3.2
 * */
public class DbPhoenixDialect extends DbDialectBase{
    @Override
    public boolean supportsVariablePaging() {
        return true;
    }

    @Override
    public boolean supportsInsertGeneratedKey() {
        return false;
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
    public void insertCmd(StringBuilder sb, String table1) {
        sb.append("UPSERT INTO ").append(tableFormat(table1));
    }
}
