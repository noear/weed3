package noear.weed;

import java.util.List;

/**
 * Created by noear on 14-9-5.
 * 命令
 */
public class Command {
    /*命令id*/
    public String       key;
    /*命令文本*/
    public String       text;
    /*命令参数*/
    public List<Variate> paramS;
    /*数据库上下文*/
    public DbContext context;
}
