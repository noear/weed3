package weed3demo.mapper;

import org.junit.Test;
import org.noear.weed.xml.XmlSqlLoader;
import org.noear.weed.xml.XmlSqlMapper;


import java.net.URL;

public class XmlSqlRunnerTest {

    @Test
    public  void test() throws Exception {

        //new ICacheServiceEx().nameSet("sdf");
        //new DbContext().nameSet("testdb");

        XmlSqlLoader.load();

//        Mapper api = XmlSqlMapper.get(Mapper.class);
//        api.user_get("mobile,sex",10,"18658857337");
//        api.user_add_for(12,"");
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
