package org.noear.weed.xml;

import org.noear.weed.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlSqlCompiler {

    //将xml解析为java code
    public static String parse(File xmlFile) throws Exception{
        if(xmlFile == null){
            return null;
        }


        Document doc = parseDoc(xmlFile);

        Node nm = doc.getDocumentElement();

        String namespace = attr(nm, "namespace");
        String db = attr(nm, ":db");
        String classname = xmlFile.getName().replace(".","_"); //namespace.replace(".","_"); //"weed_xml_sql";

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(namespace).append(";\n\n");

        sb.append("import java.math.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("import org.noear.weed.SQLBuilder;\n");
        sb.append("import org.noear.weed.xml.XmlSqlFactory;\n\n");

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
            parseSqlNode(node_map, sb, n, namespace, db, classname);
        }

        sb.append("}\n");

        node_map.clear();

        return sb.toString();
    }

    //xml:解析 sql 指令节点
    private static void parseSqlNode(Map<String,Node> nodeMap, StringBuilder sb,Node n, String namespace, String db, String classname) {
        int depth = 1;
        XmlSqlBlock dblock = new XmlSqlBlock();

        dblock.__nodeMap = nodeMap;

        dblock._namespace = namespace;
        dblock._classname = classname;
        dblock._classcode = sb;

        dblock._id = attr(n, "id");

        dblock._declare = attr(n, ":declare");
        dblock._return = attr(n, ":return");
        if (dblock._return != null && dblock._return.indexOf("[") > 0) {
            dblock._return = dblock._return.replace("[", "<")
                                           .replace("]", ">");
        }

        String db_tmp = attr(n, ":db");
        if (StringUtils.isEmpty(db_tmp)) {
            dblock._db = db;
        } else {
            dblock._db = db_tmp;
        }


        dblock._caching = attr(n, ":caching");
        dblock._usingCache = attr(n, ":usingCache");
        dblock._cacheTag = attr(n, ":cacheTag");
        dblock._cacheClear = attr(n, ":cacheClear");

        //构建申明的变量
        _parseDeclare(dblock);

        newLine(sb, depth).append("public SQLBuilder ").append(dblock._id).append("(Map map){");

        //构建代码体和变量
        StringBuilder sb2 = new StringBuilder();
        {
            newLine(sb2, depth + 1).append("SQLBuilder sb = new SQLBuilder();\n");
            _parseNodeList(n.getChildNodes(), sb2, dblock, depth + 1);
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

        sb.append("\n");
        newLine(sb, depth + 1).append("return sb;");
        newLine(sb, depth).append("}\n");

        dblock.__nodeMap = null;

        //0.确定动作
        {
            String txt2 = dblock._texts.insert(0,"# ").toString().trim().toUpperCase();

            if(dblock._action==null && txt2.indexOf(" INSERT ")>0){
                dblock._action = "INSERT";
            }

            if(dblock._action==null && txt2.indexOf(" DELETE ")>0){
                dblock._action = "DELETE";
            }

            if(dblock._action==null && txt2.indexOf(" UPDATE ")>0){
                dblock._action = "UPDATE";
            }

            if(dblock._action==null && txt2.indexOf(" SELECT ")>0){
                dblock._action = "SELECT";
            }

            dblock._texts = null;
        }

        //注册块
        XmlSqlFactory.register(namespace + "." + dblock._id, dblock);
    }

    private static void _parseDeclare(XmlSqlBlock dblock) {
        if (dblock._declare == null) {
            return;
        }
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

    private static void _parseNodeList(NodeList nl, StringBuilder sb, XmlSqlBlock dblock, int depth) {
        for (int i = 0, len = nl.getLength(); i < len; i++) {
            Node n = nl.item(i);

            _parseNode(n,sb,dblock,depth);
        }
    }

    private static void _parseNode(Node n, StringBuilder sb, XmlSqlBlock dblock,  int depth){
        int type = n.getNodeType();

        if (type == 3 || type == 4) {//text or CDATA
            String text = n.getTextContent().trim();

            if (text.length() > 0) {
                newLine(sb, depth).append("sb.append(");
                parseTxt(sb,dblock,text);
                sb.append(");");
            }
        }

        if (type == 1) {//elem
            String tagName = n.getNodeName();

            if ("if".equals(tagName)) {
                parseIfNode(sb,dblock,n,depth);
                return;
            }


            if("for".equals(tagName)){
                parseForNode(sb,dblock,n,depth);
                return;
            }

            if("ref".equals(tagName)){
                parseRefNode(sb,dblock,n,depth);
                return;
            }

            _parseNodeList(n.getChildNodes(),sb,dblock,depth);
        }
    }

    //xml:解析 if 指令节点
    private static void parseIfNode(StringBuilder sb, XmlSqlBlock dblock, Node n , int depth) {
        String _test = attr(n, "test");

        newLine(sb, depth).append("if(").append(_test).append("){");

        _parseNodeList(n.getChildNodes(), sb, dblock, depth + 1);

        newLine(sb, depth).append("}");
    }

    //xml:解析 ref 指令节点
    private static void parseRefNode(StringBuilder sb, XmlSqlBlock dblock, Node n , int depth) {
        String _sql_id = attr(n, "sql");
        if (StringUtils.isEmpty(_sql_id) == false) {
            Node ref_n = dblock.__nodeMap.get(_sql_id);
            if (ref_n == null) {
                throw new RuntimeException("sql node @" + _sql_id + " can't find");
            }
            _parseNode(ref_n, sb, dblock, depth);
        }
    }

    //xml:解析 for 指令节点
    private static void parseForNode(StringBuilder sb, XmlSqlBlock dblock, Node n , int depth) {
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
        newLine(sb, depth0).append("{");
        //::定义@index变量
        newLine(sb, depth).append("int ").append(_var.name).append("_index = 0;");
        //::定义@iterator变量
        newLine(sb, depth).append("Iterator<").append(_var.type).append("> ").append(_var.name).append("_iterator").append(" = ").append(_items).append(".iterator();");

        //使用withle(iterator.hasNext()) 进行循环
        newLine(sb, depth).append("while (").append(_var.name).append("_iterator.hasNext()){");
        //::#var变量
        newLine(sb, depth+1).append(_var.type).append(" ").append(_var.name).append(" = ").append(_var.name).append("_iterator.next();\n");

        //循环内部代码体
        _parseNodeList(n.getChildNodes(), sb, dblock, depth + 1);

        sb.append("\n");
        if(StringUtils.isEmpty(_sep_str) == false) {
            newLine(sb, depth + 1).append("if(").append(_var.name).append("_iterator.hasNext()").append("){");
            newLine(sb, depth + 2).append("sb.append(\"").append(_sep_str).append("\");");
            newLine(sb, depth + 1).append("}");
        }

        //索引变量++
        newLine(sb, depth+1).append(_var.name).append("_index++;");

        newLine(sb, depth).append("}");

        //***end for
        newLine(sb, depth0).append("}");


        XmlSqlVar _itemsVar = new XmlSqlVar(_items, _items, "Collection<" + _var.type + ">");
        dblock.varPut(_itemsVar);
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
    private static String attr(Node n, String name){
        Node tmp = n.getAttributes().getNamedItem(name);
        if(tmp == null){
            return null;
        }else{
            return tmp.getNodeValue();
        }
    }

    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;
    //xml:解析文档
    private static Document parseDoc(File xmlFile) throws Exception{
        if(dbf ==null) {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        }

        return db.parse(xmlFile);
    }

    //sql::格式化字符串
    private static void parseTxt(StringBuilder sb, XmlSqlBlock dblock, String txt0){
        Map<String, XmlSqlVar> tmpList = new LinkedHashMap<>();

        String txt2 = txt0.replace("\n"," ").replace("\"", "\\\"");
        dblock._texts.append(txt2);

        //1.处理${xxx},${xxx,type}
        {
            tmpList.clear();

            Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
            Matcher m = pattern.matcher(txt2);

            while (m.find()) {
                XmlSqlVar dv = new XmlSqlVar();
                dv.mark = m.group(0);
                dv.name = m.group(1).trim().replace("[","<").replace("]",">");
                if (dv.name.indexOf(":") > 0) {
                    String[] kv = dv.name.split(":");
                    dv.name = kv[0].trim();
                    dv.type = kv[1].trim();
                }

                tmpList.put(dv.name, dv);
                dblock.varPut(dv);
            }

            for (XmlSqlVar dv : tmpList.values()) {
                txt2 = txt2.replace(dv.mark, "\"+" + dv.name + "+\"");
            }
        }

        //2.找出@{xxx},@{xxx:type}
        {
            tmpList.clear();

            Pattern pattern = Pattern.compile("@\\{(.+?)\\}");
            Matcher m = pattern.matcher(txt2);
            while (m.find()) {
                XmlSqlVar dv = new XmlSqlVar();
                dv.mark = m.group(0);
                dv.name = m.group(1).trim().replace("[","<").replace("]",">");
                if (dv.name.indexOf(":") > 0) {
                    String[] kv = dv.name.split(":");
                    dv.name = kv[0].trim();
                    dv.type = kv[1].trim();
                }

                tmpList.put(dv.name, dv);
                dblock.varPut(dv);
            }

            for (XmlSqlVar dv : tmpList.values()) {
                if(dv.type != null && dv.type.indexOf(">")>0){
                    txt2 = txt2.replace(dv.mark, "?...");
                }else{
                    txt2 = txt2.replace(dv.mark, "?");
                }
            }

            sb.append("\"").append(txt2).append(" \"");
            tmpList.forEach((k, v) -> {
                sb.append(",").append(v.name);
            });
        }
    }
}
