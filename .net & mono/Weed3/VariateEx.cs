using System;
using System.Data;

namespace Noear.Weed {
    /**
 * Created by noear on 14-6-12.
 * 数据库访问参数（支付范型）
 */
    public class VariateEx : Variate {
        protected Func<Object>   _valueGetter;
        protected Action<Object> _valueSetter;

        public VariateEx(String name,  Func<Object> valueGetter) : base(name, null) {
            _valueGetter = valueGetter;
        }
        
        public VariateEx(String name, Func<Object> valueGetter, Action<Object> valueSetter) : base(name, null) {
            this._valueGetter = valueGetter;
            this._valueSetter = valueSetter;
        }

        public override Object getValue() {
            return _value=_valueGetter();
        }

        public override DbType getType() {
            if (_value == null)
                _value = _valueGetter();

            return base.getType();
        }

        public override void setValue(Object value) {
            if (_valueSetter != null) {
                _valueSetter(value);
            }
        }
    }
}
