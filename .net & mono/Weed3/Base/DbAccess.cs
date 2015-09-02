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
    public abstract class DbAccess : IWeedKey, IQuery {
        public String commandText = null;

        /*数据库上下文*/
        public DbContext context;
        /*访问参数*/
        public List<Variate> paramS = new List<Variate>();
        /*获取执行命令（由子类实现）*/
        protected abstract Command getCommand();
        /*获取访问标识（由子类实现）*/
        protected abstract String getCommandID();

        public DbAccess(DbContext context) {
            this.context = context;
        }
        
        /*IWeedKey begin*/
        protected String _weedKey;
        public String getWeedKey() {
            if (_weedKey == null) {
                StringBuilder sb = new StringBuilder();

                sb.Append(getCommandID()).Append(":");

                foreach (Variate p in paramS) {
                    sb.Append("_").Append(p.getValue()??"");
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

        //=======================
        //
        // 执行相关代码
        //
        /*执行命令（返回受影响数）*/
        public int execute() {
            return new SQLer().execute(getCommand(), _tran);
        }

        public long getCount() {
            return getValue(0);
        }

        /*执行命令（返回符合条件的第一个值）*/
        public T getValue<T>(T def) {
            return getValue(def, null);
        }

        /*执行命令（返回符合条件的第一个值）*/
        public T getValue<T>(T def, Action<CacheUsing, T> cacheCondition) {
            if (_cache == null)
                return new SQLer().getValue(def, getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                return _cache.get(this.getWeedKey(), () => {
                    return new SQLer().getValue(def, getCommand(), _tran);
                });
            }
        }

        /*执行命令（返回一个模理）*/
        public T getItem<T>() where T : IBinder {
            return getItem<T>(null);
        }

        /*执行命令（返回一个模理）*/
        public T getItem<T>(Action<CacheUsing, T> cacheCondition) where T : IBinder {
            if (_cache == null)
                return new SQLer().getItem<T>(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                return _cache.get(this.getWeedKey(), () => (new SQLer().getItem<T>(getCommand(), _tran)));
            }
        }
        /*执行命令（返回一个列表）*/
        public List<T> getList<T>() where T : IBinder {
            return getList<T>(null);
        }

        /*执行命令（返回一个列表）*/
        public List<T> getList<T>(Action<CacheUsing, List<T>> cacheCondition) where T : IBinder {

            if (_cache == null)
                return new SQLer().getList<T>(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                return _cache.get(this.getWeedKey(), () => (new SQLer().getList<T>(getCommand(), _tran)));
            }
        }

        public List<T> getArray<T>(String column) 
        {
           return getDataList().toArray<T>(column);
        }

        public DataList getDataList() {
            return getDataList(null);
        }

        public DataList getDataList(Action<CacheUsing, DataList> cacheCondition) {
            if (_cache == null)
                return new SQLer().getTable(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                return _cache.get(this.getWeedKey(), () => (new SQLer().getTable(getCommand(), _tran)));
            }
        }

        public DataItem getDataItem() {
            return getDataItem(null);
        }

        public DataItem getDataItem(Action<CacheUsing, DataList> cacheCondition) {
            if (_cache == null)
                return new SQLer().getRow(getCommand(), _tran);
            else {
                _cache.usingCache(cacheCondition);
                return _cache.get(this.getWeedKey(), () => (new SQLer().getRow(getCommand(), _tran)));
            }
        }

        protected DbTran _tran = null;
        public DbAccess tran(DbTran transaction) {
            _tran = transaction;
            return this;
        }

        public DbAccess tran() {
            _tran = context.tran();
            return this;
        }

        //=======================
        //
        // 缓存控制相关
        //

        protected CacheUsing _cache = null;
        /*引用一个缓存服务*/
        public IQuery caching(ICacheService service) {
            _cache = new CacheUsing(service);
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

        //    public DbAccess usingCache(Act2<CacheUsing,Object> condition)
        //    {
        //        _cache.usingCache(condition);
        //        return this;
        //    }



        /*添加缓存标签*/
        public IQuery cacheTag(String tag) {
            _cache.cacheTag(tag);
            return this;
        }
    }
}
