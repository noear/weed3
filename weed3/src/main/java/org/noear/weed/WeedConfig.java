package org.noear.weed;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;
import org.noear.weed.ext.Fun1;
import org.noear.weed.wrap.NamingStrategy;
import org.noear.weed.wrap.TypeConverter;

import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by noear on 14/11/20.
 */
public final class WeedConfig {
    public static boolean isDebug = false;
    public static boolean isUsingValueExpression=true;
    public static boolean isUsingValueNull=false;
    public static boolean isUsingSchemaPrefix =false;
    public static boolean isUpdateMustConditional=true;
    public static boolean isDeleteMustConditional=true;
    public static boolean isUsingUnderlineColumnName=true;
    public static boolean isSelectItemEmptyAsNull=false;

    /**
     * 非注解的命名策略
     * */
    public static NamingStrategy namingStrategy = new NamingStrategy();

    /**
     * 字段类型转换器
     * */
    public static TypeConverter typeConverter  = new TypeConverter();

    /**
     * 链接工厂
     * */
    public static DbConnectionFactory connectionFactory = new DbConnectionFactory();

    public static Map<String, ICacheServiceEx> libOfCache = new ConcurrentHashMap<>();
    public static Map<String, DbContext> libOfDb = new ConcurrentHashMap<>();

    static Set<Act2<Command,Throwable>> onException_listener = new LinkedHashSet<>();
    static Set<Act1<Command>> onLog_listener = new LinkedHashSet<>();

    static Set<Act1<Command>>           onCommandBuilt_listener = new LinkedHashSet();

    //执行之前
    static Set<Fun1<Boolean,Command>>   onExecuteBef_listener = new LinkedHashSet<>();
    //执行声明
    static Set<Act2<Command,Statement>> onExecuteStm_listener = new LinkedHashSet<>();
    //执行之后
    static Set<Act1<Command>>           onExecuteAft_listener = new LinkedHashSet();


    protected static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

    protected static void runExceptionEvent(Command cmd, Throwable ex) {
        if (onException_listener.size() > 0) {
            if (cmd != null) {
                cmd.timestop = System.currentTimeMillis();
            }

            onException_listener.forEach(fun -> {
                fun.run(cmd, ex);
            });
        }
    }

    protected static void runCommandBuiltEvent(Command cmd) {
        if (onCommandBuilt_listener.size() > 0) {
            onCommandBuilt_listener.forEach(fun->{
                fun.run(cmd);
            });
        }
    }

    protected static boolean runExecuteBefEvent(Command cmd) {
        cmd.timestart = System.currentTimeMillis();

        VarHolder<Boolean> rst = new VarHolder<>();
        rst.value = true;

        if (onExecuteBef_listener.size()>0) {
            onExecuteBef_listener.forEach(fun->{
                rst.value = rst.value && fun.run(cmd);
            });
        }

        return rst.value;
    }

    protected static void runExecuteStmEvent(Command cmd, Statement stm) {
        if (onExecuteStm_listener.size() > 0) {
            onExecuteStm_listener.forEach(fun->{
                fun.run(cmd, stm);
            });
        }
    }

    protected static void runExecuteAftEvent(Command cmd) {
        try {
            if (cmd.onExecuteAft != null) {
                cmd.onExecuteAft.run(cmd);
                cmd.onExecuteAft = null; //执行之后，就会清掉
            }

            cmd.timestop = System.currentTimeMillis();

            if (onExecuteAft_listener.size() > 0) {
                onExecuteAft_listener.forEach(fun -> {
                    fun.run(cmd);
                });
            }

            if (cmd.isLog > 0 && onLog_listener.size() > 0) {
                onLog_listener.forEach(fun -> fun.run(cmd));
            }
        } catch (Throwable e) {
            //执行后，不能抛出异常，不然影响正常的工作流
            e.printStackTrace();
        }
    }


    //--------------------------------------
    //
    //
    /** 出现异常时 */
    public static void onException(Act2<Command,Throwable> listener)
    {
        onException_listener.add(listener);
    }

    /** 可以记日志时 */
    public static void onLog(Act1<Command> listener){
        onLog_listener.add(listener);
    }

    /** 命令构建完成时 */
    public static void onCommandBuilt(Act1<Command> listener){
        onCommandBuilt_listener.add(listener);
    }
    /** 执行之前 */
    public static void onExecuteBef(Fun1<Boolean,Command> listener){
        onExecuteBef_listener.add(listener);
    }
    /** 执行之中 */
    public static void onExecuteStm(Act2<Command,Statement> listener){
        onExecuteStm_listener.add(listener);
    }
    /** 执行之后 */
    public static void onExecuteAft(Act1<Command> listener){
        onExecuteAft_listener.add(listener);
    }

}
