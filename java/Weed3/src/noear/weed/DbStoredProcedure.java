package noear.weed;

import noear.weed.cache.CacheState;
import noear.weed.ext.Fun0;
import noear.weed.ext.Fun1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by noear on 14-6-12.
 * 存储过程访问类
 */
public class DbStoredProcedure extends DbAccess<DbStoredProcedure> {

    public DbStoredProcedure(DbContext context){
        super(context);
    }

    protected DbStoredProcedure call(String storedProcedure) {
        this.commandText = storedProcedure;
        this.paramS.clear();
        this._weedKey = null;

        return this;
    }

    public DbStoredProcedure set(String param, Object value) {
        doSet(param, value);
        return this;
    }

    public DbStoredProcedure set(String param, Fun0<Object> valueGetter) {
        doSet(param, valueGetter);
        return this;
    }


    @Override
    protected String getCommandID() {
        return this.commandText;
    }

    @Override
    protected Command getCommand(){
        Command cmd = new Command();

        cmd.key      = getCommandID();
        cmd.context = this.context;
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

        return cmd;
    }


    //=================================
    //
    //以下未测试
    //

    public <T extends IBinder> List<T> getListBySplit(T model , String splitParamName, Fun1<String,T> getKey) throws SQLException
    {
        //如果没有缓存,则直接返回执行结果
        //
        if (_cache == null ||_cache.cacheController == CacheState.NonUsing)
            return getList(model);


        //1.获取所有分拆后的WeedCode
        //
        List<ValueMapping> vmlist = new ArrayList<>(do_splitWeedCode(splitParamName));

        List<T> list = new ArrayList<T>(vmlist.size());
        StringBuilder sb = new StringBuilder();

        //2.根据WeedCode=>WeedKey获取已缓存的数据
        //
        for (ValueMapping vm : vmlist)
        {
            T temp = _cache.getOnly(vm.weedCode);

            if (temp != null)
            {
                vm.isCached = true;
                list.add(temp);
            }
            else
            {
                vm.isCached = false;
                sb.append(vm.value + ",");
            }
        }

        //3.获取未缓存的数据，并进行缓存
        //
        if (sb.length() > 0)
        {
            sb.delete(sb.length() - 1, 1);

            doGet(splitParamName).setValue(sb.toString());

            List<T> newList1 = getList(model);

            for (T ent : newList1)
            {
                String weedKey = do_getSubWeedCode(vmlist, splitParamName, getKey.run(ent));

                _cache.storeOnly(weedKey, ent);
            }

            list.addAll(newList1);
        }

        return list;
    }

    //-------

    private List<ValueMapping> do_splitWeedCode(String paramName)
    {
        List<ValueMapping> list = new ArrayList<>();

        String[] subKeyValue = doGet(paramName).getValue().toString().split(",");

        for (String value : subKeyValue)
            list.add(do_buildSubWeedCode(paramName, value));

        return list;
    }

    private String do_getSubWeedCode(List<ValueMapping> vmList, String splitParamName, String value)
    {
        for (ValueMapping vm : vmList)
        {
            if (vm.value.equals(value))
                return vm.weedCode;
        }

        return do_buildSubWeedCode(splitParamName, value).weedCode;
    }

    private ValueMapping do_buildSubWeedCode(String paramName, String value) {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getCommandID() + ":");

        for (Variate item : paramS) {
            if (item.getName() == paramName)
                sb.append("_" + value.trim());
            else {
                Object val = item.getValue();

                if (val == null)
                    sb.append("_null");
                else
                    sb.append("_" + val.toString());
            }
        }

        return new ValueMapping(value, sb.toString());
    }
}
