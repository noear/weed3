using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed {
    public class WeedConfig {
        public static bool isDebug = false;

        static Action<Command, Exception> onException_listener = null;
        static Action<Command> onExecute_listener = null;

        internal static void logException(Command cmd, Exception ex) {
            if (onException_listener != null) {
                onException_listener(cmd, ex);
            }
        }

        internal static void logExecute(Command cmd) {
            if (isDebug == false)
                return;

            if (onExecute_listener != null) {
                onExecute_listener(cmd);
            }
        }


        //--------------------------------------
        //
        //

        public static void onException(Action<Command, Exception> listener) {
            onException_listener = listener;
        }

        public static void onExecute(Action<Command> listener) {
            onExecute_listener = listener;
        }
    }
}
