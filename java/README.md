Weed3::源码<br/>
Weed3Demo::使用示例<br/>
<br/>
更新说明<br/>
------------------<br/>
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
