using System;
using System.Collections;
using System.Collections.Generic;

namespace Noear.Weed {
    public class DataItem : IDataItem {
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

        public void remove(String name) {
            _data.Remove(name);
        }

        public T toItem<T>(T item) where T : IBinder {
            item.bind((key) => {
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

        public long getLong(String name) {
            return (long)_data[name];
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
                } else {
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


        public String toJson() {
            _JsonWriter jw = new _JsonWriter();

            buildJson(jw);

            return jw.toJson();
        }

        internal void buildJson(_JsonWriter jw) {
            jw.WriteObjectStart();
            foreach (string key in keys()) {
                Object val = get(key);

                jw.WritePropertyName(key);

                if (val == null)
                    jw.WriteNull();

                if (val is String)
                    jw.WriteValue((String)val);

                if (val is DateTime)
                    jw.WriteValue((DateTime)val);

                if (val is Boolean)
                    jw.WriteValue((Boolean)val);

                if (val is Int32)
                    jw.WriteValue((Int32)val);

                if (val is Int64)
                    jw.WriteValue((Int64)val);

                jw.WriteValue(new Variate(null, val).doubleValue(0));

            }
            jw.WriteObjectEnd();
        }
    }
}
