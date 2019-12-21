package org.noear.weed.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Db;

import java.lang.annotation.Annotation;
import java.util.Properties;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {
        //测试
        Aop.factory().beanLoaderAdd(Db.class, (clz, bw, anno) -> {
            if (clz.isInterface()) {
                Object raw = getMapper(clz, anno.value());
                if (raw != null) {
                    Aop.put(clz, raw);
                }
            }
        });


        Aop.factory().beanBuilderAdd((clz, annoS) -> {
            if (clz.isInterface()) {
                Db dbAnno = clz.getAnnotation(Db.class);
                if (dbAnno == null) {
                    if (annoS != null) {
                        for (Annotation a1 : annoS) {
                            if (a1.annotationType() == Db.class) {
                                dbAnno = (Db) a1;
                            }
                        }
                    }

                    if (dbAnno != null) {
                        return getMapper(clz, dbAnno.value());
                    }
                } else {
                    Object raw = getMapper(clz, dbAnno.value());
                    if (raw != null) {
                        Aop.put(clz, raw);
                    }
                    return raw;
                }
            }
            return null;
        });
    }

    public Object getMapper(Class<?> clz, String name) {
        DbContext db = WeedConfig.libOfDb.get(name);

        if (db == null) {
            Properties tmp = XApp.cfg().getProp(name);
            if (tmp != null && tmp.size() > 4) {
                db = new DbContext(tmp);
            }
        }

        if (db != null) {
            return db.mapper(clz);
        } else {
            return null;
        }
    }
}
