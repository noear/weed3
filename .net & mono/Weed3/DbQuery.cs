using System;
using System.Collections.Generic;
using System.Text;

namespace Noear.Weed {
    /**
 * Created by noear on 14-9-5.
 *
 * 查询语句访问类
 *
 */
    public class DbQuery : DbAccess<DbQuery> {

        /*查询语句*/
        public DbQuery(DbContext context) : base(context) {
            
        }
        
        internal DbQuery sql(SQLBuilder sqlBuilder) {
            this.commandText = sqlBuilder.ToString();
            this.paramS.Clear();
            this._weedKey = null;
            foreach (Object p1 in sqlBuilder.paramS) {
                doSet("", p1);
            }

            return this;
        }

        public long insert() {
            return new SQLer().insert(getCommand(), _tran);
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
            {
                int idx = 0;
                while (true) {
                    idx = sb.IndexOf('$', idx);
                    if (idx > 0) {
                        sb.Replace(idx, idx + 1, context.getSchema());
                        idx++;
                    }
                    else {
                        break;
                    }
                }
            }

            if (this.paramS.Count > 0) {
                
                int paIdx = 0; //参数位置 
                int atIdx = 0; //? in sb位置//到此时已不再有?...(之前已转换为?)
                

                while (true) {
                    atIdx = sb.IndexOf('?', atIdx);
                    if (atIdx > 0) {
                        Variate temp = doGet(paIdx);
                        if (temp == null) {
                            WeedConfig.logException(cmd, new WeedException("缺少参数"));
                        }

                        temp.setName("p" + paIdx);
                        sb.Replace("?", "@" + temp.getName(), atIdx, 1);

                        //atIdx+= strVal.Length;//增加替换后的长度
                        atIdx += temp.getName().Length +1;
                        paIdx++;
                    }
                    else {
                        break;
                    }
                }
            }

            cmd.text = sb.ToString();

            logCommandBuilt(cmd);

            return cmd;
        }
    }
}
