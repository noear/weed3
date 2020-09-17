package org.noear.weed.generator.entity;

import org.noear.weed.DbContext;
import org.noear.weed.generator.utils.StringUtils;
import org.noear.weed.generator.utils.XmlNames;
import org.noear.weed.generator.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class XmlEntityGenerator {
    public static void generate(File baseDir, File sourceDir) {
        try {
            String path = (baseDir.getAbsolutePath() + "/src/main/resources/weed3-generator.xml");
            File file = new File(path);

            if (file.exists() == false) {
                System.err.println("No configuration file: weed3-generator.xml");
            }

            generate0(file, sourceDir);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static void generate0(File file, File sourceDir) throws Exception {
        Document doc = XmlUtils.parseDoc(file);

        Node nm = doc.getDocumentElement();

        NodeList nl = nm.getChildNodes();
        for (int i = 0, len = nl.getLength(); i < len; i++) {
            Node n1 = nl.item(i);

            if (n1.getNodeType() == 1) {
                generateJavaFile(n1, sourceDir);
            }
        }
    }

    private static void generateJavaFile(Node n1, File sourceDir) {
        if (XmlNames.tag_source.equals(n1.getNodeName()) == false) {
            return;
        }

        Element e1 = (Element) n1;

        SourceBlock source = new SourceBlock();

        source.schema = XmlUtils.attr(n1, "schema");
        source.url = XmlUtils.attr(n1, "url");
        source.username = XmlUtils.attr(n1, "username");
        source.password = XmlUtils.attr(n1, "password");
        source.driverClassName = XmlUtils.attr(n1, "driverClassName");

        NodeList n2l = n1.getChildNodes();
        for (int i = 0, len = n2l.getLength(); i < len; i++) {
            Node n2 = n2l.item(i);

            if (n2.getNodeType() != 1) {
                continue;
            }

            if (XmlNames.tag_table.equals(n2.getNodeName())) {
                source.table_basePackage = XmlUtils.attr(n2, "basePackage");
            }
        }

        if(StringUtils.isEmpty(source.driverClassName) == false){
            try{
                Class.forName(source.driverClassName);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        String packDir = (sourceDir.getAbsolutePath() + "/" + source.table_basePackage.replace(".", "/") + "/");

        DbContext db = new DbContext(source.schema, source.url, source.username, source.password);


        try {
            EntityGenerator.createByDb(packDir, source.table_basePackage, db);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}

