Weed3::源码<br/>
Weed3Demo::使用示例<br/>
<br/>
更新说明<br/>
------------------<br/>
<br/>
3.1.6<br/>
1.将包名：noear.weed 改为：org.noear.weed<br/>
2.List<Map<String,Object>> getMapList() <br/>
3.Map<String,Object> getMap() <br/>
<br/>
3.0.5.15<br/>
1.DataList添加getMapList() -> list<map><br/>
<br/>
3.0.5.11<br/>
1.添加更新无条件时抛异常（可以改掉配置）<br/>
<br/>
3.0.5.10<br/>
1.添加tb.usingNull(true)接口 //默认为false<br/>
<br/>
3.0.5.1 （添加部分反射功能）<br/>
1.DbProcedure<br/>
  添加 setMap(map), setEntity(obj)<br/>
<br/>
3.0.4.106 （添加部分反射功能）<br/>
1.DataItem<br/>
  添加 fromEntity(obj), toEntity(cls)<br/>
  改名 setData->setMap, getData->getMap<br/>
2.DbTableQuery<br/>
  添加 setMap(map), setEntity(obj)<br/>
3.DataList<br/>
  添加 toEntityList(cls)<br/>
4.IQuery<br/>
  添加 getList(cls), getItem(cls) //多态<br/>
<br/>  
3.0.4.103<br/>
1.彻底解决@key类型参数相互包含的问题<br/>
<br/>
3.0.4.102<br/>
1.@key类型参数添加Iterable类型支持<br/>
<br/>
3.0.4.100<br/>
1.cmd.isLog 改为 int 型（def:0 no:-1 yes:1）<br/>
<br/>
3.0.4.98<br/>
1.添加性能计数<br/>
2.所有执行对象都添加log(bool)<br/>
<br/>
3.0.4.93<br/>
1.DataItem 增加 Iterable 接口<br/>
2.DataList 增加 Iterable 接口<br/>
<br/>
3.0.4.91<br/>
1.DbTableQueryBase<br/>
>插入添加null值的支持（之前会过滤掉）<br/>
>批量插入添加null值的支持<br/>
>批量更新添加null值的支持<br/>
<br/>
3.0.4.86<br/>
1.DbTableQueryBase 添加控制接口（可实现更自由的条件控制）<br/>
+begin()<br/>
+begint(...)<br/>
+end()<br/>
-exists() //重构实现代码<br/> 
<br/>
2.DataItem 添加序列化接口<br/>
+unserialize(...)<br/>
+tryUnserialize(...)<br/>
+serialize(...)<br/>
+trySerialize(...)<br/>
<br/>
3.0.4.81<br/>
1.添加 updateExt(...) //即添加也更新<br/>
<br/>
3.0.4.80<br/>
1.添加 where(),and(),or() 空参数接口<br/>
<br/>
3.0.4.74<br/>
1.DataItem::修复 toJson 对 Date 的处理<br/>
<br/>
3.0.4.72<br/>
1.Variate::修改.添加 stringValue();<br/>
<br/>
3.0.4.70<br/>
1.DataItem::修改.添加 unserialize(..),serialize()<br/>
<br/>
3.0.4.69<br/>
1.DbContext::修改.添加 fieldFormat(...)<br/>
3.0.4.67<br/>
1.DbTableQueryBase::修改.添加缓存控制接口::<br/>
+caching()<br/>
+usingCache();<br/>
+usingCache();<br/>
+cacheTag();<br/>
<br/>
3.0.4.66::<br/>
1.DbTableQueryBase::修改.添加rightJoin(...)<br/>
<br/>
3.0.4.65::<br/>
1.DbContxt::修改.添加 tranQueue(...);<br/>
<br/>
3.0.4.64::<br/>
1.增加对$表达式的验证和过滤<br/>
2.WeedConfig::修改.添加 isUsingValueExpression //是否开启$表达式支持（默认开启）<br/>
3.DbTableQueryBase::修改.添加 usingExpr(...) //当前操作是否开启$表达式支持（默认为 isUsingValueExpression）<br/>
<br/>
3.0.4.60::<br/>
1.DbContxt::修改.添加  isCompilationMode (编译模式;)<br/>
/*<br/>
DbContxt db = new DbContxt(...);<br/>
db.isCompilationMode = true;<br/>
db.table(...).where(...).select(...); //不会真正的执行<br/>
db.lastCommand.text; //拿到执行的代码<br/>
*/<br/>
<br/>
3.0.4.59:: //为JtSQL提供更友好的支持<br/>
1.DataItem::修改.添加 get(index)<br/>
2.DataList::修改.添加 toArray(index)<br/>
<br/>
3.0.4.54::<br/>
1.DataItem::修改.添加 toJson()<br/>
2.DataList::修改.添加 toJson()<br/>
<br/>
3.0.4.53::<br/>
1.DbTableQueryBase::修复 .top(...) 错误<br/>
<br/>
3.0.4.52::<br/>
1.DbTableQueryBase::修改.添加 count(...);<br/>
<br/>
3.0.4.51::<br/>
1.WeedConfig::修改.添加 onExecuteAft<br/>
2.WeedConfig::修改.添加 onExecuteBef<br/>
3.SQLer::修改.添加 监听的支持（通过:onExecuteAft,onExecuteBef）<br/>
4.DbAccess::修改.添加 onCommandBuilt(...); //可再次为cmd做处理<br/>
5.DbTableQueryBase::修改.添加 count();<br/>
6.DbTableQueryBase::修改.添加 from();<br/>
7.DbTableQueryBase::修改.添加 log(...);<br/>
1.DbTableQueryBase::修改.添加 updateList(); //测试<br/>
<br/>
3.0.4.50::<br/>
1.DataItem::修改.取消 intValue2(); //同上<br/>
2.DataItem::修改.取消 longValue2(); //同上<br/>
*.由 DataItem.getVariate(name) 解决问题<br/>
<br/>
3.Variate::修改.添加 doubleValue(def); //同上<br/>

<br/>
3.0.4.48::<br/>
1.IQuery::修改.取消 getValue(def,cacheCondition)<br/>
2.IQuery::修改.添加 getVariate();<br/>
3.IQuery::修改.添加 getVariate(cacheCondition);<br/>
4.Variate::修改.添加 intValue(def); //支持自动将 int,long,BigDecimal 转为 int<br/>
5.Variate::修改.添加 longValue(def); //同上<br/>
6.DataItem::修改.添加 intValue2(); //同上<br/>
7.DataItem::修改.添加 longValue2(); //同上<br/>
<br/>
3.0.4.47::
1.DbTableQueryBase::修改.添加 insertList();<br/>
<br/>
3.0.4.45::<br/>
1.DbTran::修改.添加 isSucceed();<br/>
2.DbTranQueue::修改.添加 isSucceed();<br/>
<br/>
3.0.4.44::<br/>
1.+exists(expr)<br/>
<br/>
<br/>
3.0.4.42::<br/>
1.SQLBuilder::修改.添加 removeLast() （去除最后一个字符）<br/>
2.DbContext::修改.添加 allowMultiQueries //是否支持多语句查询<br/>
<br/>
*.添加 SQLBuilder 的示例代码<br/>
<br/>
3.0.4.41::<br/>
1.DbTable::修改.添加 insertList(...) （批量插入数据）<br/>
2.DbTableQuery::修改.添加 insertList(...) （批量插入数据）<br/>
3.cache/EmptyCache::新增<br/>
4.DbProcedure::新增 作为 DbStoredProcedure,DbQueryProcedure 的基类<br/>
5.DbAccess::修改{<br/>
    caching()->DbAccess 改为: caching()->IQuery<br/>
    usingCache()->DbAccess 改为: usingCache()->IQuery<br/>
    cacheTag()->DbAccess 改为: cacheTag()->IQuery<br/>
}<br/>
6.DbContext::修改{<br/>
    call()->DbStoredProcedure 改为: call()->DbProcedure//新：根据输入情况反回：DbStoredProcedure 或 DbQueryProcedure<br/>
}<br/>
<br/>
*.添加java版本 demo（即：Weed3Demo）<br/>
