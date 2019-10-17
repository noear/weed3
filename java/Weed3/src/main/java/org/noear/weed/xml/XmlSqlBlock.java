package org.noear.weed.xml;

import org.w3c.dom.Node;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlSqlBlock {
    private static final String _lock ="";
    public String _namespace;
    public String _classname;
    public StringBuilder _classcode;
    public StringBuilder _classcode2;

    public String _id;
    public String _declare;
    public String _return;

    public String _db;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    //临时变量
    protected Map<String, Node> __nodeMap;
    protected StringBuilder _texts = new StringBuilder();

    public StringBuilder getClasscode(boolean lineNo){
        if(lineNo){
            if(_classcode2==null){
                synchronized (_lock){
                    if(_classcode2 == null){
                        _classcode2 = new StringBuilder();
                        String[] ss = _classcode.toString().split("\n");
                        for(int i=0,len=ss.length; i<len; i++){
                            _classcode2.append(i+1).append(". ").append(ss[i]).append("\n");
                        }
                    }
                }
            }
            return _classcode2;
        }else{
            return _classcode;
        }
    }

    public String _action;
    public boolean isSelect(){
        return "SELECT".equals(_action);
    }

    public boolean isInsert(){
        return "INSERT".equals(_action);
    }

    public Map<String, XmlSqlVar> varMap = new LinkedHashMap<>();
    public void varPut(XmlSqlVar dv) {
        if (dv.type == null || dv.type.length() == 0) {
            return;
        }

        varMap.put(dv.name, dv);
    }

    public String format(String txt, Map map) {
        String txt2 = txt;
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher m = pattern.matcher(txt2);

        while (m.find()) {
            String mark = m.group(0);
            String name = m.group(1).trim();

            if (name.indexOf(":") > 0) {
                String[] kv = name.split(":");
                name = kv[0].trim();
            }

            Object val = map.get(name);
            if (val == null) {
                throw new RuntimeException("Parameter does not exist:@" + name);
            }

            txt2 = txt2.replace(mark, val.toString());
        }

        return txt2;
    }

    public IXmlSqlBuilder builder;
}
