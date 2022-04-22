package org.noear.weed.xml;

import org.noear.weed.DataItem;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.IOUtils;
import org.w3c.dom.Node;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class XmlSqlBlock {
    private static final String _lock ="";
    public String _namespace;
    public String _classname;
    public StringBuilder _classcode;
    public StringBuilder _classcode2;
    public List<String> _import = new ArrayList<>();

    public String _id;
    public String _param; //（属性：外部输入变量申明；默认会自动生成）
    public String _declare; //（属性：内部变量类型预申明）
    public String _return;//（属性：返回类型）
    public String _return_item;

    public String _caching;
    public String _cacheClear;
    public String _cacheTag;
    public String _usingCache;

    //缓存标签集合
    public Map<String, XmlSqlVar> tagMap = new LinkedHashMap<>();

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
    /** 是否为select 操作 */
    public boolean isSelect(){
        return "SELECT".equals(_action);
    }

    /** 是否为 insert 操作 */
    public boolean isInsert(){
        return "INSERT".equals(_action);
    }
    //其它的操作不需要判断

    /** 变量标签集合 */
    private Map<String, XmlSqlVar> varMap = new LinkedHashMap<>();
    public Collection<XmlSqlVar> varList(){
        return Collections.unmodifiableCollection(varMap.values());
    }

    public void varPut(XmlSqlVar dv) {
        if (dv.type == null || dv.type.length() == 0) {
            varMap.putIfAbsent(dv.name, dv);
        }else {
            varMap.put(dv.name, dv);
        }
    }
    /** 变量记数器 */
    public int varNum = 0;

    //构建清除的cache tag
    public String formatRemoveTags(XmlSqlBlock block, Map map) {
        String txt2 = block._cacheClear;

        for (XmlSqlVar dv : block.tagMap.values()) {
            if (dv.label == 0) {
                Object val = map.get(dv.name);
                if (val == null) {
                    throw new RuntimeException("Parameter does not exist:@" + dv.name);
                }

                txt2 = txt2.replace(dv.mark, val.toString());
            }
        }

        return txt2;
    }

    //构建添加的cache tag
    public String formatAppendTags(XmlSqlBlock block, Map map, Object rst) {
        String txt2 = block._cacheTag;

        for (XmlSqlVar dv : block.tagMap.values()) {
            if (dv.label == 1) {
                //1.先从入参取值
                Object val = map.get(dv.name);

                if(val == null && rst!=null){
                    //尝试去结果取值（sql xml 输出的，只会是）
                    if(rst instanceof DataItem){
                        val = ((DataItem)rst).get(dv.name);
                    }
                }

                if (val == null) {
                    throw new RuntimeException("Parameter does not exist:@" + dv.name);
                }

                txt2 = txt2.replace(dv.mark, val.toString());
            }
        }

        return txt2;
    }

    public IXmlSqlBuilder builder;

    static final String java_types = ",char,boolean,short,int,long,float,double,Character,Boolean,Short,Integer,Long,Float,Double,Date,LocalDateTime,LocalTime,LocalDate,Object,";
    static final String weed_types = ",Map,MapList,DateItem,DateList,";
    public String newType(String type ) {
        if(type == null){
            return null;
        }

        if(_import == null || _import.size() == 0){
            return type;
        }

        if (type.indexOf(".") >= 0) {
            return type;
        }
        if (type.indexOf(">") >= 0) {
            return type;
        }
        if (java_types.indexOf("," + type + ",") >= 0) {
            return type;
        }
        if (weed_types.indexOf("," + type + ",") >= 0) {
            return type;
        }

        for(String pg : _import) {
            if (pg.endsWith("*")) {
                //如果是以 * 结尾的，且能拼出新类型
                String tm = pg.substring(0, pg.length() - 1) + type;
                if( IOUtils.loadClass(tm) != null){
                    return tm;
                }
            }else if(pg.endsWith(type)){
                //如果是以 xxx 结尾的
                return pg;
            }
        }

        return type;
    }
}
