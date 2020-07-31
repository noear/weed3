package org.noear.weed;

import org.noear.weed.wrap.DbVarType;

import java.io.Serializable;
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


    DbVarType _type = DbVarType.Object;
    public DbVarType getType() {
        return _type;
    }

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }


    public String getString(){
        return (String) _value;
    }

    public Date getDate(){
        return (Date) _value;
    }

    public Boolean getBoolean(){
        return (Boolean) _value;
    }

    public Number getNumber(){
        return (Number) _value;
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

        if(_value instanceof Number){
            return ((Number)_value).doubleValue();
        }

        if(_value instanceof Long){
            return (long)_value;
        }

        if(_value instanceof Integer){
            return (int)_value;
        }

        if(_value instanceof Boolean) {
            return ((boolean) _value) ? 1 : 0;
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

        if(_value instanceof Number){
            return ((Number)_value).longValue();
        }

        if(_value instanceof Boolean) {
            return ((boolean) _value) ? 1 : 0;
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

        if(_value instanceof Boolean) {
            return ((boolean) _value) ? 1 : 0;
        }

        if(_value instanceof Number){
            return ((Number)_value).intValue();
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
}
