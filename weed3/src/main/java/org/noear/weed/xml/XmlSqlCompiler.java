package org.noear.weed.xml;

import org.noear.weed.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlSqlCompiler {

    //将xml解析为java code
    public static String parse(URL xmlFile) throws Exception{
        if(xmlFile == null){
            return null;
        }


        Document doc = parseDoc(xmlFile);

        Node nm = doc.getDocumentElement();

        String filepath = xmlFile.getPath();
        int filename_idx = filepath.lastIndexOf("/")+1;
        String filename = filepath.substring(filename_idx);

        String namespace = attr(nm, "namespace");
        String _import = attr(nm, "import");

        String classname = filename.replace(".","_"); //namespace.replace(".","_"); //"weed_xml_sql";

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(namespace).append(";\n\n");

        sb.append("import java.math.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("import org.noear.weed.utils.*;\n");
        sb.append("import org.noear.weed.SQLBuilder;\n");
        sb.append("import org.noear.weed.xml.XmlSqlFactory;\n");
        if(StringUtils.isEmpty(_import) == false) {
            String[] ss = _import.split(";");
            for (String s : ss) {
                if (s.length() > 2) {
                    sb.append("import ").append(s).append(";\n");
                }
            }
        }
        sb.append("\n");

        Map<String,Node> node_map = new HashMap<>();
        NodeList sql_list = doc.getElementsByTagName("sql");
        for (int i = 0, len = sql_list.getLength(); i < len; i++) {
            Node n = sql_list.item(i);
            String id = attr(n,"id");
            if(id!=null){
                node_map.put(id,n);
            }
        }

        sb.append("public class ").append(classname).append("{");

        //构造函数
        newLine(sb, 1).append("private static final String _namespace=\"").append(namespace).append("\";");
        newLine(sb, 1).append("public ").append(classname).append("(){");

        for (int i = 0, len = sql_list.getLength(); i < len; i++) {
            Node n = sql_list.item(i);
            String id_str = attr(n,"id");
            if(id_str!= null){
                newLine(sb,2).append("XmlSqlFactory.register(_namespace + \".")
                        .append(id_str).append("\",")
                        .append("this::").append(id_str).append(");");

            }
        }

        newLine(sb, 1).append("}");

        //代码码函数
        for (int i = 0, len = sql_list.getLength(); i < len; i++) {
            Node n = sql_list.item(i);
            parseSqlNode(node_map, sb, n, _import, namespace, classname);
        }

        sb.append("}\n");

        node_map.clear();

        return sb.toString();
    }

    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;
    //xml:解析文档
    private static Document parseDoc(URL xmlFile) throws Exception{
        if(dbf ==null) {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        }

        return db.parse(xmlFile.openStream());
    }

    //xml:解析 sql 指令节点
    private static void parseSqlNode(Map<String,Node> nodeMap, StringBuilder sb,Node n, String _import, String namespace,  String classname) {
        int depth = 1;
        XmlSqlBlock dblock = new XmlSqlBlock();

        dblock.__nodeMap = nodeMap;

        if (_import != null) {
            for (String s : _import.split(";")) {
                dblock._import.add(s.trim());
            }
        }

        dblock._namespace = namespace;
        dblock._classname = classname;
        dblock._classcode = sb;

        dblock._id = attr(n, "id");

        dblock._param = attr(n, ":param");
        dblock._declare = attr(n, ":declare");
        dblock._return = attr(n, ":return");
        if (dblock._return != null && dblock._return.indexOf("[") > 0) {
            dblock._return = dblock._return.replace("[", "<")
                    .replace("]", ">");

            int start = dblock._return.indexOf("<");
            int end = dblock._return.indexOf(">");
            if (start >= 0 && end > start) {
                dblock._return_item = dblock._return.substring(start + 1, end);
            }
        }

        dblock._caching = attr(n, ":caching");
        dblock._usingCache = attr(n, ":usingCache");
        dblock._cacheTag = attr(n, ":cacheTag");
        dblock._cacheClear = attr(n, ":cacheClear");

        //构建需要申明的变量
        _parseSqlDeclare(dblock);
        _parseSqlCachaTag(dblock);

        newLine(sb, depth).append("public SQLBuilder ").append(dblock._id).append("(Map map) throws Exception{");

        //构建代码体和变量
        StringBuilder sb2 = new StringBuilder();
        {
            newLine(sb2, depth + 1).append("SQLBuilder sb = new SQLBuilder();\n");
            _parseNodeList(n.getChildNodes(), "sb", sb2, dblock, depth + 1);
        }

        //1.打印变量
        int var_num = 0;
        for (XmlSqlVar dv : dblock.varMap.values()) {
            if (dv.type != null && dv.type.length() > 0) {
                var_num++;
                newLine(sb, depth + 1)
                        .append(dv.type).append(" ").append(dv.name).append(" = ")
                        .append("(").append(dv.type).append(")map.get(\"").append(dv.name).append("\");");
            }
        }

        if (var_num > 0) {
            sb.append("\n");
        }

        //2.打印代码体
        sb.append(sb2);

        //3.打印预处理变量（cache tag 用到的，带.的变量）
        if (dblock.tagMap.size() > 0) {
            sb.append("\n");
        }
        for (XmlSqlVar dv : dblock.tagMap.values()) {
            if (dv.name.indexOf(".") > 0) {
                newLine(sb, depth + 1)
                        .append("map.put(\"").append(dv.name).append("\", ")
                        .append(dv.name).append(");");
            }
        }

        //4.结束并返回
        sb.append("\n");
        newLine(sb, depth + 1).append("return sb;");
        newLine(sb, depth).append("}\n");

        dblock.__nodeMap = null;

        //0.确定动作
        {
            String txt2 = dblock._texts.insert(0, "# ").toString().trim().toUpperCase();

            if (dblock._action == null && txt2.indexOf(" INSERT ") > 0) {
                dblock._action = "INSERT";
            }

            if (dblock._action == null && txt2.indexOf(" DELETE ") > 0) {
                dblock._action = "DELETE";
            }

            if (dblock._action == null && txt2.indexOf(" UPDATE ") > 0) {
                dblock._action = "UPDATE";
            }

            if (dblock._action == null && txt2.indexOf(" SELECT ") > 0) {
                dblock._action = "SELECT";
            }

            dblock._texts = null;
        }

        //注册块
        String tmp = dblock._return_item;
        dblock._return_item = dblock.newType(dblock._return_item);
        dblock._return = dblock.newType(dblock._return);
        if (tmp != null && tmp.equals(dblock._return_item) == false) {
            //把List<User> 转为 List<xx.xx.User>
            dblock._return = dblock._return.replace("<" + tmp + ">", "<" + dblock._return_item + ">");
        }

        XmlSqlFactory.register(namespace + "." + dblock._id, dblock);
    }

    /** 解析申明的变量 */
    private static void _parseSqlDeclare(XmlSqlBlock dblock) {
        //（属性：外部输入变量申明；默认会自动生成）
        if (dblock._param != null) {
            String[] ss = dblock._param.split(",");
            for (int i = 0, len = ss.length; i < len; i++) {
                String tmp = ss[i].trim();
                if (tmp.indexOf(":") > 0 && tmp.length() > 3) {
                    String[] kv = tmp.split(":");

                    XmlSqlVar dv = new XmlSqlVar(tmp, kv[0].trim(), kv[1].trim());
                    dblock.varPut(dv);
                }
            }
        }

        //（属性：内部变量类型预申明）
        if (dblock._declare != null) {
            String[] ss = dblock._declare.split(",");
            for (int i = 0, len = ss.length; i < len; i++) {
                String tmp = ss[i].trim();
                if (tmp.indexOf(":") > 0 && tmp.length() > 3) {
                    String[] kv = tmp.split(":");

                    XmlSqlVar dv = new XmlSqlVar(tmp, kv[0].trim(), kv[1].trim());
                    dblock.varPut(dv);
                }
            }
        }
    }

    /** 解析缓存标签 */
    private static void _parseSqlCachaTag(XmlSqlBlock dblock) {
        if (dblock._cacheClear != null) {
            Matcher m = XmlSqlVar.varRepExp.matcher(dblock._cacheClear);

            while (m.find()) {
                XmlSqlVar dv = parseTxtVar(m);
                dv.label = 0;//cache clear
                dblock.tagMap.put(dv.mark, dv);
            }
        }

        if (dblock._cacheTag != null) {
            Matcher m = XmlSqlVar.varRepExp.matcher(dblock._cacheTag);

            while (m.find()) {
                XmlSqlVar dv = parseTxtVar(m);
                dv.label = 1; //cache tag
                dblock.tagMap.put(dv.mark, dv);
            }
        }
    }

    private static void _parseNodeList(NodeList nl, String sqlBuilderName,  StringBuilder sb, XmlSqlBlock dblock, int depth) {
        for (int i = 0, len = nl.getLength(); i < len; i++) {
            Node n = nl.item(i);

            _parseNode(n,sqlBuilderName, sb,dblock,depth);
        }
    }

    private static void _parseNode(Node n, String sqlBuilderName, StringBuilder sb, XmlSqlBlock dblock,  int depth){
        int type = n.getNodeType();

        if (type == 3 || type == 4) {//text or CDATA
            String text = n.getTextContent().trim();

            if (text.length() > 0) {
                newLine(sb, depth).append(sqlBuilderName).append(".append(");
                parseTxt(sb,dblock,text);
                sb.append(");");
            }
        }

        if (type == 1) {//elem
            String tagName = n.getNodeName();

            //if控制指令
            if ("if".equals(tagName)) {
                parseIfNode(sb, sqlBuilderName, dblock, n, depth);
                return;
            }


            //for控制指令
            if ("for".equals(tagName)) {
                parseForNode(sb, sqlBuilderName, dblock, n, depth);
                return;
            }

            if ("ref".equals(tagName)) {
                parseRefNode(sb, sqlBuilderName, dblock, n, depth);
                return;
            }

            if ("trim".equals(tagName)) {
                parseTrimNode(sb, sqlBuilderName, dblock, n, depth);
                return;
            }

            _parseNodeList(n.getChildNodes(), sqlBuilderName, sb, dblock, depth);
        }
    }

    //xml:解析 trim 指令节点
    private static void parseTrimNode(StringBuilder sb, String sqlBuilderName , XmlSqlBlock dblock, Node n , int depth) {
        String _trimStart = attr(n, "trimStart");//开始去除
        String _trimEnd = attr(n, "trimEnd");//结属去除
        String _prefix = attr(n, "prefix");//添前缀
        String _suffix = attr(n, "suffix");//添后缀

        dblock.varNum++;

        String varName = "sb"+dblock.varNum;

        sb.append("\n");
        newLine(sb, depth).append("SQLBuilder ").append(varName).append(" = new SQLBuilder();  /*trim node*/");

        _parseNodeList(n.getChildNodes(), varName, sb, dblock, depth);

        if(StringUtils.isEmpty(_trimStart) == false){
            newLine(sb, depth).append(varName).append(".trimStart(\"").append(_trimStart.trim()).append("\");");
        }

        if(StringUtils.isEmpty(_trimEnd) == false){
            newLine(sb, depth).append(varName).append(".trimEnd(\"").append(_trimEnd.trim()).append("\");");
        }

        if(StringUtils.isEmpty(_prefix) == false){
            newLine(sb, depth).append(varName).append(".addPrefix(\"").append(_prefix.trim()).append("\");");
        }

        if(StringUtils.isEmpty(_suffix) == false){
            newLine(sb, depth).append(varName).append(".addSuffix(\"").append(_suffix.trim()).append("\");");
        }

        newLine(sb, depth).append(sqlBuilderName).append(".append(").append(varName).append(");\n");
    }

    //xml:解析 if 指令节点
    private static void parseIfNode(StringBuilder sb,String sqlBuilderName, XmlSqlBlock dblock, Node n , int depth) {
        String _test = attr(n, "test")
                        .replace(" lt "," < ")
                        .replace(" lte "," <= ")
                        .replace(" gt "," > ")
                        .replace(" gte "," >= ")
                        .replace(" and ", " && ")
                        .replace(" or ", " || ");

        if(_test.indexOf("?")>0){
            _test = parseIfTestExpr(_test);
        }

        newLine(sb, depth).append("if(").append(_test).append("){ /*if node*/");

        _parseNodeList(n.getChildNodes(),sqlBuilderName, sb, dblock, depth + 1);

        newLine(sb, depth).append("}");
    }

    private static String parseIfTestExpr(String test) {
        Pattern r = Pattern.compile("([\\w\\.]*?)\\?(\\?|\\!|\\w*)");
        Matcher m = r.matcher(test);
        if (m.find()) {
            String vname = m.group(1);
            String vfun = "?" + m.group(2);
            if ("??".equals(vfun)) {
                String newStr = vname+" != null";

                test = test.replace(m.group(), newStr);
            }

            if ("?!".equals(vfun)) {
                String newStr = "StringUtils.isEmpty(" + vname + ") == false";

                test = test.replace(m.group(), newStr);
            }
        }

        return test;
    }

    //xml:解析 ref 指令节点
    private static void parseRefNode(StringBuilder sb, String sqlBuilderName,XmlSqlBlock dblock, Node n , int depth) {
        String _sql_id = attr(n, "sql");
        if (StringUtils.isEmpty(_sql_id) == false) {
            Node ref_n = dblock.__nodeMap.get(_sql_id);
            if (ref_n == null) {
                throw new RuntimeException("sql node @" + _sql_id + " can't find");
            }
            _parseNode(ref_n,sqlBuilderName, sb, dblock, depth);
        }
    }

    //xml:解析 for 指令节点
    private static void parseForNode(StringBuilder sb,String sqlBuilderName, XmlSqlBlock dblock, Node n , int depth) {
        /**
         * //编译结果示例
         * {
         *     int m_index = 0; //新生的 m_index 变量
         *     Iterator<weed3demo.mapper.UserModel> m_iterator = list.iterator(); //@items配置
         *     while (m_iterator.hasNext()) {
         *         weed3demo.mapper.UserModel m = m_iterator.next(); //@var配置
         *
         *         sb.append("(?,?,?) ", m.user_id, m.mobile, m.sex);
         *
         *         if (m_iterator.hasNext()) { //当 @sep 不为空
         *             sb.append(",");
         *         }
         *         m_index++;
         *     }
         * }
         * */
        String _items = attr(n, "items");
        String _sep_str = attr(n, "sep");
        String _var_str = attr(n, "var").trim();

        if(_var_str.indexOf(":")<0 || _var_str.length() < 3){
            StringBuilder eb = new StringBuilder();
            eb.append(dblock._namespace).append("/").append(dblock._id).append("::")
                    .append("for/var(").append(_var_str).append(") must declare the type");
            throw new RuntimeException(eb.toString());
        }

        String[] kv = _var_str.split(":");

        XmlSqlVar _var = new XmlSqlVar(_var_str,kv[0].trim(),kv[1].trim());


        //索引变量
        int depth0 = depth;
        depth++;

        //***start for
        newLine(sb, depth0).append("{ /*for node*/");
        //::定义@index变量
        newLine(sb, depth).append("int ").append(_var.name).append("_index = 0;");
        //::定义@iterator变量
        newLine(sb, depth).append("Iterator<").append(_var.type).append("> ").append(_var.name).append("_iterator").append(" = ").append(_items).append(".iterator();");

        //使用withle(iterator.hasNext()) 进行循环
        newLine(sb, depth).append("while (").append(_var.name).append("_iterator.hasNext()){");
        //::#var变量
        newLine(sb, depth+1).append(_var.type).append(" ").append(_var.name).append(" = ").append(_var.name).append("_iterator.next();\n");

        //循环内部代码体
        _parseNodeList(n.getChildNodes(),sqlBuilderName, sb, dblock, depth + 1);

        sb.append("\n");
        if(StringUtils.isEmpty(_sep_str) == false) {
            newLine(sb, depth + 1).append("if(").append(_var.name).append("_iterator.hasNext()").append("){");
            newLine(sb, depth + 2).append(sqlBuilderName).append(".append(\"").append(_sep_str).append("\");");
            newLine(sb, depth + 1).append("}");
        }

        //索引变量++
        newLine(sb, depth+1).append(_var.name).append("_index++;");

        newLine(sb, depth).append("}");

        //***end for
        newLine(sb, depth0).append("}");


        if(_items.indexOf(".")<0) { // m.users , 这种，不需要申明变量
            XmlSqlVar _itemsVar = new XmlSqlVar(_items, _items, "Collection<" + _var.type + ">");
            dblock.varPut(_itemsVar);
        }
    }

    //sql::格式化字符串
    private static void parseTxt(StringBuilder sb, XmlSqlBlock dblock, String txt0){
        //临时变量列表
        List<XmlSqlVar> tmpList = new ArrayList<>();

        String txt2 = txt0.replace("\n"," ").replace("\"", "\\\"");
        dblock._texts.append(txt2);

        //1.处理${xxx},${xxx:type}
        {
            tmpList.clear();

            Matcher m = XmlSqlVar.varRepExp.matcher(txt2);

            while (m.find()) {
                XmlSqlVar dv = parseTxtVar(m);

                tmpList.add(dv);
                dblock.varPut(dv);
            }

            for (XmlSqlVar dv : tmpList) {
                //如果没有type 申明，采用 map.get()
                if(StringUtils.isEmpty(dv.type)){
                    txt2 = txt2.replace(dv.mark, "\"+ map.get(\"" + dv.name + "\") +\"");
                }else{
                    txt2 = txt2.replace(dv.mark, "\"+ " + dv.name + " +\"");
                }

            }
        }

        //2.找出@{xxx},@{xxx:type}
        {
            tmpList.clear();

            Matcher m = XmlSqlVar.varComExp.matcher(txt2);
            while (m.find()) {
                XmlSqlVar dv = parseTxtVar(m);

                tmpList.add(dv);
                dblock.varPut(dv);
            }

            for (XmlSqlVar dv : tmpList) {
                if(dv.type != null && dv.type.indexOf(">")>0){
                    txt2 = txt2.replace(dv.mark, "?...");
                }else{
                    txt2 = txt2.replace(dv.mark, "?");
                }
            }

            sb.append("\"").append(txt2).append(" \"");
            tmpList.forEach(v -> {
                //如果没有type 申明，采用 map.get()
                if(StringUtils.isEmpty(v.type)){
                    sb.append(",map.get(\"").append(v.name).append("\")");
                }else{
                    sb.append(",").append(v.name);
                }
            });
        }
    }

    //解析文本本里的变量
    private static XmlSqlVar parseTxtVar(Matcher m){
        XmlSqlVar dv = new XmlSqlVar();
        dv.mark = m.group(0);
        dv.name = m.group(1).trim().replace("[","<").replace("]",">");
        if (dv.name.indexOf(":") > 0) {
            String[] kv = dv.name.split(":");
            dv.name = kv[0].trim();
            dv.type = kv[1].trim();
        }

        return dv;
    }


    //sb:新起一行代码
    private static StringBuilder newLine(StringBuilder sb, int depth){
        sb.append("\n");
        while (depth>0){
            sb.append("  ");
            depth--;
        }

        return sb;
    }

    //xml:读取属性
    private static String attr(Node n, String name) {
        if (name.startsWith(":")) {
            return attr(n, name, name.substring(1));
        } else {
            return attr(n, name, null);
        }
    }

    private static String attr(Node n, String name, String name2){
        Node tmp = n.getAttributes().getNamedItem(name);

        if(tmp == null && name2 != null){
            tmp = n.getAttributes().getNamedItem(name2);
        }

        if(tmp == null){
            return null;
        }else{
            return tmp.getNodeValue();
        }
    }
}
