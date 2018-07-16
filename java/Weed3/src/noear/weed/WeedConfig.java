package noear.weed;

import noear.weed.ext.Act1;
import noear.weed.ext.Act2;
import noear.weed.ext.Fun1;

/**
 * Created by yuety on 14/11/20.
 */
public final class WeedConfig {
    public static boolean isDebug = false;
    public static boolean isUsingValueExpression=true;

    static Act2<Command,Exception> onException_listener = null;
    static Act1<Command> onExecuteAft_listener = null;
    static Fun1<Boolean,Command> onExecuteBef_listener = null;
    static Act1<Command> onLog_listener = null;


    protected static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

    protected static void logException(Command cmd,Exception ex) {
        if (onException_listener != null) {
            try {
                onException_listener.run(cmd, ex);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected static void logExecuteAft(Command cmd) {
        cmd.timestop = System.currentTimeMillis();

        if (onExecuteAft_listener != null) {
            onExecuteAft_listener.run(cmd);
        }

        if(cmd.isLog && onLog_listener != null){
            onLog_listener.run(cmd);
        }
    }

    protected static boolean logExecuteBef(Command cmd) {
        cmd.timestart = System.currentTimeMillis();

        if (onExecuteBef_listener != null) {
            return onExecuteBef_listener.run(cmd);
        }

        return true;
    }


    //--------------------------------------
    //
    //

    public static void onException(Act2<Command,Exception> listener)
    {
        onException_listener = listener;
    }

    public static void onLog(Act1<Command> listener){
        onLog_listener = listener;
    }

    public static void onExecuteAft(Act1<Command> listener){
        onExecuteAft_listener = listener;
    }
    public static void onExecuteBef(Fun1<Boolean,Command> listener){
        onExecuteBef_listener = listener;
    }
}
