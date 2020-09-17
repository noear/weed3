package org.noear.weed.generator.utils;

import org.w3c.dom.Document;
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
}
