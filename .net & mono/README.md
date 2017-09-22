Weed3::源码<br/>
Weed3Demo::使用示例<br/>
<br/>
更新说明<br/>
------------------<br/>
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
