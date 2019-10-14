package org.noear.weed.xml;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlSqlLoader {
    public static XmlSqlLoader _g = new XmlSqlLoader();


    private List<File> xmlFiles = new ArrayList<>();
    /**
     * 加载扩展文件夹（或文件）
     * */
    public static void load() throws Exception{
        URL path = getResource("/weed3/");
        File dic = new File(path.toURI());
        _g.do_load(dic);

        List<String> codes = new ArrayList<>();
        for(File file : _g.xmlFiles){
            String code = XmlSqlCompiler.parse(file);
            codes.add(code);
        }

        boolean is_ok = StringJavaCompiler.instance().compiler(codes);
        if(is_ok){
            StringJavaCompiler.instance().loadClassAll(true);
        }else{
            String error = StringJavaCompiler.instance().getCompilerMessage();
            System.out.println(error);
            throw new RuntimeException(error);
        }
    }

    /**
     * 加载扩展具体的jar文件
     * */
    public static void loadXml(File file) {
        _g.do_loadFile(file);
    }


    private XmlSqlLoader() { }

    /** 如果是目录的话，只处理一级 */
    private void do_load(File file) {
        if (file.exists() == false) {
            return;
        }

        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                do_loadFile(tmp);
            }
        } else {
            do_loadFile(file);
        }
    }


    private void do_loadFile(File file) {
        if (file.isFile()) {
            String path = file.getAbsolutePath();
            try {
                //尝试加载jar包
                if (path.endsWith(".xml")) {
                    xmlFiles.add(file);
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //res::获取资源的RUL
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
