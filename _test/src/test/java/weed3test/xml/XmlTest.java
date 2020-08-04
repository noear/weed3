package weed3test.xml;

import org.junit.Test;
import org.noear.solon.XUtil;
import org.noear.weed.xml.XmlSqlCompiler;

import java.net.URL;

public class XmlTest {
    @Test
    public void test1() throws Exception {

        URL url = XUtil.getResource("bak/test2.xml");

        String code = XmlSqlCompiler.parse(url);

        System.out.println(code);
    }
}
