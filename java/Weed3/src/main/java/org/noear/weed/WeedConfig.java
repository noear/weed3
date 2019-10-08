package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act2;
import org.noear.weed.ext.Act3;
import org.noear.weed.ext.Fun1;

import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by noear on 14/11/20.
 */
public final class WeedConfig {
    public static boolean isDebug = false;
    public static boolean isUsingValueExpression=true;
    public static boolean isUsingValueNull=false;
    public static boolean isUsingTableSpace=false;
    public static boolean isUpdateMustConditional=true;

    static Set<Act2<Command,Exception>> onException_listener = new LinkedHashSet<>();
    static Set<Act1<Command>> onLog_listener = new LinkedHashSet<>();

    //执行之前
    static Set<Fun1<Boolean,Command>>   onExecuteBef_listener = new LinkedHashSet<>();
    //执行声明
    static Set<Act3<Command,Statement,DbTran>> onExecuteStm_listener = new LinkedHashSet<>();
    //执行之后
    static Set<Act1<Command>>           onExecuteAft_listener = new LinkedHashSet();


    protected static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

    protected static void runExceptionEvent(Command cmd, Exception ex) {
        if (onException_listener.size() > 0) {
            cmd.timestop = System.currentTimeMillis();

            onException_listener.forEach(fun->{
                fun.run(cmd,ex);
            });
        }
    }

    protected static boolean runExecuteBefEvent(Command cmd) {
        cmd.timestart = System.currentTimeMillis();

        if (onExecuteBef_listener.size()>0) {
            onExecuteBef_listener.forEach(fun->fun.run(cmd));
        }

        return true;
    }

    protected static void runExecuteStmEvent(Command cmd, Statement stm, DbTran tran) {
        if (onExecuteStm_listener.size() > 0) {
            onExecuteStm_listener.forEach(fun->{
                fun.run(cmd, stm, tran);
            });
        }
    }

    protected static void runExecuteAftEvent(Command cmd) {
        cmd.timestop = System.currentTimeMillis();

        if (onExecuteAft_listener.size() > 0) {
            onExecuteAft_listener.forEach(fun->{
                fun.run(cmd);
            });
        }

        if (cmd.isLog > 0 && onLog_listener.size()>0) {
            onLog_listener.forEach(fun->fun.run(cmd));
        }
    }




    //--------------------------------------
    //
    //

    public static void onException(Act2<Command,Exception> listener)
    {
        onException_listener.add(listener);
    }

    public static void onLog(Act1<Command> listener){
        onLog_listener.add(listener);
    }


    //执行之前
    public static void onExecuteBef(Fun1<Boolean,Command> listener){
        onExecuteBef_listener.add(listener);
    }
    //执行声明
    public static void onExecuteStm(Act3<Command,Statement,DbTran> listener){
        onExecuteStm_listener.add(listener);
    }
    //执行之后
    public static void onExecuteAft(Act1<Command> listener){
        onExecuteAft_listener.add(listener);
    }

}
