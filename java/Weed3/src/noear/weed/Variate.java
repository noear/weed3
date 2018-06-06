package noear.weed;

import noear.weed.ext.DbType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by noear on 14-6-12.
 * 数据库访问参数（支付范型）
 */
public class Variate implements Serializable {
    protected String _name;
    protected Object _value;
    public final   int   _hash;

    protected  Variate(){
        _hash=0;
    }

    public Variate(String name, Object value) {
        this._name = name;
        this._value = value;

        if (name != null)
            this._hash = name.hashCode();
        else
            this._hash = 0;
    }

    public boolean isNull(){
        return _value == null;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }


    DbType _type = DbType.Object;
    public  DbType getType() {
        return _type;
    }

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    //--------------------
    public <T> T value(T def) {
        if (_value == null)
            return def;
        else {
            return (T) _value;
        }
    }

    public double doubleValue(double def){
        if(_value == null){
            return def;
        }

        if(_value instanceof Double){
            return (double)_value;
        }

        if(_value instanceof Float){
            return (double)((float)_value);
        }

        if(_value instanceof BigDecimal){
            return ((BigDecimal)_value).doubleValue();
        }

        if(_value instanceof Long){
            return (long)_value;
        }

        if(_value instanceof Integer){
            return (int)_value;
        }

        if(_value instanceof Date){
            return ((Date)_value).getTime();
        }

        return def;
    }

    public long longValue(long def){
        if(_value == null){
            return def;
        }

        if(_value instanceof Long){
            return (long)_value;
        }

        if(_value instanceof Integer){
            return (int)_value;
        }

        if(_value instanceof BigDecimal){
            return ((BigDecimal)_value).longValue();
        }

        if(_value instanceof Date){
            return ((Date)_value).getTime();
        }

        return def;
    }

    public int intValue(int def){
        if(_value == null){
            return def;
        }

        if(_value instanceof Integer){
            return (int)_value;
        }

        if(_value instanceof Long){
            return (int) ((long)_value);
        }

        if(_value instanceof BigDecimal){
            return ((BigDecimal)_value).intValue();
        }

        return def;
    }

    public String stringValue(String def){
        if(_value == null){
            return def;
        }

        if(_value instanceof String){
            return (String)_value;
        }else{
            return _value.toString();
        }
    }


//    private Object getDefault(DbType type)
//    {
//        if(type == DbType.String || type == type.DateTime)
//            return null;
//        else if(type == DbType.Int64)
//            return 0L;
//        else if(type == DbType.Double || type == DbType.Single)
//            return 0.0;
//        else if(type == DbType.Boolean)
//            return false;
//        else
//            return 0;
//    }
}
