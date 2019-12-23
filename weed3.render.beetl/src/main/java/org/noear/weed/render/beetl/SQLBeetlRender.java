package org.noear.weed.render.beetl;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.noear.weed.IRender;
import org.noear.weed.utils.IOUtils;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.Map;

public class SQLBeetlRender implements IRender {

    private static SQLBeetlRender _global;

    public static SQLBeetlRender global() {
        if (_global == null) {
            _global = new SQLBeetlRender();
        }

        return _global;
    }

    Configuration cfg = null;
    GroupTemplate gt = null;

    private String _baseUri = "/weed3/";

    //不要要入参，方便后面多视图混用
    //
    public SQLBeetlRender() {

        try {
            cfg = Configuration.defaultConfiguration();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        cfg.setStatementStart("--");
        cfg.setStatementEnd(null);
        cfg.setStatementStart2("#");
        cfg.setStatementEnd2(null);
        cfg.setPlaceholderStart("${");
        cfg.setPlaceholderEnd("}");

        boolean isDebugMode = "1".equals(System.getProperty("debug"));

        if (isDebugMode) {
            //添加调试模式
            String dirroot = IOUtils.getResource("/").toString().replace("target/classes/", "");
            String dir_str = dirroot + "src/main/resources" + _baseUri;
            File dir = new File(URI.create(dir_str));
            if (!dir.exists()) {
                dir_str = dirroot + "src/main/webapp" + _baseUri;
                dir = new File(URI.create(dir_str));
            }

            try {
                if (dir.exists()) {
                    FileResourceLoader loader = new FileResourceLoader(dir.getAbsolutePath(), "utf-8");
                    gt = new GroupTemplate(loader, cfg);
                } else {
                    initForRuntime();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            initForRuntime();
        }
    }

    private void initForRuntime() {
        try {
            ClasspathResourceLoader loader = new ClasspathResourceLoader(this.getClass().getClassLoader(), _baseUri);
            gt = new GroupTemplate(loader, cfg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void registerTag(String name, Class<?> tag) {
        try {
            gt.registerTag(name, tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSharedVariable(String name, Object value) {
        try {
            gt.getSharedVars().put(name, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String render(String path, Map<String, Object> args) throws Exception {
        if (path == null) {
            return null;
        }

        StringWriter writer = new StringWriter();

        Template template = gt.getTemplate(path);
        template.binding(args);
        template.renderTo(writer);

        return writer.toString();
    }
}
