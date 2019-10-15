package org.noear.weed;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by noear on 14-9-5.
 *
 * 查询语句访问类
 *
 * $.tableName  --$ 代表当表db context schema
 * @paramName   --@ 为参数名的开头
 */
public class DbQuery extends DbAccess<DbQuery> {



    public DbQuery(DbContext context)
    {
        super(context);
    }

    public DbQuery sql(SQLBuilder sqlBuilder) {
        this.commandText = sqlBuilder.toString();
        this.paramS.clear();
        this._weedKey = null;
        for (Object p1 : sqlBuilder.paramS) {
            doSet("", p1);
        }

        return this;
    }

    public long insert() throws SQLException
    {
        return new SQLer().insert(getCommand(),_tran);
    }

    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    protected Command getCommand() {

        Command cmd = new Command(this.context,_tran);

        cmd.key     = getCommandID();
        cmd.paramS = new ArrayList<>();
        for(Variate v : this.paramS){
            cmd.paramS.add(v.getValue());
        }

        StringBuilder sb = new StringBuilder(commandText);

        //1.替换schema
        int idx=0;
        while (true) {
            idx = sb.indexOf("$",idx);
            if(idx>0) {
                sb.replace(idx, idx + 1, context.getSchema());
                idx++;
            }
            else {
                break;
            }
        }


        cmd.text = sb.toString();


        runCommandBuiltEvent(cmd);

        return cmd;
    }
}
