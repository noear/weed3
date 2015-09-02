package noear.weed;

import noear.weed.ext.Act2;

/**
 * Created by yuety on 14/11/20.
 */
public class WeedLog {
    static Act2<DbCommand,Exception> _writer;
    public static void logException(DbCommand cmd,Exception ex)
    {
        if(isInited()) {
            _writer.run(cmd, ex);
        }
    }

    public static boolean isInited() {
        return _writer != null;
    }

    public static void setWriter(Act2<DbCommand,Exception> writer)
    {
        _writer = writer;
    }
}
