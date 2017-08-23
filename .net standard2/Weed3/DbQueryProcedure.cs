using Noear.Weed.Cache;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace Noear.Weed {
    /**
    * Created by noear on 17-6-12.
    * 查询过程访问类（模拟存储过程）
    */
    public class DbQueryProcedure : DbProcedure {

        private Dictionary<String, Variate> _paramS2 = new Dictionary<String, Variate> ();

        public DbQueryProcedure(DbContext context) : base(context) {

        }

        /*延后初始化接口*/
        private Action _lazyload;
        private bool _is_lazyload;
        protected void lazyload(Action action) {
            _lazyload = action;
            _is_lazyload = false;
        }

        protected void tryLazyload() {
            if (_is_lazyload == false) {
                _is_lazyload = true;

                if (_lazyload != null) {
                    _lazyload();
                }
            }
        }

        protected internal DbQueryProcedure sql(string sqlCode) {
            this.commandText = sqlCode;
            this.paramS.Clear();
            this._weedKey = null;

            if (_lazyload == null) { //如果是后续加载的话，不能清掉这些参数
                this._paramS2.Clear();
            }

            return this;
        }

        private DbQueryProcedure doSqlItem(String sqlCode) {
            this.commandText = sqlCode;
            this.paramS.Clear();
            this._weedKey = null;

            return this;
        }

        public override DbProcedure set(String param, Object value) {
            _paramS2.Add(param, new Variate(param, value));
            return this;
        }

        public override DbProcedure set(String param, Func<Object> valueGetter) {
            _paramS2.Add(param, new VariateEx(param, valueGetter));
            return this;
        }

        //
        //===========================================
        //
        public override string getWeedKey() {
            return buildWeedKey(_paramS2.Values);
        }
        protected override String getCommandID() {
            tryLazyload();

            return this.commandText;
        }

        protected override Command getCommand() {
            tryLazyload();

            Command cmd = new Command(context);

            cmd.key = getCommandID();

            String sqlTxt = this.commandText;

            {
                var mlist = Regex.Matches(sqlTxt, "@\\w+");
                foreach (Match m in mlist) {
                    String key = m.Groups[0].Value;
                    if (WeedConfig.isDebug) {
                        if (_paramS2.ContainsKey(key) == false) {
                            throw new WeedException("Lack of parameter:" + key);
                        }
                    }

                    doSet(_paramS2[key]);
                }

                foreach (String key in _paramS2.Keys) {
                    sqlTxt = sqlTxt.Replace(key, "?");
                }
            }

            if (context.hasSchema()) {
                sqlTxt.Replace("$", context.getSchema());
            }

            cmd.paramS = this.paramS;
            cmd.text = sqlTxt;

            return cmd;
        }

        public override int execute() {
            tryLazyload();

            if (context.allowMultiQueries) {
                return base.execute();
            } else {
                int num = 0;
                String[] sqlList = commandText.Split(';'); //支持多段SQL执行
                foreach (String sql in sqlList) {
                    if (sql.Length > 10) {
                        doSqlItem(sql);

                        num += base.execute();
                    }
                }

                return num;
            }
        }
    }
}
