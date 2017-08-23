using System;
using System.Collections.Generic;

namespace Noear.Weed {
    public class Command {
        /*命令id*/
        public String key;
        /*命令文本*/
        public String text;
        /*命令参数*/
        public List<Variate> paramS;
        /*数据库上下文*/
        public DbContext context;

        public Command(DbContext context) {
            this.context = context;
            this.context.lastCommand = this;
        }
    }
}
