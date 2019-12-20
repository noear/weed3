package org.noear.weed;

import org.noear.weed.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 15/3/5.
 */
public class SQLBuilder {
    //当前数据
    public StringBuilder builder = new StringBuilder(200);
    public List<Object> paramS = new ArrayList<Object>();

    //备份数据
    private StringBuilder b_builder = new StringBuilder();
    private List<Object> b_paramS = new ArrayList<>();

    public int indexOf(String str) {
        return builder.indexOf(str);
    }

    public void clear() {
        builder.delete(0, builder.length());
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
        builder.append(b_builder);
        paramS.addAll(b_paramS);
    }

    public SQLBuilder insert(String code, Object... args) {
        SQLPartBuilder pb = new SQLPartBuilder(code, args);

        builder.insert(0, pb.code);
        paramS.addAll(0, pb.paramS);
        return this;
    }

    public SQLBuilder insert(SQLBuilder part) {
        builder.insert(0, part.builder);
        paramS.addAll(0, part.paramS);
        return this;
    }

    public SQLBuilder insert(int offset,  Object val){
        builder.insert(offset,val);
        return this;
    }

    public SQLBuilder append(String code, Object... args) {
        SQLPartBuilder pb = new SQLPartBuilder(code, args);

        builder.append(pb.code);
        paramS.addAll(pb.paramS);
        return this;
    }

    public SQLBuilder append(Object val) {
        builder.append(val);
        return this;
    }

    public SQLBuilder append(SQLBuilder part) {
        builder.append(part.builder);
        paramS.addAll(part.paramS);
        return this;
    }

    public SQLBuilder remove(int start, int length) {
        builder.delete(start, start + length);
        return this;
    }

    public SQLBuilder removeLast() {
        builder.setLength(builder.length() - 1);
        return this;
    }

    public SQLBuilder trimEnd(String str) {
        int len = str.length();
        if (len > 0) {
            String tmp = builder.toString().trim();

            while (true) {
                int idx = tmp.lastIndexOf(str);
                if (idx == tmp.length() - len) {
                    tmp = tmp.substring(0, tmp.length() - len);
                } else {
                    break;
                }
            }
            builder.setLength(0);
            builder.append(tmp);
        }

        return this;
    }


    public SQLBuilder trimStart(String str) {
        int len = str.length();
        if (len > 0) {
            String tmp = builder.toString().trim();

            while (true) {
                int idx = tmp.indexOf(str);
                if (idx == 0) {
                    tmp = tmp.substring(len);
                } else {
                    break;
                }
            }
            builder.setLength(0);
            builder.append(tmp);
        }
        return this;
    }

    //添加前缀
    public SQLBuilder addPrefix(String str) {
        builder.insert(0, str);
        return this;
    }

    //添加后缀
    public SQLBuilder addSuffix(String str) {
        builder.append(str);
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

        public SQLPartBuilder(String code, Object[] args) {

            paramS = new ArrayList<>();

            if (args != null && args.length > 0) {
                StringBuilder builder = StringUtils.borrowBuilder();
                builder.append(code);
                for (Object p1 : args) {
                    if (p1 instanceof Iterable) { //将数组转为单体
                        StringBuilder sb = StringUtils.borrowBuilder();
                        for (Object p2 : (Iterable) p1) {
                            paramS.add(p2);
                            sb.append("?").append(",");
                        }

                        int len = sb.length();
                        if (len > 0) {
                            sb.deleteCharAt(len - 1);
                        }

                        int idx = builder.indexOf("?...");
                        String tmp = StringUtils.releaseBuilder(sb);

                        //imporved by Yukai
                        if (len == 0) {
                            builder.replace(idx, idx + 4, "null");
                        } else {
                            builder.replace(idx, idx + 4, tmp);
                        }
                    } else if (p1 instanceof DbQuery) {

                        DbQuery s1 = (DbQuery) p1;

                        for (Variate p2 : s1.paramS) {
                            paramS.add(p2.getValue());
                        }

                        int idx = builder.indexOf("?...");
                        if (s1.paramS.size() > 0)
                            builder.replace(idx, idx + 4, s1.commandText);
                        else
                            builder.replace(idx, idx + 4, s1.commandText);
                    } else {
                        paramS.add(p1);
                    }
                }

                this.code = StringUtils.releaseBuilder(builder);
            } else {
                this.code = code;
            }
        }
    }
}