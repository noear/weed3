package org.noear.weed;

import org.noear.weed.annotation.DbTable;
import org.noear.weed.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class BaseTableEntity {
    public Class<?> entityClz;
    public String tableName;
    public String pkName;

    private static final String lock ="";
    private static Map<Class<?>,BaseTableEntity> _lib = new HashMap<>();
    public static BaseTableEntity get(BaseMapper baseMapper) {
        Class<?> clz = baseMapper.getClass();

        BaseTableEntity tmp = _lib.get(clz);
        if (tmp == null) {
            synchronized (lock) {
                tmp = _lib.get(clz);
                if (tmp == null) {
                    tmp = new BaseTableEntity(baseMapper);
                    _lib.put(clz, tmp);
                }
            }
        }

        return tmp;
    }

    private BaseTableEntity(BaseMapper baseMapper) {
        if(baseMapper instanceof BaseMapperWrap){
            entityClz = (Class<?>) ((BaseMapperWrap)baseMapper).entityType();
        }else{
            Type type = baseMapper.getClass().getInterfaces()[0].getGenericInterfaces()[0];
            entityClz = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
        }

        if(entityClz == Object.class){
            throw new RuntimeException("请为BaseMapper申明实体类型");
        }

        DbTable ann = entityClz.getAnnotation(DbTable.class);
        if (ann != null) {
            tableName = ann.value();
        }

        if (tableName == null) {
            tableName = entityClz.getSimpleName();
        }

        for (Field f1 : entityClz.getFields()) {
            if (f1.getAnnotation(PrimaryKey.class) != null) {
                pkName = f1.getName();
                break;
            }
        }

        if(pkName == null){
            throw new RuntimeException("没申明主键");
        }
    }
}
