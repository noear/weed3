package org.noear.weed.ext;

import java.sql.SQLException;

/**
 * Created by noear on 14-9-12.
 */
public interface Fun0Ex <T,Ex extends Throwable> {
    T run() throws Ex;
}
