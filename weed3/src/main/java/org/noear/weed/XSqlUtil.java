package org.noear.weed;

import org.noear.weed.utils.StringUtils;
import org.noear.weed.xml.XmlSqlBlock;
import org.noear.weed.xml.XmlSqlFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class XSqlUtil {
    /**
     * @param sqlid =@{namespace}.{id}
     */
    public static Object exec(DbContext db, String sqlid, Map<String, Object> paramS, Class<?> rClz, Type rType) throws Exception {
        //3.获取代码块，并检测有效性
        XmlSqlBlock block = XmlSqlFactory.get(sqlid.substring(1));
        if (block == null) {
            throw new RuntimeException("Xmlsql does not exist:" + sqlid);
        }

        //4.生成命令
        DbProcedure sp = db.call( sqlid);
        if(paramS!=null) {
            sp.setMap(paramS);
        }

        //5.构建输出
        if (block.isSelect()) {
            if (block._return.indexOf(".") > 0) {
                //实体化处理
                if (block._return_item != null) {
                    //是实体集合
                    if (block._return.indexOf("java.lang.") > 0) {
                        return sp.getArray(0);
                    } else {
                        Class<?> rst_clz2 = item_type(rType, block);
                        if (IBinder.class.isAssignableFrom(rst_clz2)) {
                            return sp.getList((IBinder) rst_clz2.newInstance());
                        } else {
                            return sp.getList(rst_clz2);
                        }
                    }

                } else {
                    Class<?> rst_type = rClz;
                    if(rClz == null) {
                        if (StringUtils.isEmpty(block._return) == false) {
                            rst_type  = Class.forName(block._return);
                        }else{
                            rst_type = Void.TYPE;
                        }
                    }

                    //是单实体
                    if (IBinder.class.isAssignableFrom(rst_type)) {
                        return sp.getItem((IBinder) rst_type.newInstance());
                    } else {
                        return sp.getItem(rst_type);
                    }
                }
            } else {
                if (block._return.startsWith("List<")) {
                    return sp.getDataList().toArray(0);
                }

                //普通职处理
                switch (block._return) {
                    case "Map":
                        return sp.getMap();
                    case "MapList":
                        return sp.getMapList();
                    case "DataItem":
                        return sp.getDataItem();
                    case "DataList":
                        return sp.getDataList();
                    default: {
                        Variate val = sp.getVariate();

                        if (val.getValue() == null) {
                            return 0;
                        }

                        //解决 BigDecimal BigInteger 问题
                        if (block._return.toLowerCase().startsWith("int")) {
                            return val.intValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("long")) {
                            return val.longValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("doub")) {
                            return val.doubleValue(0);
                        }

                        if (block._return.toLowerCase().startsWith("str")) {
                            return val.stringValue(null);
                        }

                        return val.getValue();
                    }
                }
            }
        } else {
            if (block.isInsert()) {
                long rst = sp.insert();
                if (block._return != null && block._return.indexOf("ool") > 0) {
                    //说明是bool 或  Boolean
                    return rst > 0;
                } else {
                    return rst;
                }
            } else {
                int rst = sp.execute();
                if (block._return != null && block._return.indexOf("ool") > 0) {
                    //说明是bool 或  Boolean
                    return rst > 0;
                } else {
                    return rst;
                }
            }
        }
    }

    private  static Class<?> item_type(Type rType, XmlSqlBlock block) throws Exception{
        if(rType instanceof ParameterizedType) {
            return (Class<?>) (((ParameterizedType) rType).getActualTypeArguments()[0]);
        }

        if(StringUtils.isEmpty(block._return_item) == false) {
            return getClass(block._return_item);
        }

        return null;
    }

    private static Map<String,Class<?>> _clzMap  =new HashMap<>();
    private static Class<?> getClass(String fullname) throws Exception{
        Class<?> tmp = _clzMap.get(fullname);
        if(tmp == null){
            synchronized (fullname.intern()){
                tmp = _clzMap.get(fullname);
                if(tmp == null){
                    tmp = Class.forName(fullname);
                    _clzMap.put(fullname,tmp);
                }
            }
        }

        return tmp;
    }
}
