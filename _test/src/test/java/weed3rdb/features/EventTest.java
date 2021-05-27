package weed3rdb.features;

import org.noear.weed.WeedConfig;

/**
 * @author noear 2021/4/2 created
 */
public class EventTest {
    public void demo(){
        WeedConfig.onExecuteAft((cmd)->{
            System.out.println("[Weed] " + cmd.toSqlString());
        });
    }
}
