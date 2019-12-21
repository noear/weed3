package org.noear.weed;

import org.noear.weed.annotation.Table;
import org.noear.weed.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 19-12-11.
 */
class BaseEntityWrap {
    public Class<?> entityClz;
    public String tableName;
    public String pkName;

    private static final String lock ="";
    private static Map<BaseMapper, BaseEntityWrap> _lib = new HashMap<>();
    public static BaseEntityWrap get(BaseMapper bm) {
        BaseEntityWrap tmp = _lib.get(bm);
        if (tmp == null) {
            synchronized (lock) {
                tmp = _lib.get(bm);
                if (tmp == null) {
                    tmp = new BaseEntityWrap(bm);
                    _lib.put(bm, tmp);
                }
            }
        }

        return tmp;
    }

    private BaseEntityWrap(BaseMapper baseMapper) {
        if(baseMapper instanceof BaseMapperWrap){
            entityClz = (Class<?>) ((BaseMapperWrap)baseMapper).entityType();
        }else{
            Type type = baseMapper.getClass().getInterfaces()[0].getGenericInterfaces()[0];
            entityClz = (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
        }

        if(entityClz == Object.class){
            throw new RuntimeException("请为BaseMapper申明实体类型");
        }

        Table ann = entityClz.getAnnotation(Table.class);
        if (ann != null) {
            tableName = ann.value();
        }

        if (tableName == null) {
            tableName = entityClz.getSimpleName();
        }

        for (Field f1 : entityClz.getDeclaredFields()) {
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
