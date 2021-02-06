package weed3rdb.features;

import org.noear.weed.WeedConfig;

public class ConfigDemo {

    public void test1() {
        //监听异常
        WeedConfig.onException((cmd, ex) -> {
            ex.printStackTrace();
        });


        //记录行为
        WeedConfig.onLog((cmd) -> {
            if (cmd.isLog >= 0) { //isLog: -1,不需要记录；0,默认；1,需要记录
                //cmd.text;         //执行代码
                //cmd.paramS;   	//执行参数
                //cmd.paramMap();   //执行参数Map化
            }
        });

        //监听性能
        WeedConfig.onExecuteAft((cmd) -> {
            //cmd.timespan()
        });

        WeedConfig.onExecuteBef((cmd) -> {
            if (cmd.text.indexOf("DELETE ") >= 0) {
                return false;
            }
            return true;
        });
    }
}
