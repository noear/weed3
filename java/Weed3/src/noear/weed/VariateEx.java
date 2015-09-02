package noear.weed;

import noear.weed.ext.Act1;
import noear.weed.ext.Fun0;

/**
 * Created by yuety on 15/9/2.
 */
public class VariateEx extends Variate {
    protected Fun0<Object> _valueGetter;
    protected Act1<Object> _valueSetter;

    public VariateEx(String name,  Fun0<Object> valueGetter) {
        super(name, null);
        _valueGetter = valueGetter;
    }

    public VariateEx(String name, Fun0<Object> valueGetter, Act1<Object> valueSetter)  {
        super(name, null);
        this._valueGetter = valueGetter;
        this._valueSetter = valueSetter;
    }

    public  Object getValue() {
        return _value=_valueGetter.run();
    }

    public  DbType getType() {
        if (_value == null)
            _value = _valueGetter.run();

        return super.getType();
    }

    public  void setValue(Object value) {
        if (_valueSetter != null) {
            _valueSetter.run(value);
        }
    }
}