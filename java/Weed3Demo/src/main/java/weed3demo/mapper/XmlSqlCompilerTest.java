package weed3demo.mapper;

import org.junit.Test;
import org.noear.weed.xml.*;

import java.io.File;
import java.net.URL;

public class XmlSqlCompilerTest {
    @Test
    public  void load() throws Exception {
        URL url = getResource("/weed3/mapper.xml");

        String code = XmlSqlCompiler.parse( new File(url.toURI()));

        System.out.println(code);

//        XmlSqlLoader.load();
//
//        IXmlSqlBuilder tmp = XmlSqlFactory.get("org.xxx.xxx.user_add");
//        if(tmp == null){
//            return;
//        }else{
//            System.out.println("已找到 IXmlSqlBuilder: org.xxx.xxx.user_add");
//        }
    }

    public static URL getResource(String name) {
        URL url = XmlSqlLoader.class.getResource(name);
        if (url == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                url = loader.getResource(name);
            } else {
                url = ClassLoader.getSystemResource(name);
            }
        }

        return url;
    }


}
