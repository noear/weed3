package org.noear.weed.xml;

import java.util.regex.Pattern;

public class XmlSqlVar {
    public String mark;
    public String name;
    public String type;

    /**
     * 标记，用于区分不同的变量类型
     * 0:cacheClear
     * 1:cacheTag
     * */
    public int label;

    public XmlSqlVar(){}
    public XmlSqlVar(String mark, String name, String type){
        this.mark = mark;
        this.name = name;
        this.type = type;
    }

    //替换形变量表达式
    public static final Pattern varRepExp = Pattern.compile("\\$\\{(.+?)\\}");
    //编译型变量表达式
    public static final Pattern varComExp = Pattern.compile("@\\{(.+?)\\}");
}
