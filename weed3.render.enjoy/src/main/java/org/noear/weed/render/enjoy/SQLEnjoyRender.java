package org.noear.weed.render.enjoy;

import com.jfinal.template.Directive;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.jfinal.template.source.FileSourceFactory;
import org.noear.weed.IRender;
import org.noear.weed.utils.IOUtils;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.Map;

public class SQLEnjoyRender implements IRender {
    private static SQLEnjoyRender _global;
    public static SQLEnjoyRender global(){
        if(_global==null){
            _global = new SQLEnjoyRender();
        }

        return _global;
    }



    Engine engine = Engine.use();

    private String _baseUri ="/weed3/";
    //不要要入参，方便后面多视图混用
    //
    public SQLEnjoyRender() {
        boolean isDebugMode = "1".equals(System.getProperty("debug"));

        if (isDebugMode) {
            //添加调试模式
            String dirroot = IOUtils.getResource("/").toString().replace("target/classes/", "");
            String dir_str = dirroot + "src/main/resources"+_baseUri;
            File dir = new File(URI.create(dir_str));
            if (!dir.exists()) {
                dir_str = dirroot + "src/main/webapp"+_baseUri;
                dir = new File(URI.create(dir_str));
            }

            try {
                if (dir.exists()) {
                    engine.setDevMode(true);
                    engine.setBaseTemplatePath(dir.getPath());
                    engine.setSourceFactory(new FileSourceFactory());
                }else{
                    initForRuntime();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            initForRuntime();
        }
    }

    private void initForRuntime(){
        try {
            engine.setBaseTemplatePath(_baseUri);
            engine.setSourceFactory(new ClassPathSourceFactory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addDirective(String name, Class<? extends Directive> clz){
        try {
            engine.addDirective(name, clz);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setSharedVariable(String name,Object value) {
        try {
            engine.addSharedObject(name, value);
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

        Template template = engine.getTemplate(path);
        template.render(args, writer);

        return writer.toString();
    }
}
