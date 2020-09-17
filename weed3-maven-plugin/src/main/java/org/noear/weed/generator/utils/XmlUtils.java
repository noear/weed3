package org.noear.weed.generator.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;

public class XmlUtils {
    private static DocumentBuilderFactory docBf = null;
    private static DocumentBuilder docB = null;

    //xml:解析文档
    public static Document parseDoc(File xmlFile) throws Exception {
        if (docBf == null) {
            docBf = DocumentBuilderFactory.newInstance();
            docBf.setValidating(false);
            docB = docBf.newDocumentBuilder();

            docB.setEntityResolver((publicId, systemId) -> {
                return new InputSource(new StringReader(""));
            });
        }

        return docB.parse(xmlFile);
    }

    //xml:读取属性
    public static String attr(Node n, String name) {
        if (name.startsWith(":")) {
            return attr(n, name, name.substring(1));
        } else {
            return attr(n, name, null);
        }
    }

    public static String attr(Node n, String name, String name2){
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
