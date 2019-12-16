package org.noear.weed;

import org.noear.weed.cache.CacheState;
import org.noear.weed.ext.Act0;
import org.noear.weed.ext.Fun0;
import org.noear.weed.ext.Fun1;
import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 2017/7/22.
 */
public abstract class DbProcedure extends DbAccess<DbProcedure> {
    public DbProcedure(DbContext context) {
        super(context);
    }

    abstract public DbProcedure set(String param, Object value);
    abstract public DbProcedure setMap(Map<String, Object> map);
    abstract public DbProcedure setEntity(Object obj);

    public DbProcedure setIf(boolean condition, String param, Object value){
        if(condition){
            set(param,value);
        }
        return this;
    }
    public DbProcedure setMapIf(Map<String, Object> map, Fun2<Boolean,String,Object> condition){
        if(map!=null){
            map.forEach((k,v)->{
                if(condition.run(k,v)){
                    set(k,v);
                }
            });
        }
        return this;
    }
    public DbProcedure setEntityIf(Object obj, Fun2<Boolean,String,Object> condition) {
        EntityUtils.fromEntity(obj, (k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });
        return this;
    }

    /** 延后初始化接口 */
    private Act0 _lazyload;
    /** 是否已尝试延后加载 */
    private boolean _is_lazyload;

    protected void lazyload(Act0 action){
        _lazyload = action;
        _is_lazyload = false;
    }

    protected void tryLazyload() {
        if (_is_lazyload == false) {
            _is_lazyload = true;

            if (_lazyload != null) {
                _lazyload.run();
            }
        }
    }
}
