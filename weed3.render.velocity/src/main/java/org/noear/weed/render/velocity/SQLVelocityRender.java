package org.noear.weed.render.velocity;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.noear.weed.IRender;
import org.noear.weed.utils.IOUtils;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SQLVelocityRender implements IRender {
    private static SQLVelocityRender _global;
    public static SQLVelocityRender global(){
        if(_global==null){
            _global = new SQLVelocityRender();
        }

        return _global;
    }

    private VelocityEngine velocity = new VelocityEngine();
    private Map<String,Object> _sharedVariable=new HashMap<>();

    private String _baseUri ="/WEB-INF/view/";

    //不要要入参，方便后面多视图混用
    //
    public SQLVelocityRender() {
        boolean isDebugMode = "1".equals(System.getProperty("debug"));

        if (isDebugMode) {
            forDebug();
        }else{
            forRelease();
        }

        velocity.setProperty(Velocity.ENCODING_DEFAULT, getEncoding());
        velocity.setProperty(Velocity.INPUT_ENCODING, getEncoding());

        velocity.init();
    }

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
                velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, dir.getAbsolutePath() + File.separatorChar);
            }else{
                //如果没有找到文件，则使用发行模式
                //
                forRelease();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void forRelease(){
        String root_path = IOUtils.getResource(_baseUri).getPath();

        velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);
        velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, root_path);
    }

    public void loadDirective(Object obj){
        velocity.loadDirective(obj.getClass().getName());
    }

    public void setSharedVariable(String key, Object obj){
        _sharedVariable.put(key, obj);
    }

    public String getEncoding(){
        return "utf-8";
    }


    @Override
    public String render(String path, Map<String, Object> args) throws Throwable {
        //取得velocity的模版
        Template t = velocity.getTemplate(path, getEncoding());

        // 取得velocity的上下文context
        VelocityContext vc = new VelocityContext(args);
        _sharedVariable.forEach((k, v) -> vc.put(k, v));

        StringWriter writer = new StringWriter();
        t.merge(vc, writer);

        return writer.toString();
    }
}
