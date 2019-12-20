package org.noear.weed.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Db;

import java.lang.annotation.Annotation;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {
        //测试
        Aop.factory().beanLoaderAdd(Db.class, (clz, bw, anno) -> {
            if (clz.isInterface()) {
                DbContext db = WeedConfig.libOfDb.get(anno.value());
                Object raw = db.mapper(clz);
                Aop.put(clz, raw);
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
                        DbContext db = WeedConfig.libOfDb.get(dbAnno.value());
                        Object raw = db.mapper(clz);
                        return raw;
                    }
                } else {
                    DbContext db = WeedConfig.libOfDb.get(dbAnno.value());
                    Object raw = db.mapper(clz);
                    Aop.put(clz, raw);
                    return raw;
                }
            }
            return null;
        });
    }
}
