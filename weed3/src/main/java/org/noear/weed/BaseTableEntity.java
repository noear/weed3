package org.noear.weed;

import org.noear.weed.annotation.DbTable;
import org.noear.weed.annotation.PrimaryKey;

import java.lang.reflect.Field;

class BaseTableEntity {
    public Class<?> entityType;
    public String tableName;
    public String pkName;

    public BaseTableEntity(BaseMapper baseMapper) {
        entityType = (Class<?>) baseMapper.entityType();

        DbTable ann = entityType.getAnnotation(DbTable.class);
        if (ann != null) {
            tableName = ann.value();
        }

        if (tableName == null) {
            tableName = entityType.getSimpleName();
        }

        for (Field f1 : entityType.getFields()) {
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
