package org.noear.weed;

import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.utils.ThrowableUtils;
import org.noear.weed.xml.XmlSqlBlock;
import org.noear.weed.xml.XmlSqlFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 19-10-14.
 * Xmlsql过程访问类
 */
public class DbXmlsqlProcedure extends DbProcedure {
    private String _sqlid;
    private Map<String,Object> _map = new HashMap<>(); //不能用:Variate, IXmlSqlBuilder 不支持

    public DbXmlsqlProcedure(DbContext context){
        super(context);
    }

    protected DbXmlsqlProcedure sql(String sqlid) {
        _sqlid = sqlid;

        this.commandText = sqlid;
        this.paramS.clear();
        this._weedKey = null;

        return this;
    }

    @Override
    public DbProcedure set(String param, Object value) {
        _map.put(param,value);
        return this;
    }

    @Override
    public DbProcedure setMap(Map<String, Object> map) {
        if (map != null) {
            map.forEach((k, v) -> {
                _map.put(k,v);
            });
        }
        return this;
    }

    @Override
    public DbProcedure setEntity(Object obj){
        EntityUtils.fromEntity(obj,(k, v)->{
            _map.put(k,v);
        });
        return this;
    }

    private void _onSet(String name, Object val){
        if("_tran".equals(name)){
        }
    }

    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    public String getWeedKey() {
        if(_weedKey==null)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(getCommandID()).append(":");

            for(Object p: _map.values()) {
                sb.append("_").append(p);
            }

            _weedKey= sb.toString();
        }
        return _weedKey;
    }

    @Override
    protected Command getCommand(){
        Command cmd = new Command(this.context);

        cmd.key      = getCommandID();


        XmlSqlBlock block = XmlSqlFactory.get(_sqlid);
        if(block == null || block.builder==null) {
            throw new RuntimeException("Sql @" + _sqlid + " does not exist");
        }

        SQLBuilder sqlBuilder = null;
        try {
            sqlBuilder = block.builder.build(_map);
        }catch (Throwable ex){
            System.out.println("[Weed] "+block.getClasscode(true));

            ex = ThrowableUtils.throwableUnwrap(ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }

        for (Object p1 : sqlBuilder.paramS) {
            doSet("", p1);
        }

        cmd.text = sqlBuilder.toString();
        cmd.paramS  = this.paramS;

        tryCacheController(cmd, block);

        runCommandBuiltEvent(cmd);

        return cmd;
    }

    /** 尝试缓存控制 */
    private void tryCacheController(Command cmd, XmlSqlBlock block) {
        //配置化缓存处理（有配置，并且未手动配置过缓存）...
        if (StringUtils.isEmpty(block._caching) == false && this._cache == null) {
            //寄存缓存对象
            cmd.cache = WeedConfig.libOfCache.get(block._caching);

            //如果不存在，则提示异常
            if (cmd.cache == null) {
                throw new RuntimeException("WeedConfig.libOfCache does not exist:@" + block._caching);
            }

            if (block.isSelect()) {
                //如果是查询，可以在处理之前添加控制
                this.caching(cmd.cache);

                if (StringUtils.isEmpty(block._usingCache) == false) {
                    this.usingCache(Integer.parseInt(block._usingCache));
                }

                if (StringUtils.isEmpty(block._cacheTag) == false) {
                    _cache.usingCache((uc, obj) -> {
                        Arrays.asList(block.formatAppendTags(block, _map, obj).split(",")).forEach((k) -> {
                            this.cacheTag(k.trim());
                        });
                    });
                }
            } else {
                if (StringUtils.isEmpty(block._cacheClear) == false) {
                    //如果是非查询，需要在执行后处理清理动作
                    cmd.onExecuteAft = (c) -> {
                        Arrays.asList(block.formatRemoveTags(block, _map).split(",")).forEach((k) -> {
                            c.cache.clear(k.trim());
                        });
                    };
                }
            }
        }
    }
}
