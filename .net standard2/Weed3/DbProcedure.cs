using Noear.Weed.Cache;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Noear.Weed {
    public abstract class DbProcedure : DbAccess<DbProcedure> {
        public DbProcedure(DbContext context) : base(context) {
        }

        abstract public DbProcedure set(String param, Object value);
        abstract public DbProcedure set(String param, Func<Object> valueGetter);

        //=================================
        //
        //以下未测试
        //
        public List<T> getListBySplit<T>(T model, String splitParamName, Func<T, Object> getKey) where T : class, IBinder {
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
                } else {
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
