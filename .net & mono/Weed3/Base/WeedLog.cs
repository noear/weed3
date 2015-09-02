using System;

namespace Noear.Weed {
    /**
 * Created by noear on 14/11/20.
 */
    public class WeedLog {
        static Action<Command, Exception> _writer;
        public static void logException(Command cmd, Exception ex) {
            if (isInited()) {
                _writer(cmd, ex);
            }
        }

        public static bool isInited() {
            return _writer != null;
        }

        public static void setWriter(Action<Command, Exception> writer) {
            _writer = writer;
        }
    }

}
