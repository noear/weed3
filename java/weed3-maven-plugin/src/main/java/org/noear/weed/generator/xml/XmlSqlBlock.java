package org.noear.weed.generator.xml;

import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;

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
    public String _note;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    //临时变量
    protected Map<String, Node> __nodeMap;

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
