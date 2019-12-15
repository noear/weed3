package org.noear.weed;

import org.noear.weed.cache.CacheState;
import org.noear.weed.ext.Fun0;
import org.noear.weed.ext.Fun1;
import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.utils.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 2017/7/22.
 */
public abstract class DbProcedure extends DbAccess<DbProcedure> {
    public DbProcedure(DbContext context) {
        super(context);
    }

    abstract public DbProcedure set(String param, Object value);
    abstract public DbProcedure set(String param, Fun0<Object> valueGetter);
    abstract public DbProcedure setMap(Map<String, Object> map);
    abstract public DbProcedure setEntity(Object obj);

    public DbProcedure setIf(boolean condition, String param, Object value){
        if(condition){
            set(param,value);
        }
        return this;
    }
    public DbProcedure setMapIf(Map<String, Object> map, Fun2<Boolean,String,Object> condition){
        if(map!=null){
            map.forEach((k,v)->{
                if(condition.run(k,v)){
                    set(k,v);
                }
            });
        }
        return this;
    }
    public DbProcedure setEntityIf(Object obj, Fun2<Boolean,String,Object> condition) {
        EntityUtils.fromEntity(obj, (k, v) -> {
            if (condition.run(k, v)) {
                set(k, v);
            }
        });
        return this;
    }

    //=================================
    //
    //以下未测试
    //

    public <T extends IBinder> List<T> getListBySplit(T model , String splitParamName, Fun1<String,T> getKey) throws SQLException
    {
        //如果没有缓存,则直接返回执行结果
        //
        if (_cache == null ||_cache.cacheController == CacheState.NonUsing) {
            return getList(model);
        }


        //1.获取所有分拆后的WeedCode
        //
        List<ValueMapping> vmlist = new ArrayList<>(do_splitWeedCode(splitParamName));

        List<T> list = new ArrayList<T>(vmlist.size());
        StringBuilder sb = StringUtils.borrowBuilder();

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

            String value = StringUtils.releaseBuilder(sb);
            doGet(splitParamName).setValue(value);

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

        for (String value : subKeyValue) {
            list.add(do_buildSubWeedCode(paramName, value));
        }

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
        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append(this.getCommandID() + ":");

        for (Variate item : paramS) {
            if (item.getName() == paramName) {
                sb.append("_" + value.trim());
            }
            else {
                Object val = item.getValue();

                if (val == null) {
                    sb.append("_null");
                }
                else {
                    sb.append("_" + val.toString());
                }
            }
        }

        return new ValueMapping(value, StringUtils.releaseBuilder(sb));
    }
}
