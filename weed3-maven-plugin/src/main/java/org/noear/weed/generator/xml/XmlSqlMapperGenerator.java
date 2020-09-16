package org.noear.weed.generator.xml;

import org.noear.weed.generator.utils.IOUtils;
import org.noear.weed.generator.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class XmlSqlMapperGenerator {

    /** 生成 java 类 */
    public static void generate(File baseDir, File sourceDir) {
        try {
            String path = (baseDir.getAbsolutePath() + "/src/main/resources/weed3");
            File dic = new File(path);

            do_generate(dic,sourceDir);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static void do_generate(File file, File sourceDir) throws Throwable {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                do_generate(tmp, sourceDir);
            }
        } else {
            if (file.getName().endsWith(".xml")) {
                generateJavaFile(sourceDir, file);
            }
        }
    }


    public static void generateJavaFile(File sourceDir, File xmlFile) throws Exception {
        JavaCodeBlock block = parse(xmlFile);
        if (block == null) {
            return;
        }

        String dic_path = sourceDir.getAbsolutePath()+"/" + block._packageName.replace(".", "/");

        new File(dic_path).mkdirs();

        String file_path = dic_path + "/" + block._className + ".java";
        File file = new File(file_path);
        if (!file.exists()) {
            file.createNewFile();
        }

        IOUtils.fileWrite(file, block._code);
        System.out.println("Generated : "+file.getAbsolutePath());
    }

    //将xml解析为java code
    public static JavaCodeBlock parse(File xmlFile) throws Exception{
        if(xmlFile == null){
            return null;
        }

        Document doc = parseDoc(xmlFile);

        Node nm = doc.getDocumentElement();

        Set<String> importSet = new HashSet<>();

        String namespace = attr(nm, "namespace");
        String _import = attr(nm, "import");
        int sepindex = namespace.lastIndexOf('.');

        String baseMapperOf = attr(nm, ":baseMapper");
        String dbOf = attr(nm,":db");

        String packagename = namespace.substring(0,sepindex);
        String classname = namespace.substring(sepindex+1);
        //String classname = xmlFile.getName().split("\\.")[0]; //namespace.replace(".","_"); //"weed_xml_sql";

        StringBuilder sb = new StringBuilder();


        sb.append("package ").append(packagename).append(";\n\n");

        sb.append("import java.math.*;\n");
        sb.append("import java.sql.SQLException;\n");
        sb.append("import java.time.*;\n");
        sb.append("import java.util.*;\n\n");

        sb.append("import org.noear.weed.BaseMapper;\n");
        sb.append("import org.noear.weed.DataItem;\n");
        sb.append("import org.noear.weed.DataList;\n");
        sb.append("import org.noear.weed.annotation.Db;\n");
        sb.append("import org.noear.weed.xml.Namespace;\n");
        if(StringUtils.isEmpty(_import) == false) {
            String[] ss = _import.split(";");
            for (String s : ss) {
                if (s.length() > 2) {
                    sb.append("import ").append(s).append(";\n");
                }
            }
        }
        sb.append("\n");

        StringBuilder sb2 = new StringBuilder();


        Map<String,Node> node_map = new HashMap<String,Node>();
        NodeList sql_list = doc.getElementsByTagName("sql");
        for (int i = 0, len = sql_list.getLength(); i < len; i++) {
            Node n = sql_list.item(i);
            String id = attr(n,"id");
            if(id!=null){
                node_map.put(id,n);
            }
        }

        if(StringUtils.isEmpty(dbOf) == false){
            sb2.append("@Db(\"").append(dbOf).append("\")\n");
        }
        sb2.append("@Namespace(\"").append(namespace).append("\")\n");
        sb2.append("public interface ").append(classname);
        if(StringUtils.isEmpty(baseMapperOf) == false) {
            sb2.append(" extends BaseMapper<")
                    .append(baseMapperOf)
                    .append(">");
        }
        sb2.append("{");

        //构建block
        StringBuilder sb_tmp  =new StringBuilder();
        for (int i = 0, len = sql_list.getLength(); i < len; i++) {
            Node n = sql_list.item(i);
            XmlSqlBlock block = parseSqlNode(node_map, sb_tmp, n, namespace, classname);

            //打印接口中块***
            writerBlock(sb2, block);
            if(i<len-1) {
                sb2.append("\n");
            }

            importSet.addAll(block.impTypeSet);
        }

        sb2.append("\n}\n");

        for(String type : importSet.stream().sorted().collect(Collectors.toList())){
            sb.append("import ").append(type).append(";\n");
        }
        if(importSet.size()>0){
            newLine(sb,0);
        }

        sb.append(sb2);


        node_map.clear();

        JavaCodeBlock codeBlock = new JavaCodeBlock();

        codeBlock._namespace = namespace;
        codeBlock._packageName = packagename;
        codeBlock._className =classname;
        codeBlock._code = sb.toString();

        return codeBlock;
    }

    private static void writerBlock(StringBuilder sb, XmlSqlBlock block){
        //写入注释
        if(block._remarks != null && block._remarks.length()>0){
            newLine(sb,2).append("//").append(block._remarks);
        }

        //写入接口定义
        newLine(sb,2);
        if(StringUtils.isEmpty(block._return)){
            sb.append("void");
        }
        else{
            if("Map".equals(block._return)){
                sb.append("Map<String,Object>");
            } else if("MapList".equals(block._return)){
                sb.append("List<Map<String,Object>>");
            } else{
                sb.append(block._return);
            }
        }

        sb.append(" ").append(block._id).append("(");

        for(String k : block.varMap.keySet()){
            XmlSqlVar v = block.varMap.get(k);

            sb.append(v.type).append(" ").append(v.name).append(", ");
        }

        if(block.varMap.size()>0){
            //删掉最后的：(, )
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
        }

        sb.append(") throws SQLException;");
    }

    //xml:解析 sql 指令节点
    private static XmlSqlBlock parseSqlNode(Map<String,Node> nodeMap, StringBuilder sb,Node n, String namespace, String classname) {
        int depth = 1;
        XmlSqlBlock dblock = new XmlSqlBlock();

        dblock.__nodeMap = nodeMap;

        dblock._namespace = namespace;
        dblock._classname = classname;
        dblock._classcode = sb;

        dblock._id = attr(n, "id");

        dblock._remarks = attr(n, ":remarks");
        if(dblock._remarks == null) {
            dblock._remarks = attr(n, ":note");
        }
        dblock._param = attr(n, ":param");
        dblock._declare = attr(n, ":declare");
        dblock._return = attr(n, ":return");
        if (dblock._return != null) {
            TypeBlock tBlock = new TypeBlock(dblock._return);

            dblock._return = tBlock.newType;
            if(tBlock.impType!=null){
                dblock.impTypeSet.add(tBlock.impType);
            }
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
                        .append(dv.type).append(" ").append(dv.name).append("=")
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
        //注册块
        return dblock;
    }

    private static void _parseDeclare(XmlSqlBlock dblock) {
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
        String _var_str = attr(n, "var").trim();

        if (_var_str.indexOf(":") < 0 || _var_str.length() < 3) {
            StringBuilder eb = new StringBuilder();
            eb.append(dblock._namespace).append("/").append(dblock._id).append("::")
                    .append("for/var(").append(_var_str).append(") must declare the type");
            throw new RuntimeException(eb.toString());
        }

        String[] kv = _var_str.split(":");

        XmlSqlVar _var = new XmlSqlVar(_var_str, kv[0].trim(), kv[1].trim());
        String _items = attr(n, "items");

        //newLine(sb, depth).append("Iterable<").append(_var.type).append("> ").append(_items).append("=(Iterable<").append(_var.type).append(">)map.get(\"").append(_items).append("\");");
        newLine(sb, depth).append("for(").append(_var.type).append(" ").append(_var.name).append(" : ").append(_items).append("){");

        _parseNodeList(n.getChildNodes(), sb, dblock, depth + 1);

        //注到
        if (_items.indexOf(".") < 0) {
            XmlSqlVar _itemsVar = new XmlSqlVar(_items, _items, "Collection<" + _var.type + ">");
            dblock.varPut(_itemsVar);
        }

        newLine(sb, depth).append("}");
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

    private static DocumentBuilderFactory docBf = null;
    private static DocumentBuilder docB = null;
    //xml:解析文档
    private static Document parseDoc(File xmlFile) throws Exception{
        if(docBf ==null) {
            docBf = DocumentBuilderFactory.newInstance();
            docBf.setValidating(false);
            docB = docBf.newDocumentBuilder();

            docB.setEntityResolver(( publicId,  systemId)->{
                if (systemId.contains("weed3-mapper.dtd")) {
//                    InputStream dtdStream = XmlSqlBlock.class.getResourceAsStream("/org/noear/weed/xml/weed3-mapper.dtd");
//                    return new InputSource(dtdStream);
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            });
        }

        return docB.parse(xmlFile);
    }

    //sql::格式化字符串
    private static void parseTxt(StringBuilder sb, XmlSqlBlock dblock, String txt0){
        String txt2 = null;
        Map<String, XmlSqlVar> tmpList = new LinkedHashMap<String, XmlSqlVar>();

        txt2 = txt0.replace("\n"," ").replace("\"","\\\"");
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
            for(String k : tmpList.keySet()){
                XmlSqlVar v = tmpList.get(k);
                sb.append(",").append(v.name);
            }
        }
    }
}
