package webapp;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Db;
import org.noear.weed.ext.Act0;
import org.noear.weed.xml.XmlSqlLoader;
import webapp.model.AppxModel;

import java.lang.annotation.Annotation;

public class App {
    public static void main(String[] args){
        XApp app = XApp.start(App.class,args);

        app.get("/",(c)->{
            c.render("nav.htm", null);
        });
    }
}
