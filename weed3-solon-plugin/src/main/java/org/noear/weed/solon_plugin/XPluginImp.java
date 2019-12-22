package org.noear.weed.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.*;
import org.noear.solon.ext.Act1;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;

import java.lang.annotation.Annotation;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {

        Aop.factory().beanLoaderAdd(Db.class, (clz, bw, anno) -> {
            if (clz.isInterface()) {
                getMapper(clz, anno, null, (raw)->{
                    Aop.put(clz, raw);
                });
            }
        });


        Aop.factory().beanBuilderAdd((clz, fwT) -> {
            if (clz.isInterface()) {
                Db dbAnno = clz.getAnnotation(Db.class);
                if (dbAnno == null) {
                    if (fwT.fw.annoS != null) {
                        for (Annotation a1 : fwT.fw.annoS) {
                            if (a1.annotationType() == Db.class) {
                                dbAnno = (Db) a1;
                            }
                        }
                    }

                    if (dbAnno != null) {
                        //不适合长期存在
                        getMapper(clz, dbAnno, fwT, (raw)->{
                            fwT.setValue(raw);
                        });
                        return true;
                    }
                } else {
                    //适合长期存在
                    getMapper(clz, dbAnno, fwT, (raw)->{
                        fwT.setValue(raw);
                        Aop.put(clz, raw);
                    });
                    return true;
                }
            }

            return false;
        });
    }

    public void getMapper(Class<?> clz, Db anno, FieldWrapTmp fwT, Act1<Object> callback) {
        Aop.getAsyn(anno.value(), (bw) -> {
            DbContext db = bw.raw();
            Object obj = null;
            //生成mapper
            if (fwT != null && fwT.fw.genericType != null) {
                if (clz == BaseMapper.class) {
                    obj = db.mapperBase((Class<?>) fwT.fw.genericType.getActualTypeArguments()[0]);
                }
            } else {
                obj = db.mapper(clz);
            }

            if (obj != null) {
                callback.run(obj);
            }
        });
    }
}
