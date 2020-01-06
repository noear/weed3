package org.noear.weed.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.*;
import org.noear.solon.ext.Act1;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {
        Aop.factory().beanCreatorAdd(Db.class, (clz, bw, anno) -> {
            if (clz.isInterface()) {
                getMapper(clz, anno, null, (raw) -> {
                    Aop.put(clz, raw);
                });
            }
        });


        Aop.factory().beanInjectorAdd(Db.class, (fwT, anno) -> {
            if (fwT.getType().isInterface()) {
                getMapper(fwT.getType(), anno, fwT, (raw) -> {
                    fwT.setValue(raw);
                });
            }
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
