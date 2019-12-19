package org.noear.weed.wrap;

import org.noear.weed.annotation.Exclude;
import org.noear.weed.annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Field wrap
 * */
public class FieldWrap {
    public final Field field;
    public final String name;
    public final boolean exclude;

    private Method _getter;
    private Method _setter;

    protected FieldWrap(Class<?> clz, Field f1) {
        field = f1;
        exclude = (f1.getAnnotation(Exclude.class) != null);

        String nameTmp = null;
        Column fn = f1.getAnnotation(Column.class);
        if (fn != null) {
            nameTmp = fn.value();
        }else{
            nameTmp = f1.getName();
        }

        name = nameTmp;

        _getter = findGetter(clz, f1);
        _setter = findSetter(clz, f1);
    }

    public Object getValue(Object tObj) throws ReflectiveOperationException{
        if(_getter == null){
            return field.get(tObj);
        }else{
            return _getter.invoke(tObj);
        }
    }

    public void setValue(Object tObj, Object val) throws ReflectiveOperationException{
        val = typeChange(val,field.getType());

        if(_setter == null){
            field.set(tObj,val);
        }else{
            _setter.invoke(tObj, new Object[]{val});
        }
    }

    private static Method findGetter(Class<?> tCls,Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "get" + firstLetter + fieldName.substring(1);

        try {
            Method getFun = tCls.getMethod(setMethodName);
            if (getFun != null) {
                return getFun;
            }
        } catch (NoSuchMethodException ex) {

        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static Method findSetter(Class<?> tCls, Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + fieldName.substring(1);

        try {
            Method setFun = tCls.getMethod(setMethodName, new Class[]{field.getType()});
            if (setFun != null) {
                return setFun;
            }
        } catch (NoSuchMethodException ex) {

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static Object typeChange(Object val, Class<?> type){
        if(val instanceof BigDecimal){
            if(Long.class == type || Long.TYPE == type){
                return ((BigDecimal)val).longValue();
            }

            if(Integer.class == type || Integer.TYPE == type){
                return ((BigDecimal)val).intValue();
            }

            if(Double.class == type || Double.TYPE == type){
                return ((BigDecimal)val).doubleValue();
            }
        }

        if(val instanceof java.sql.Timestamp){
            if(type == LocalDateTime.class){
                return ((Timestamp)val).toLocalDateTime();
            }
        }

        if(val instanceof java.sql.Date){
            if(type == LocalDate.class){
                return ((Date)val).toLocalDate();
            }
        }

        if(val instanceof java.sql.Time){
            if(type == LocalTime.class){
                return ((Time)val).toLocalTime();
            }
        }

        return val;
    }
}
