package org.noear.weed;

import org.noear.weed.ext.Fun0;
import org.noear.weed.utils.EntityUtil;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.xml.IXmlSqlBuilder;
import org.noear.weed.xml.XmlSqlFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 14-6-12.
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
        return this;
    }

    @Override
    public DbProcedure set(String param, Fun0<Object> valueGetter) {
        _map.put(param,valueGetter.run());
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
    public DbProcedure setEntity(Object obj) throws RuntimeException ,ReflectiveOperationException{
        EntityUtil.fromEntity(obj,(k, v)->{
            _map.put(k,v);
        });
        return this;
    }

    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    protected Command getCommand(){
        Command cmd = new Command(this.context);

        cmd.key      = getCommandID();


        IXmlSqlBuilder xmlSqlBuilder = XmlSqlFactory.get(_sqlName);
        if(xmlSqlBuilder == null) {
            throw new RuntimeException("Xml sql @" + _sqlName + " does not exist");
        }

        SQLBuilder sqlBuilder = xmlSqlBuilder.build(_map);

        cmd.text = sqlBuilder.toString();
        cmd.paramS  = sqlBuilder.paramS;

        runCommandBuiltEvent(cmd);

        return cmd;
    }
}
