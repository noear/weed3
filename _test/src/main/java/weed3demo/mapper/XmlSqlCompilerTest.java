package weed3demo.mapper;

import org.junit.Test;
import org.noear.weed.SQLBuilder;
import org.noear.weed.xml.*;

import java.net.URL;

public class XmlSqlCompilerTest {
    @Test
    public void test0() {
        SQLBuilder sb = new SQLBuilder();
        String tmp = sb.addPrefix("and b=1 and c=2, ").trimStart("and").trimEnd(",").addPrefix("select * from xxx where ").toString();

        assert tmp != null;
    }
    @Test
    public  void test1() throws Exception {
        String dic_root = getResource("/").toString().replace("target/classes/","");
        String dic_java = dic_root +"src/main/java/";

        System.out.println(dic_java);
    }

    @Test
    public  void test11() throws Exception {
        URL url = getResource("/weed3/xxx.bbb/SalmonMapper.xml");

        String code = XmlSqlCompiler.parse( url);

        System.out.println(code);
    }

    @Test
    public  void test12() throws Exception {

        XmlSqlLoader.load();

        XmlSqlBlock tmp = XmlSqlFactory.get("org.xxx.xxx.user_add");

        if(tmp == null){
            return;
        }else{
            System.out.println("已找到 IXmlSqlBuilder: org.xxx.xxx.user_add");
        }
    }

    @Test
    public  void test21() throws Exception {
        URL url = getResource("/weed3/UserMapper.xml");

//        XmlSqlMapperGenerator.generator(new File(url.toURI()));
//
//        JavaCodeBlock block = XmlSqlMapperCompiler.parse( new File(url.toURI()));
//
//        System.out.println(block._code);
    }

    @Test
    public  void test22() throws Exception {
        //XmlSqlMapperGenerator.generator();

//        JavaCodeBlock block = XmlSqlMapperCompiler.parse( new File(url.toURI()));
//
//        System.out.println(block._code);
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
