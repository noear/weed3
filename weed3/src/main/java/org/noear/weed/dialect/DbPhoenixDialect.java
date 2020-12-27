package org.noear.weed.dialect;

import org.noear.weed.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
        return "UPSERT INTO";
    }

    @Override
    public PreparedStatement prepareCMD(Connection c, Command cmd, boolean isInsert) throws SQLException {
        if (cmd.text.indexOf("{call") >= 0)
            return c.prepareCall(cmd.fullText());
        else {
            return c.prepareStatement(cmd.fullText());
        }
    }
}
