package noear.weed;

import noear.weed.ext.DbType;

/**
 * Created by noear on 14-6-12.
 * 数据库访问参数（支付范型）
 */
public class Variate {
    protected String _name;
    protected Object _value;
    public final   int   _hash;


    public Variate(String name, Object value) {
        this._name = name;
        this._value = value;

        if (name != null)
            this._hash = name.hashCode();
        else
            this._hash = 0;
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
        else
            return (T)_value;

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
