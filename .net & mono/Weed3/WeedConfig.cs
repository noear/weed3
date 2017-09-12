using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed {
    public class WeedConfig {
        public static bool isDebug = false;

        static Action<Command, Exception> onException_listener = null;
        static Action<Command> onExecuteAft_listener = null;
        static Func< Command, Boolean> onExecuteBef_listener = null;
        static Action<Command> onLog_listener = null;

        internal static void logException(Command cmd, Exception ex) {
            if (onException_listener != null) {
                try {
                    onException_listener(cmd, ex);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }

        internal static void logExecuteAft(Command cmd) {
            if (onExecuteAft_listener != null) {
                onExecuteAft_listener(cmd);
            }

            if (cmd.isLog && onLog_listener != null) {
                onLog_listener(cmd);
            }
        }

        internal static bool logExecuteBef(Command cmd) {
            if (onExecuteBef_listener != null) {
                return onExecuteBef_listener(cmd);
            }

            return true;
        }


        //--------------------------------------
        //
        //

        public static void onException(Action<Command, Exception> listener) {
            onException_listener = listener;
        }

        public static void onLog(Action<Command> listener) {
            onLog_listener = listener;
        }

        public static void onExecuteAft(Action<Command> listener) {
            onExecuteAft_listener = listener;
        }
        public static void onExecuteBef(Func<Command, Boolean> listener) {
            onExecuteBef_listener = listener;
        }
    }
}
