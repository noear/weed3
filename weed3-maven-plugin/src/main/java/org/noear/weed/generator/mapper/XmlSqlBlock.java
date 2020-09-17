package org.noear.weed.generator.mapper;

import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

public class XmlSqlBlock {
    private static final String _lock ="";

    public String _namespace;
    public String _classname;
    public StringBuilder _classcode;

    public String _id;
    public String _param;
    public String _declare;
    public String _return;

    public String _remarks;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    //临时变量
    protected Map<String, Node> __nodeMap;

    public Map<String, XmlSqlVar> varMap = new LinkedHashMap<String, XmlSqlVar>();
    public void varPut(XmlSqlVar dv) {
        if (dv.type == null || dv.type.length() == 0) {
            return;
        }

        if(dv.type.indexOf(".")>0){
            TypeBlock tBlock = new TypeBlock(dv.type);
            if(tBlock.impType!=null) {
                impTypeSet.add(tBlock.impType);
            }

            dv.type = tBlock.newType;
        }

        varMap.put(dv.name, dv);
    }
    public Set<String> impTypeSet = new HashSet<>();

}
