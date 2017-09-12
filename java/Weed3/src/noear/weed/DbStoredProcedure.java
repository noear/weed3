package noear.weed;

import noear.weed.ext.Fun0;


/**
 * Created by noear on 14-6-12.
 * 存储过程访问类
 */
public class DbStoredProcedure extends DbProcedure {

    public DbStoredProcedure(DbContext context){
        super(context);
    }

    protected DbStoredProcedure call(String storedProcedure) {
        this.commandText = storedProcedure;
        this.paramS.clear();
        this._weedKey = null;

        return this;
    }

    @Override
    public DbProcedure set(String param, Object value) {
        doSet(param, value);
        return this;
    }

    @Override
    public DbProcedure set(String param, Fun0<Object> valueGetter) {
        doSet(param, valueGetter);
        return this;
    }

    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    protected Command getCommand(){
        Command cmd = new Command(this.context);

        cmd.key      = getCommandID();
        cmd.paramS  = this.paramS;

        StringBuilder sb = new StringBuilder();
        sb.append("{call ");

        if(context.hasSchema()) {
            sb.append(context.getSchema()).append(".");
        }

        sb.append(commandText.trim());

        if(paramS.size()>0) {
            sb.append('(');
            for (Variate p : paramS) {
                sb.append("?,");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(')');
        }
        sb.append('}');

        cmd.text = sb.toString();

        logCommandBuilt(cmd);

        return cmd;
    }
}
