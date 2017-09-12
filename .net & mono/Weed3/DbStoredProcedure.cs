using Noear.Weed.Cache;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;


namespace Noear.Weed {

    /**
     * Created by noear on 14-6-12.
     * 存储过程访问类
     */
    public class DbStoredProcedure : DbProcedure {

        public DbStoredProcedure(DbContext context) : base(context) {

        }

        protected internal DbStoredProcedure call(string storedProcedure) {
            this.commandText = storedProcedure;
            this.paramS.Clear();
            this._weedKey = null;

            return this;
        }

        public override DbProcedure set(String param, Object value) {
            doSet(param, value);
            return this;
        }

        public override DbProcedure set(String param, Func<Object> valueGetter) {
            doSet(param, valueGetter);
            return this;
        }

        //
        //===========================================
        //
        protected override String getCommandID() {
            return this.commandText;
        }

        protected override Command getCommand() {
            Command cmd = new Command(this.context);

            cmd.key = getCommandID();
            cmd.paramS = this.paramS;

            StringBuilder sb = new StringBuilder(commandText);

            //1.替换schema
            int idx = 0;
            while (true) {
                idx = sb.IndexOf('$', idx);
                if (idx > 0) {
                    sb.Replace(idx, idx + 1, context.getSchema());
                    idx++;
                } else {
                    break;
                }
            }

            cmd.text = sb.ToString();

            logCommandBuilt(cmd);

            return cmd;
        }
    }
}
