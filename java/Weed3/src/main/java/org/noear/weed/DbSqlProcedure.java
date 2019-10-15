package org.noear.weed;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.ext.Fun0;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.xml.XmlSqlBlock;
import org.noear.weed.xml.XmlSqlFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 19-10-14.
 * SQL过程访问类
 */
public class DbSqlProcedure extends DbProcedure {
    private String _sqlName;
    private Map<String,Object> _map = new HashMap<>();

    public DbSqlProcedure(DbContext context){
        super(context);
    }

    protected DbSqlProcedure sql(String sqlName) {
        _sqlName = sqlName;

        this.commandText = sqlName;
        this.paramS.clear();
        this._weedKey = null;

        return this;
    }

    @Override
    public DbProcedure set(String param, Object value) {
        _map.put(param,value);
        _onSet(param,value);
        return this;
    }

    @Override
    public DbProcedure set(String param, Fun0<Object> valueGetter) {
        throw new RuntimeException("DbSqlProcedure not support set(name,valueGetter)");
    }

    @Override
    public DbProcedure setMap(Map<String, Object> map) {
        if (map != null) {
            map.forEach((k, v) -> {
                _map.put(k,v);
                _onSet(k,v);
            });
        }
        return this;
    }

    @Override
    public DbProcedure setEntity(Object obj) throws RuntimeException ,ReflectiveOperationException{
        EntityUtils.fromEntity(obj,(k, v)->{
            _map.put(k,v);

            _onSet(k,v);
        });
        return this;
    }

    private void _onSet(String name, Object val){
        if("_tran".equals(name)){
            if(val instanceof DbTran){
                this.tran((DbTran)val);
            }

            if(val instanceof DbTranQueue){
                this.tran((DbTranQueue)val);
            }
        }
    }

    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    protected Command getCommand(){
        Command cmd = new Command(this.context,_tran);

        cmd.key      = getCommandID();


        XmlSqlBlock block = XmlSqlFactory.get(_sqlName);
        if(block == null || block.builder==null) {
            throw new RuntimeException("Xml sql @" + _sqlName + " does not exist");
        }

        SQLBuilder sqlBuilder = null;
        try {
            sqlBuilder = block.builder.build(_map);
        }catch (Throwable ex){
            System.out.println(block.getClasscode(true));
            throw ex;
        }

        cmd.text = sqlBuilder.toString();
        cmd.paramS  = sqlBuilder.paramS;

        tryCacheController(block);

        runCommandBuiltEvent(cmd);

        return cmd;
    }

    /** 尝试缓存控制 */
    private void tryCacheController(XmlSqlBlock block){
        //配置化缓存处理（有配置，并且未手动配置过缓存）...
        if(StringUtils.isEmpty(block._caching) == false && this._cache == null){
            ICacheServiceEx cache = WeedConfig.libOfCache.get(block._caching);

            if(cache == null){
                throw new RuntimeException("WeedConfig.libOfCache does not exist:@"+block._caching);
            }

            if("SELECT".equals(block.action)){
                this.caching(cache);

                if(StringUtils.isEmpty(block._usingCache) == false){
                    this.usingCache(Integer.parseInt(block._usingCache));
                }

                if(StringUtils.isEmpty(block._cacheTag) == false){
                    Arrays.asList(block.format(block._cacheTag, _map).split(",")).forEach((k)->{
                        this.cacheTag(k.trim());
                    });

                }
            }else{
                if(StringUtils.isEmpty(block._cacheClear) == false){
                    Arrays.asList(block.format(block._cacheClear, _map).split(",")).forEach((k)->{
                        cache.clear(k.trim());
                    });
                }
            }
        }
    }
}
