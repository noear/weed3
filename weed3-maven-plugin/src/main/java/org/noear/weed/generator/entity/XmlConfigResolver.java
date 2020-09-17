package org.noear.weed.generator.entity;

import org.noear.weed.generator.utils.XmlUtils;
import org.w3c.dom.Document;
import java.io.File;

public class XmlConfigResolver {
    public static void exec(File baseDir, File sourceDir) {
        try {
            String path = (baseDir.getAbsolutePath() + "/src/main/resources/weed3-generator.xml");
            File file = new File(path);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static void parse(File file) throws Exception {
        Document doc = XmlUtils.parseDoc(file);
    }
}
