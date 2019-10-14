package org.noear.weed;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 14-9-5.
 * 命令
 */
public class Command {
    /*命令tag（用于寄存一些数据）*/
    public String       tag;
    /*是否进行日志*/
    public int      isLog; //def:0  no:-1 yes:1


    /*命令id*/
    public String       key;
    /*命令文本*/
    public String       text;
    /*命令参数*/
    public List<Object> paramS;
    /*数据库上下文*/
    public DbContext context;

    //开始计时
    public long timestart = 0;
    public long timestop = 0;

    private Map<String,Object> _paramMap;
    public Map<String,Object> paramMap() {
        if(_paramMap == null) {
            _paramMap = new LinkedHashMap<>();

            int idx = 0;
            for (Object v : paramS) {
                _paramMap.put("v" + idx, v);
                idx++;
            }
        }

        return _paramMap;
    }

    //执行时长
    public long timespan(){
        return timestop  -timestart;
    }


    public Command(DbContext context) {
        this.context = context;
        this.context.lastCommand = this;
    }

    public String fullText() {
        if (context.codeHint() == null)
            return text;
        else
            return context.codeHint() + text;
    }

}
