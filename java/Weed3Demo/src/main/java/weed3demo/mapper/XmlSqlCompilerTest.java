package weed3demo.mapper;

import org.junit.Test;
import org.noear.weed.xml.*;

public class XmlSqlCompilerTest {
    @Test
    public  void load() throws Exception {

        XmlSqlLoader.load();

        IXmlSqlBuilder tmp = XmlSqlFactory.get("org.xxx.xxx.user_add");
        if(tmp == null){
            return;
        }else{
            System.out.println("已找到 IXmlSqlBuilder: org.xxx.xxx.user_add");
        }
    }


}
