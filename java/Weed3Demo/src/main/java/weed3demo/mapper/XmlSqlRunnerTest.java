package weed3demo.mapper;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.xml.XmlSqlLoader;
import org.noear.weed.xml.XmlSqlProxy;

import java.net.URL;

public class XmlSqlRunnerTest {
    @Test
    public  void test() throws Exception {

        WeedConfig.libOfDb.put("testdb",new DbContext());

        XmlSqlLoader.load();

        Mapper api = XmlSqlProxy.get(Mapper.class);
        //api.user_get("mobile,sex",10,"18658857337");
        api.user_add_for(12,"");
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
