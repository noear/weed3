package org.noear.weed.generator.entity;

import org.noear.weed.DbContext;
import org.noear.weed.generator.utils.StringUtils;
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

    private static void generateJavaFile(Node n1, File sourceDir) throws Exception {
        if (XmlNames.tag_source.equals(n1.getNodeName()) == false) {
            return;
        }

        Element e1 = (Element) n1;

        XmlSourceBlock source = new XmlSourceBlock();

        source.schema = XmlUtils.attr(n1, XmlNames.att_schema);
        source.url = XmlUtils.attr(n1, XmlNames.att_url);
        source.username = XmlUtils.attr(n1, XmlNames.att_username);
        source.password = XmlUtils.attr(n1, XmlNames.att_password);
        source.driverClassName = XmlUtils.attr(n1, XmlNames.att_driverClassName);

        NodeList n2l = n1.getChildNodes();
        for (int i = 0, len = n2l.getLength(); i < len; i++) {
            Node n2 = n2l.item(i);

            if (n2.getNodeType() != 1) {
                continue;
            }

            if (XmlNames.tag_entity.equals(n2.getNodeName())) {
                source.entity_targetPackage = XmlUtils.attr(n2, XmlNames.att_targetPackage);
                source.entity_entityName = XmlUtils.attr(n2, XmlNames.att_entityName);
                source.entity_fieldStyle = XmlUtils.attr(n2, XmlNames.att_fieldStyle);
            }

            if (XmlNames.tag_dao.equals(n2.getNodeName())) {
                source.dao_targetPackage = XmlUtils.attr(n2, XmlNames.att_targetPackage);
                source.dao_entityName = XmlUtils.attr(n2, XmlNames.att_entityName);
                source.dao_db = XmlUtils.attr(n2, XmlNames.att_db);
            }

            if (XmlNames.tag_service.equals(n2.getNodeName())) {
                source.service_targetPackage = XmlUtils.attr(n2, XmlNames.att_targetPackage);
                source.service_entityName = XmlUtils.attr(n2, XmlNames.att_entityName);
            }
        }

        if (StringUtils.isEmpty(source.entity_entityName)) {
            source.entity_entityName = "${table}";
        }

        if (StringUtils.isEmpty(source.dao_entityName)) {
            source.dao_entityName = source.entity_entityName;
        }

        if (StringUtils.isEmpty(source.service_entityName)) {
            source.service_entityName = source.entity_entityName;
        }


        if (StringUtils.isEmpty(source.driverClassName) == false) {
            Class.forName(source.driverClassName);
        }


        DbContext db = new DbContext(source.schema, source.url, source.username, source.password);

        tryGenerateEntityFiles(db, source, sourceDir);
        tryGenerateDaoFiles(db, source, sourceDir);
    }

    private static void tryGenerateEntityFiles(DbContext db, XmlSourceBlock source, File sourceDir) {
        if (StringUtils.isEmpty(source.entity_targetPackage)) {
            return;
        }

        String packDir = (sourceDir.getAbsolutePath() + "/" + source.entity_targetPackage.replace(".", "/") + "/");

        try {
            EntityGenerator.createByDb(packDir,
                    source.entity_targetPackage, db,
                    source.entity_entityName,
                    XmlNames.val_camel.equals(source.entity_fieldStyle));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static void tryGenerateDaoFiles(DbContext db, XmlSourceBlock source, File sourceDir) {
        if (StringUtils.isEmpty(source.dao_targetPackage)) {
            return;
        }

        String packDir = (sourceDir.getAbsolutePath() + "/" + source.dao_targetPackage.replace(".", "/") + "/");

        try {
            MapperGenerator.createByDb(source.entity_targetPackage, packDir,
                    source.dao_targetPackage, db,
                    source.dao_entityName, source.dao_db);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}

