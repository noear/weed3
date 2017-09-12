using Noear.Weed.Cache;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace Noear.Weed {

    /**
     * Created by noear on 14-9-5.
     * 数据库方问基类
     */
    [Serializable]
    public abstract class DbAccess<X> : IWeedKey, IQuery where X : DbAccess<X> {
        public String commandText = null;

        /*数据库上下文*/
        public DbContext context;
        /*访问参数*/
        public List<Variate> paramS = new List<Variate>();
        /*获取执行命令（由子类实现）*/
        protected abstract Command getCommand();
        /*获取访问标识（由子类实现）*/
        protected abstract String getCommandID();

        private Action<Command> onCommandExpr = null;

        public X onCommandBuilt(Action<Command> expr) {
            this.onCommandExpr = expr;
            return (X)this;
        }

        protected void logCommandBuilt(Command cmd) {
            onCommandExpr?.Invoke(cmd);
        }

        public DbAccess(DbContext context) {
            this.context = context;
        }

        /*IWeedKey begin*/
        protected String _weedKey;
        public virtual String getWeedKey() {
            return buildWeedKey(paramS);
        }
        protected String buildWeedKey(IEnumerable<Variate> args) {
            if (_weedKey == null) {
                StringBuilder sb = new StringBuilder();

                sb.Append(getCommandID()).Append(":");

                foreach (Variate p in args) {
                    sb.Append("_").Append(p.getValue() ?? "");
                }

                _weedKey = sb.ToString();
            }
            return _weedKey;
        }
        /*IWeedKey end*/

        /*获取参数(可能不再需要了)*/
        protected Variate doGet(String paramName) {
            int hash = paramName.GetHashCode();
            foreach (Variate p1 in paramS) {
                if (hash == p1._hash)
                    return p1;
            }

            return null;
        }

        protected Variate doGet(int index) {
            return paramS[index];
        }

        /*设置参数值*/
        protected virtual void doSet(String param, Object value) {
            paramS.Add(new Variate(param, value));
        }

        protected virtual void doSet(String param, Func<Object> valueGetter) {
            paramS.Add(new VariateEx(param, valueGetter));
        }

        protected virtual void doSet(String param, Func<Object> valueGetter, Action<Object> valueSetter) {
            paramS.Add(new VariateEx(param, valueGetter, valueSetter));
        }

        protected void doSet(Variate value) {
            paramS.Add(value);
        }

        //=======================
        //
        // 执行相关代码
        //
        /*执行命令（返回受影响数）*/
        public virtual int execute() {
            return new SQLer().execute(getCommand(), _tran);
        }

        public Object getValue() {
            Variate rst = new SQLer().getVariate(getCommand(), _tran);

            if (rst == null)
                return null;
            else
                return rst.getValue();
        }

        /*执行命令（返回符合条件的第一个值）*/
        public T getValue<T>(T def) {
            return getVariate(null).value(def);
        }

        /*执行命令（返回符合条件的第一个值）*/
        public Variate getVariate() {
            return getVariate(null);
        }

        /*执行命令（返回符合条件的第一个值）*/
        public Variate getVariate(Action<CacheUsing, Variate> cacheCondition) {
            Variate rst;
            if (_cache == null)
                rst = new SQLer().getVariate(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                rst = _cache.get(this.getWeedKey(), () => (new SQLer().getVariate(getCommand(), _tran)));
            }
            if (rst == null)
                return new Variate();
            else
                return rst;

        }

        /*执行命令（返回一个模理）*/
        public T getItem<T>(T model) where T : class, IBinder {
            return getItem<T>(model, null);
        }

        /*执行命令（返回一个模理）*/
        public T getItem<T>(T model, Action<CacheUsing, T> cacheCondition) where T : class, IBinder {
            T rst;
            if (_cache == null)
                rst = new SQLer().getItem<T>(model, getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                rst = _cache.get(this.getWeedKey(), () => (new SQLer().getItem<T>(model, getCommand(), _tran)));
            }

            if (rst == null)
                return model;
            else
                return rst;
        }
        /*执行命令（返回一个列表）*/
        public List<T> getList<T>(T model) where T : class, IBinder {
            return getList<T>(model, null);
        }

        /*执行命令（返回一个列表）*/
        public List<T> getList<T>(T model, Action<CacheUsing, List<T>> cacheCondition) where T : class, IBinder {
            List<T> rst;
            if (_cache == null)
                rst = new SQLer().getList<T>(model, getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                rst = _cache.get(this.getWeedKey(), () => (new SQLer().getList<T>(model, getCommand(), _tran)));
            }

            if (rst == null)
                return new List<T>();
            else
                return rst;
        }

        public List<T> getArray<T>(String column) {
            return getDataList().toArray<T>(column);
        }

        public DataList getDataList() {
            return getDataList(null);
        }

        public DataList getDataList(Action<CacheUsing, DataList> cacheCondition) {
            DataList rst;
            if (_cache == null)
                rst = new SQLer().getTable(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                rst = _cache.get(this.getWeedKey(), () => (new SQLer().getTable(getCommand(), _tran)));
            }

            if (rst == null)
                return new DataList();
            else
                return rst;
        }

        public DataItem getDataItem() {
            return getDataItem(null);
        }

        public DataItem getDataItem(Action<CacheUsing, DataList> cacheCondition) {
            DataItem rst;
            if (_cache == null)
                rst = new SQLer().getRow(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                rst = _cache.get(this.getWeedKey(), () => (new SQLer().getRow(getCommand(), _tran)));
            }

            if (rst == null)
                return new DataItem();
            else
                return rst;
        }

        protected DbTran _tran = null;
        public X tran(DbTran transaction) {
            _tran = transaction;
            return (X)this;
        }

        //不能形成控制流,没意义
        //public X tran() {
        //    _tran = context.tran();
        //    return (X)this;
        //}

        //=======================
        //
        // 缓存控制相关
        //

        protected CacheUsing _cache = null;
        /*引用一个缓存服务*/
        public IQuery caching(ICacheService service) {
            _cache = new CacheUsing(service).usingCache(true);
            return this;
        }
        /*是否使用缓存*/
        public IQuery usingCache(bool isCache) {
            _cache.usingCache(isCache);
            return this;
        }
        /*使用缓存时间（单位：秒）*/
        public IQuery usingCache(int seconds) {
            _cache.usingCache(seconds);
            return this;
        }

        /*添加缓存标签*/
        public IQuery cacheTag(String tag) {
            _cache.cacheTag(tag);
            return this;
        }
    }
}
