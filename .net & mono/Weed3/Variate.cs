using System;
using System.Data;

namespace Noear.Weed {
    /**
 * Created by noear on 14-6-12.
 * 数据库访问参数
 */
    public class Variate {
        protected String _name;
        protected object _value;
        internal readonly  int   _hash;

        internal Variate() {
            _hash = 0;
        }

        public Variate(String name, Object value) { 
            this._name = name;
            this._value = value;

            if (name != null)
                this._hash = name.GetHashCode();
        }
        
        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }

        
        DbType _type = 0;
        public virtual DbType getType() {
            if (_type == 0) {
                var _typeName = _value.GetType().Name;
                _type = DbTypeMapping.GetType(_typeName);
            }
            return _type;
        }
        
        public virtual Object getValue() {
            return _value;
        }

        public virtual void setValue(Object value) {
            _value = value; 
        }

        //--------------------
        public T value<T>(T def) {
            if (_value == null)
                return def;
            else if (DBNull.Value.Equals(_value))
                return def;
            else
                return doValue<T>();
        }
        
        T doValue<T>() {
            try {
                return (T)_value;
            }
            catch (Exception) {
                if (_name != null)
                    throw new WeedException(_name + "::无法将类型为“" + _value.GetType().ToString() + "”的对象强制转换为类型“" + typeof(T).ToString() + "”");
                else
                    throw new WeedException("无法将类型为“" + _value.GetType().ToString() + "”的对象强制转换为类型“" + typeof(T).ToString() + "”");
            }
        }

        public long longValue(long def) {
            if (_value == null) {
                return def;
            }

            if (_value is long){
                return (long)_value;
            }

            if (_value is int){
                return (int)_value;
            }

            if (_value is Decimal){
                return (int)((Decimal)_value);
            }

            return def;
        }

        public int intValue(int def) {
            if (_value == null) {
                return def;
            }

            if (_value is int) {
                return (int)_value;
            }

            if (_value is long){
                return (int)((long)_value);
            }

            if (_value is Decimal){
                return (int)((Decimal)_value);
            }

            return def;
        }

        public double doubleValue(double def) {
            if (_value == null) {
                return def;
            }

            if (_value is double) {
                return (double)_value;
            }

            if (_value is long) {
                return (long)_value;
            }

            if (_value is int) {
                return (int)_value;
            }

            if (_value is Decimal) {
                return (double)((Decimal)_value);
            }

            return def;
        }
    }
}
