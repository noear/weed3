package org.noear.weed.xml;

import org.noear.weed.utils.IOUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlSqlLoader {
    public static XmlSqlLoader _g = new XmlSqlLoader();

    private static String _lock = "";

    private  boolean is_loaed = false;
    private List<File> xmlFiles = new ArrayList<>();

    /**
     * 加载扩展文件夹（或文件）
     * */
    public static void load() throws Exception{
        if(_g.is_loaed == false){
            synchronized (_lock){
                if(_g.is_loaed == false){
                    _g.is_loaed = true;

                    _g.do_load();
                }
            }
        }
    }

    public static void tryLoad(){
        try{
            load();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void do_load() throws Exception{
        URL path = IOUtils.getResource("/weed3/");
        File dic = new File(path.toURI());
        _g.do_load(dic);

        List<String> codes = new ArrayList<>();
        for(File file : _g.xmlFiles){
            String code = XmlSqlCompiler.parse(file);
            codes.add(code);
        }

        boolean is_ok = JavaStringCompiler.instance().compiler(codes);
        if(is_ok){
            JavaStringCompiler.instance().loadClassAll(true);
        }else{
            String error = JavaStringCompiler.instance().getCompilerMessage();
            System.out.println(error);
            throw new RuntimeException(error);
        }
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
}
