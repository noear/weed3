package weed3demo.mapper;

import org.w3c.dom.Document;

import java.util.*;

public class XmlSqlBlock {
    public String _namespace;
    public String _id;
    public String _declare;
    public String _return;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    public Document xmldoc;

    public String action;

    public Map<String, XmlSqlVar> varMap = new LinkedHashMap<>();
    public void varPut(XmlSqlVar dv) {
        if (dv.type == null || dv.type.length() == 0) {
            return;
        }

        varMap.put(dv.name, dv);
    }

    public IXmlSqlBuilder sqlBuilder;
}
