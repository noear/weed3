package noear.weed.ext;

import java.sql.SQLException;

/**
 * Created by yuety on 14-9-12.
 */
public interface Fun0Ex <T,Ex extends Throwable> {
    public T run() throws Ex;
}
