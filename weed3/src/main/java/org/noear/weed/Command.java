package org.noear.weed;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.ext.Act1;
import org.noear.weed.utils.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 14-9-5.
 * 命令
 */
public class Command {
    /**
     * 命令tag（用于寄存一些数据）
     */
    public String tag;
    /**
     * 是否进行日志
     */
    public int isLog; //def:0  no:-1 yes:1


    /**
     * 命令id
     */
    public String key;
    /**
     * 命令文本
     */
    public String text;
    /**
     * 命令参数
     */
    public List<Variate> paramS;
    /**
     * 数据库上下文（肯定且必须有）
     */
    public DbContext context;
    /**
     * 缓存服务对象（可能有，可能没有）
     */
    public ICacheServiceEx cache;

    /**
     * 数据处理事务
     */
    public DbTran tran;

    /**
     * 是否为批处理
     */
    public boolean isBatch = false;

    //计时变量
    public long timestart = 0;
    public long timestop = 0;

    public Command(DbContext context) {
        this.context = context;
        this.context.lastCommand = this;
        this.tran = tran;

        if (tran == null) {
            this.tran = DbTranUtil.current();
        }
    }

    private Map<String, Object> _paramMap;

    /**
     * 参数字典
     */
    public Map<String, Object> paramMap() {
        if (_paramMap == null) {
            _paramMap = new LinkedHashMap<>();

            int idx = 0;
            for (Variate v : paramS) {
                if (StringUtils.isEmpty(v._name)) {
                    _paramMap.put("v" + idx, v.getValue());
                } else {
                    _paramMap.put("v" + idx + "-" + v._name, v.getValue());
                }
                idx++;
            }
        }

        return _paramMap;
    }

    @Deprecated
    public String text2() {
        return toSqlString();
    }

    /**
     * 转为SQL字符串
     */
    public String toSqlString() {
        StringBuilder sb = new StringBuilder();

        if (isBatch) {
            sb.append(text);
            sb.append(" --:batch");
        } else {
            String[] ss = text.split("\\?");
            for (int i = 0, len = ss.length; i < len; i++) {
                sb.append(ss[i]);

                if (i < len) {
                    Variate val = paramS.get(i);

                    if (val.isNull()) {
                        sb.append("NULL");
                    } else if (val.getValue() instanceof String) {
                        sb.append("'").append(val.getString()).append("'");
                    } else if (val.getValue() instanceof Boolean) {
                        sb.append(val.getBoolean());
                    } else if (val.getValue() instanceof Date) {
                        sb.append("'").append(val.getDate()).append("'");
                    } else {
                        sb.append(val.getValue());
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * 执行时长
     */
    public long timespan() {
        return timestop - timestart;
    }

    /**
     * 完整的命令文本
     */
    public String fullText() {
        if (context.codeHint() == null)
            return context.getDialect().preReview(text);
        else
            return context.codeHint() + context.getDialect().preReview(text);
    }

    public Act1<Command> onExecuteAft = null;

}
