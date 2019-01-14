package noear.weed.utils;

import noear.weed.DataItem;
import noear.weed.annotation.DbField;
import noear.weed.ext.Act2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EntityUtil {
    //为设置和获取的函数进行缓存
    private static Map<Field,Method> _fieldSetLib = new HashMap<>();
    private static Map<Field,Method> _fieldGetLib = new HashMap<>();

    public static void fromEntity(Object obj, Act2<String,Object> setter) throws ReflectiveOperationException{
        Class<?> cls = obj.getClass();
        Field[] fields = obj.getClass().getDeclaredFields();
        DbField fa;
        Object val;

        for (Field f : fields) {
            fa = f.getAnnotation(DbField.class);

            if (fa == null || fa.exclude() == false) {
                val = EntityUtil.getFieldValue(cls, obj, f);
                if(val != null) {
                    setter.run(f.getName(), val);
                }
            }
        }
    }

    public  static  <T> T toEntity(Class<T> cls, Field[] fields, DataItem data) throws ReflectiveOperationException{
        String key = null;
        T item = cls.newInstance();

        for (Field f : fields) {
            key = f.getName();

            if (data.exists(key)) {
                EntityUtil.setFieldValue(cls,item,f,data.get(key));
            }
        }

        return item;
    }

    public static Object getFieldValue(Class<?> tCls,Object tObj,Field field) throws ReflectiveOperationException{
        if(_fieldGetLib.containsKey(field)){
            Method getFun = _fieldGetLib.get(field);
            if(getFun == null){
                return field.get(tObj);
            }else{
                return getFun.invoke(tObj);
            }
        }

        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "get" + firstLetter + fieldName.substring(1);

        try {
            Method getFun = tCls.getMethod(setMethodName);
            if(getFun !=null) {
                _fieldGetLib.put(field,getFun);
                return getFun.invoke(tObj);
            }
        }
        catch (NoSuchMethodException ex){
            _fieldGetLib.put(field,null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return field.get(tObj);
    }

    public static void setFieldValue(Class<?> tCls,Object tObj,Field field, Object val) throws ReflectiveOperationException {
        if(_fieldSetLib.containsKey(field)){
            Method setFun = _fieldSetLib.get(field);
            if(setFun == null){
                field.set(tObj,val);
            }else{
                setFun.invoke(tObj, new Object[]{val});
            }
            return;
        }

        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + fieldName.substring(1);


        try {
            Method setFun = tCls.getMethod(setMethodName, new Class[]{field.getType()});
            if(setFun !=null) {
                _fieldSetLib.put(field,setFun);
                setFun.invoke(tObj, new Object[]{val});
                return;
            }
        }
        catch (NoSuchMethodException ex){
            _fieldSetLib.put(field,null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        field.set(tObj,val);
    }
}
