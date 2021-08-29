package webapp;

import org.noear.solon.Solon;
import org.noear.weed.WeedConfig;

/**
 * @author noear 2021/1/23 created
 */
public class DemoApp {
    public static void main(String[] args){
        Solon.start(DemoApp.class, args,x->{
            WeedConfig.isUsingUnderlineColumnName = false;
        });
    }
}
