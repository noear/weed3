using System;
using System.Collections;
using System.Collections.Generic;

namespace Noear.Weed {
    public class DataItem  : IDataItem {
        Dictionary<String, Object> _data = new Dictionary<string, object>();

        public DataItem() { }
        public DataItem(bool isUsingDbNull) { _isUsingDbNull = isUsingDbNull; }

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
            _data[name] = value;
            return this;
        }

        public Object get(String name) {
            return _data[name];
        }

        public Variate getVariate(String name) {
            if (_data.ContainsKey(name))
                return new Variate(name, get(name));
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
            return (short)_data[name];
        }

        public int getInt(String name) {
            return (int)_data[name];
        }

        public int getInt2(String name) {
            Object val = get(name);

            if (val == null) {
                return 0;
            }

            if (val is long){
                return (int)((long)val);
            }

            if (val is int){
                return (int)val;
            }

            if (val is Decimal){
                return (int)((Decimal)val);
            }

            return 0;
        }

        public long getLong(String name) {
            return (long)_data[name];
        }

        public long getLong2(String name) {
            Object val = get(name);

            if (val == null) {
                return 0;
            }

            if (val is long){
                return (long)val;
            }

            if (val is int){
                return (int)val;
            }

            if (val is Decimal){
                return (long)((Decimal)val);
            }

            return 0;
        }

        public double getDouble(String name) {
            return (double)_data[name];
        }

        public float getFloat(String name) {
            return (float)_data[name];
        }

        public String getString(String name) {
            return (String)_data[name];
        }

        public bool getBoolean(String name) {
            return (bool)_data[name];
        }

        public DateTime getDateTime(String name) {
            return (DateTime)_data[name];
        }

        public void forEach(Action<String, Object> callback) {
            foreach (var kv in _data) {
                if (kv.Value == null && _isUsingDbNull) {
                    callback(kv.Key, "$NULL");
                }
                else {
                    callback(kv.Key, kv.Value);
                }
            }
        }

        private bool _isUsingDbNull = false;

        //============================
        public static IDataItem create(IDataItem schema, GetHandler source) {
            DataItem item = new DataItem();
            foreach (var key in schema.keys()) {
                object val = source(key);
                if (val != null) {
                    item.set(key, val);
                }
            }
            return item;
        }
    }
}
