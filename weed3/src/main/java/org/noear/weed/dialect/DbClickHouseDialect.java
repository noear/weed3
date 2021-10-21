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

    @Override
    public void updateCmd(StringBuilder sb, String table1) {
        sb.append("ALTER TABLE ").append(table1).append(" UPDATE ");
    }

    @Override
    public void deleteCmd(StringBuilder sb, String table1, boolean addFrom) {
        sb.append("ALTER TABLE ").append(table1).append(" DELETE ");
    }
}
