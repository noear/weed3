package weed3demo.global;

import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;

public class SwiftDemo {
    public void demo() throws Exception{
        //定义两个上下文（具体配置不加了）
        DbContext db = new DbContext();
        DbContext db_r = new DbContext();

        //监听命令构建事件
        WeedConfig.onCommandBuilt((cmd)->{
            //读的话为从库，非读为主库
            if(cmd.text.trim().toUpperCase().startsWith("SELECT ")){
                cmd.context = db_r;
            }
        });

        //执行它时，会自动切换相关的库上
        db.call("@xxx.xxx.xxx").set("user_id",1).getMap();
    }

    public void demo2() throws Exception{
        //定义两个上下文
        DbContext xxx = new DbContext();
        DbContext xxx_r = new DbContext();
        DbContext yyy = new DbContext();
        DbContext yyy_r = new DbContext();
        //
        // .. 假如有很多库
        //

        //监听命令构建事件
        WeedConfig.onCommandBuilt((cmd)->{
            if(cmd.text.trim().toUpperCase().startsWith("SELECT ")) {
                cmd.context = WeedConfig.libOfDb.get(cmd.context.name() + "_r");
            }
        });

        xxx.call("@xxx.xxx.xxx").set("xx",2).execute();
        yyy.call("@yyy.yyy.yyy").set("yy",1).insert();
    }
}
