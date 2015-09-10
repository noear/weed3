package noear.weed;

import noear.weed.ext.Act2;

/**
 * Created by yuety on 14/11/20.
 */
public class WeedLog {
    static Act2<Command,Exception> _writer;
    public static void logException(Command cmd,Exception ex)
    {
        if(isInited()) {
            _writer.run(cmd, ex);
        }
    }

    public static boolean isInited() {
        return _writer != null;
    }

    public static void setWriter(Act2<Command,Exception> writer)
    {
        _writer = writer;
    }
}
