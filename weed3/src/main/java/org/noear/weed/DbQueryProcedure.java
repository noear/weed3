package org.noear.weed;

import org.noear.weed.ext.Act0;
import org.noear.weed.ext.Fun0;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.StringUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by noear on 17-6-12.
 * 查询过程访问类（模拟存储过程）
 */
public class DbQueryProcedure extends DbProcedure {

    private Map<String,Variate> _paramS2 = new HashMap<>();

    public DbQueryProcedure(DbContext context){
        super(context);
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

    //---------

    protected DbQueryProcedure sql(String sqlCode) {
        this.commandText = sqlCode;
        this.paramS.clear();
        this._weedKey = null;

        if(_lazyload == null) { //如果是后续加载的话，不能清掉这些参数
            this._paramS2.clear();
        }

        return this;
    }

    private  DbQueryProcedure doSqlItem(String sqlCode){
        this.commandText = sqlCode;
        this.paramS.clear();
        this._weedKey = null;

        return this;
    }

    private void set_do(String param, Object value){
        _paramS2.put(param,new Variate(param,value));
    }
    @Override
    public DbProcedure set(String param, Object value) {
        if(param.startsWith("@")){
            set_do(param.substring(1),value);
        }else{
            set_do(param,value);
        }

        return this;
    }

    @Override
    public DbProcedure set(String param, Fun0<Object> valueGetter) {
        if(param.startsWith("@")){
            _paramS2.put(param.substring(1), new VariateEx(param, valueGetter));
        }else{
            _paramS2.put(param, new VariateEx(param, valueGetter));
        }

        return this;
    }
    @Override
    public DbProcedure setMap(Map<String, Object> map) {
        if (map != null) {
            map.forEach((k, v) -> {
                set_do( k, v);
            });
        }
        return this;
    }
    @Override
    public DbProcedure setEntity(Object obj) {
        EntityUtils.fromEntity(obj,(k, v)->{
            set_do(k, v);
        });
        return this;
    }

    //
    //===========================================
    //
    @Override
    public String getWeedKey() {
        return buildWeedKey(_paramS2.values());
    }

    @Override
    protected String getCommandID() {
        tryLazyload();

        return this.commandText;
    }

    @Override
    protected Command getCommand() throws SQLException{
        tryLazyload();

        Command cmd = new Command(this.context, _tran);

        cmd.key      = getCommandID();

        String sqlTxt = this.commandText;

        {
            Map<String, String> tmpList = new HashMap<>();
            Pattern pattern = Pattern.compile("@(\\w+)|[@\\$]{1}\\{(\\w+)\\}");
            Matcher m = pattern.matcher(sqlTxt);
            while (m.find()) {
                String mark = m.group(0);
                String name = m.group(1);
                if(StringUtils.isEmpty(name)){
                    name = m.group(2);
                }

                if (WeedConfig.isDebug) {
                    if (_paramS2.containsKey(name) == false) {
                        throw new SQLException("Lack of parameter:" + name);
                    }
                }

                Variate val = _paramS2.get(name);
                Object tmp = val.getValue();
                if (tmp instanceof Iterable) { //支持数组型参数
                    StringBuilder sb = StringUtils.borrowBuilder();
                    for (Object p2 : (Iterable) tmp) {
                        doSet(new Variate(name, p2));//对this.paramS进行设值

                        sb.append("?").append(",");
                    }

                    int len = sb.length();
                    if (len > 0) {
                        sb.deleteCharAt(len - 1);
                    }

                    tmpList.put(mark, StringUtils.releaseBuilder(sb));
                } else {
                    if(mark.startsWith("@")){
                        doSet(val); //对this.paramS进行设值
                        tmpList.put(mark, "?");
                    }else{
                        tmpList.put(mark, val.stringValue(""));
                    }
                }
            }

            //按长倒排KEY
            List<String> keyList = new ArrayList<>(tmpList.keySet());
            Collections.sort(keyList, (o1, o2) -> {
                int len = o2.length() - o1.length();
                if (len > 0) {
                    return 1;
                } else if (len < 0) {
                    return -1;
                } else {
                    return 0;
                }
            });

            for (String key : keyList) {
                sqlTxt = sqlTxt.replace(key, tmpList.get(key));
            }
        }

        if(context.schemaHas()){
            sqlTxt.replace("$",context.schema());
        }

        cmd.paramS = this.paramS;
        
        cmd.text    = sqlTxt;

        runCommandBuiltEvent(cmd);

        return cmd;
    }

    @Override
    public int execute() throws SQLException {
        tryLazyload();

        if(context.allowMultiQueries){
            return super.execute();
        }else {
            int num = 0;
            String[] sqlList = commandText.split(";"); //支持多段SQL执行
            for (String sql : sqlList) {
                if (sql.length() > 10) {
                    doSqlItem(sql);

                    num += super.execute();
                }
            }

            return num;
        }
    }
}
