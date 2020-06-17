package org.noear.weed.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.noear.weed.IRender;
import org.noear.weed.utils.IOUtils;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.Map;

public class SQLFreemarkerRender implements IRender {
    private static SQLFreemarkerRender _global;
    public static SQLFreemarkerRender global(){
        if(_global==null){
            _global = new SQLFreemarkerRender();
        }

        return _global;
    }



    Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);

    private String _baseUri ="/weed3/";
    //不要要入参，方便后面多视图混用
    //
    public SQLFreemarkerRender() {
        boolean isDebugMode = "1".equals(System.getProperty("debug"));

        if (isDebugMode) {
            forRelease();
        }else {
            forDebug();
        }

        cfg.setNumberFormat("#");
        cfg.setDefaultEncoding("utf-8");
    }

    //尝试 调试模式 进行实始化
    private void forDebug(){
        String dirroot = IOUtils.getResource("/").toString().replace("target/classes/", "");
        String dir_str = dirroot + "src/main/resources"+_baseUri;
        File dir = new File(URI.create(dir_str));
        if (!dir.exists()) {
            dir_str = dirroot + "src/main/webapp"+_baseUri;
            dir = new File(URI.create(dir_str));
        }

        try {
            if (dir.exists()) {
                cfg.setDirectoryForTemplateLoading(dir);
            }else{
                //如果没有找到文件，则使用发行模式
                //
                forRelease();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //使用 发布模式 进行实始化
    private void forRelease(){
        try {
            cfg.setClassForTemplateLoading(this.getClass(), _baseUri);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(0, Integer.MAX_VALUE));
    }

    public void setSharedVariable(String name,Object value) {
        try {
            cfg.setSharedVariable(name, value);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String render(String path, Map<String, Object> args) throws Throwable {
        if (path == null) {
            return null;
        }

        StringWriter writer = new StringWriter();

        Template template = cfg.getTemplate(path, "utf-8");
        template.process(args, writer);

        return writer.toString();
    }
}
