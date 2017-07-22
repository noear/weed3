package noear.weed;

import noear.weed.ext.Act1;
import noear.weed.ext.Act2;

/**
 * Created by yuety on 14/11/20.
 */
public final class WeedConfig {
    public static boolean isDebug = false;

    static Act2<Command,Exception> onException_listener = null;
    static Act1<Command> onExecute_listener = null;

    protected static void logException(Command cmd,Exception ex)
    {
        if(onException_listener != null) {
            onException_listener.run(cmd, ex);
        }
    }

    protected static void logExecute(Command cmd) {
        if (isDebug == false)
            return;

        if (onExecute_listener != null) {
            onExecute_listener.run(cmd);
        }
    }


    //--------------------------------------
    //
    //

    public static void onException(Act2<Command,Exception> listener)
    {
        onException_listener = listener;
    }

    public static void onExecute(Act1<Command> listener){
        onExecute_listener = listener;
    }
}
