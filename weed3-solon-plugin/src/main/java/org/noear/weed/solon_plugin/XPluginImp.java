package org.noear.weed.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.FieldWrap;
import org.noear.solon.core.XPlugin;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Db;

import java.lang.annotation.Annotation;
import java.util.Properties;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {

        Aop.factory().beanLoaderAdd(Db.class, (clz, bw, anno) -> {
            if (clz.isInterface()) {
                Object raw = getMapper(clz, anno, null);
                if (raw != null) {
                    Aop.put(clz, raw);
                }
            }
        });


        Aop.factory().beanBuilderAdd((clz, fw) -> {
            if (clz.isInterface()) {
                Db dbAnno = clz.getAnnotation(Db.class);
                if (dbAnno == null) {
                    if (fw.annoS != null) {
                        for (Annotation a1 : fw.annoS) {
                            if (a1.annotationType() == Db.class) {
                                dbAnno = (Db) a1;
                            }
                        }
                    }

                    if (dbAnno != null) {
                        return getMapper(clz, dbAnno, fw);
                    }
                } else {
                    Object raw = getMapper(clz, dbAnno, fw);
                    if (raw != null) {
                        Aop.put(clz, raw);
                    }
                    return raw;
                }
            }
            return null;
        });
    }

    public Object getMapper(Class<?> clz, Db anno, FieldWrap fw) {
        //1.先找bean
        DbContext db = Aop.get(anno.value());

        if (db == null) {
            //2.再找libOfDb
            db = WeedConfig.libOfDb.get(anno.value());
        }

        if (db == null) {
            //3.再找配置
            Properties tmp = XApp.cfg().getProp(anno.value());
            if (tmp != null && tmp.size() > 4) {
                db = new DbContext(tmp);
            }
        }

        if (db != null) {
            //生成mapper
            if (fw.genericType != null) {
                if (clz == BaseMapper.class) {
                    return db.mapperBase((Class<?>) fw.genericType.getActualTypeArguments()[0]);
                }
            } else {
                return db.mapper(clz);
            }
        }

        return null;
    }
}
