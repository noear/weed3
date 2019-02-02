package noear.weed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 15/3/5.
 */
public class SQLBuilder {
    private StringBuilder builder = new StringBuilder();
    public List<Object> paramS = new ArrayList<Object>();

    StringBuilder b_builder = new StringBuilder();
    List<Object> b_paramS = new ArrayList<>();

    public int indexOf(String str){
        return builder.indexOf(str);
    }

    public void clear()
    {
        builder.delete(0,builder.length());
        paramS.clear();
    }

    //备分状态
    protected void backup() {
        b_builder.append(builder.toString());
        b_paramS.addAll(paramS);
    }
    //还原状态
    protected void restore() {
        clear();
        builder.append(b_builder.toString());
        paramS.addAll(b_paramS);
    }

    public SQLBuilder insert(String code,  Object... args) {
        SQLPartBuilder pb = new SQLPartBuilder(code, args);

        builder.insert(0, pb.code);
        paramS.addAll(0, pb.paramS);
        return this;
    }

    public SQLBuilder append(String code,  Object... args) {
        SQLPartBuilder pb = new SQLPartBuilder(code, args);

        builder.append(pb.code);
        paramS.addAll(pb.paramS);
        return this;
    }

    public SQLBuilder remove(int start, int length) {
        builder.delete(start, start+ length);
        return this;
    }

    public SQLBuilder removeLast(){
        builder.deleteCharAt(builder.length() - 1);
        return this;
    }

    public int length() {
        return builder.length();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    //部分构建
    protected class SQLPartBuilder {
        public String code;
        public List<Object> paramS;

        public SQLPartBuilder(String code,  Object[] args) {

            paramS = new ArrayList<Object>();

            if (args.length > 0) {
                StringBuilder builder = new StringBuilder(code);
                for (Object p1 : args) {
                    if (p1 instanceof Iterable) { //将数组转为单体
                        StringBuilder sb = new StringBuilder();
                        for (Object p2 : (Iterable) p1) {
                            paramS.add(p2);
                            sb.append("?").append(",");
                        }

                        int len = sb.length();
                        if (len > 0) {
                            sb.deleteCharAt(len - 1);
                        }

                        int idx = builder.indexOf("?...");

                        //imporved by Yukai
                        if (len == 0) {
                            builder.replace(idx, idx + 4, "null");
                        } else {
                            builder.replace(idx, idx + 4, sb.toString());
                        }
                    }
                    else if (p1 instanceof DbQuery) {

                        DbQuery s1 = (DbQuery)p1;

                        for (Variate p2 : s1.paramS) {
                            paramS.add(p2.getValue());
                        }

                        int idx = builder.indexOf("?...");
                        if (s1.paramS.size() > 0)
                            builder.replace(idx, idx + 4, s1.commandText);
                        else
                            builder.replace(idx, idx + 4, s1.commandText);
                    }
                    else {
                        paramS.add(p1);
                    }
                }

                this.code = builder.toString();
            }
            else {
                this.code = code;
            }
        }
    }
}