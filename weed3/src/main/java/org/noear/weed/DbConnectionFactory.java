package org.noear.weed;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionFactory {
    public Connection getConnection(DataSource ds) throws SQLException {
        return ds.getConnection();
    }
}
