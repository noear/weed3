package org.noear.weed.xml;

import org.w3c.dom.Document;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlSqlBlock {
    public String _namespace;
    public String _classname;

    public String _id;
    public String _declare;
    public String _return;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    public String action;

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
