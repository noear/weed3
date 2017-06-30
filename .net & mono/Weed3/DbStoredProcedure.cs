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
    public class DbStoredProcedure : DbAccess<DbStoredProcedure> {

        public DbStoredProcedure(DbContext context) : base(context) {

        }

        protected internal DbStoredProcedure call(string storedProcedure) {
            this.commandText = storedProcedure;
            this.paramS.Clear();
            this._weedKey = null;

            return this;
        }

        public DbStoredProcedure set(String param, Object value) {
            doSet(param, value);
            return this;
        }

        public DbStoredProcedure set(String param, Func<Object> valueGetter) {
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
                }
                else {
                    break;
                }
            }

            cmd.text = sb.ToString();

            return cmd;
        }

        //=================================
        //
        //以下未测试
        //
        public List<T> getListBySplit<T>(T model,String splitParamName, Func<T, Object> getKey) where T : class, IBinder {
            //如果没有缓存,则直接返回执行结果
            //
            if (_cache == null || _cache.cacheController == CacheState.NonUsing)
                return getList<T>(model);


            //1.获取所有分拆后的WeedCode
            //
            List<ValueMapping> vmlist = new List<ValueMapping>(do_splitWeedCode(splitParamName));

            List<T> list = new List<T>(vmlist.Count);
            StringBuilder sb = new StringBuilder();

            //2.根据WeedCode=>WeedKey获取已缓存的数据
            //
            foreach (ValueMapping vm in vmlist) {
                T temp = _cache.getOnly<T>(vm.weedCode);

                if (temp != null) {
                    vm.isCached = true;
                    list.Add(temp);
                }
                else {
                    vm.isCached = false;
                    sb.Append(vm.value + ",");
                }
            }

            //3.获取未缓存的数据，并进行缓存
            //
            if (sb.Length > 0) {
                sb.Remove(sb.Length - 1, 1);

                doGet(splitParamName).setValue(sb.ToString());

                List<T> newList1 = getList<T>(model);

                foreach (T ent in newList1) {
                    String weedKey = do_getSubWeedCode(vmlist, splitParamName, getKey(ent).ToString());

                    _cache.storeOnly(weedKey, ent);
                }

                list.AddRange(newList1);
            }

            return list;
        }

        //-------

        private List<ValueMapping> do_splitWeedCode(String paramName) {
            List<ValueMapping> list = new List<ValueMapping>();

            String[] subKeyValues = ((string)doGet(paramName).getValue()).Split(',');

            foreach (String value in subKeyValues)
                list.Add(do_buildSubWeedCode(paramName, value));

            return list;
        }

        private String do_getSubWeedCode(List<ValueMapping> vmList, String splitParamName, String value) {
            int hash = value.GetHashCode();

            foreach (ValueMapping vm in vmList) {
                if (hash == vm._hash)
                    return vm.weedCode;
            }

            return do_buildSubWeedCode(splitParamName, value).weedCode;
        }

        private ValueMapping do_buildSubWeedCode(String paramName, String value) {
            int hash = paramName.GetHashCode();
            StringBuilder sb = new StringBuilder();

            sb.Append(this.getCommandID() + ":");
            foreach (Variate item in paramS) {
                if (item._hash == hash)
                    sb.Append("_" + value.Trim());
                else {
                    Object val = item.getValue();

                    if (val == null)
                        sb.Append("_null");
                    else
                        sb.Append("_" + val);
                }
            }

            return new ValueMapping(value, sb.ToString());
        }
    }
}
