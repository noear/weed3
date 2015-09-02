using System;
using System.Collections;
using System.Collections.Generic;

namespace Noear.Weed {
    public class DataItemEx  : IDataItem {
        Dictionary<String, Func<Object>> _data = new Dictionary<string, Func<Object>>();
        bool _isNotNull = false;

        public DataItemEx() { }

        public DataItemEx(bool isNotNull) { _isNotNull = isNotNull; }

        public int count() {
            return _data.Count;
        }
        public void clear() {
            _data.Clear();
        }

        public bool exists(string name) {
            return _data.ContainsKey(name);
        }

        public IEnumerable<string> keys() {
            return _data.Keys;
        }

        public IDataItem set(String name, Object value) {
            _data[name] = (() => value);
            return this;
        }

        public DataItemEx set(String name, Func<Object> value) {
            _data[name] = value;
            return this;
        }

        public Object get(String name) {
            return _data[name]();
        }

        public Variate getVariate(String name) {
            if (_data.ContainsKey(name))
                return new VariateEx(name, _data[name]);
            else
                return new Variate(name, null);
        }

        public T toItem<T>(T item) where T : IBinder {
            item.bind((key)=>{
                return getVariate(key);
            });

            return item;
        }

        public short getShort(String name) {
            return (short)get(name);
        }

        public int getInt(String name) {
            return (int)get(name);
        }

        public long getLong(String name) {
            return (long)get(name);
        }

        public double getDouble(String name) {
            return (double)get(name);
        }

        public float getFloat(String name) {
            return (float)get(name);
        }

        public String getString(String name) {
            return (String)get(name);
        }

        public bool getBoolean(String name) {
            return (bool)get(name);
        }

        public DateTime getDateTime(String name) {
            return (DateTime)get(name);
        }

        

        //
        //===========================
        //
        public IEnumerator<KeyValuePair<string, object>> GetEnumerator() {
            foreach (var kv in _data) {
                var val = kv.Value();
                if (_isNotNull) {
                    if (val != null) {
                        yield return new KeyValuePair<string, object>(kv.Key, val);
                    }
                }
                else {
                    yield return new KeyValuePair<string, object>(kv.Key, val);
                }
            }
        }

        IEnumerator IEnumerable.GetEnumerator() {
            foreach (var kv in _data) {
                var val = kv.Value();
                if (_isNotNull) {
                    if (val != null) {
                        yield return new KeyValuePair<string, object>(kv.Key, val);
                    }
                }
                else {
                    yield return new KeyValuePair<string, object>(kv.Key, val);
                }
            }
        }
        
    }
}
