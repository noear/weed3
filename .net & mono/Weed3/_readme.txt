3.0.4.54::
1.DataItem::修改.添加 toJson()
2.DataList::修改.添加 toJson()

3.0.4.53::
1.DbTableQueryBase::修复 .top(...) 错误

3.0.4.52::
1.DbTableQueryBase::修改.添加 count(...);

3.0.4.51::
1.WeedConfig::修改.添加 onExecuteAft
2.WeedConfig::修改.添加 onExecuteBef
3.SQLer::修改.添加 监听的支持（通过:onExecuteAft,onExecuteBef）
4.DbAccess::修改.添加 onCommandBuilt(...); //可再次为cmd做处理
5.DbTableQueryBase::修改.添加 count();
6.DbTableQueryBase::修改.添加 from();
7.DbTableQueryBase::修改.添加 log(...);
1.DbTableQueryBase::修改.添加 updateList(); //测试

3.0.4.50::
1.DataItem::修改.取消 intValue2(); //同上
2.DataItem::修改.取消 longValue2(); //同上
*.由 DataItem.getVariate(name) 解决问题

3.Variate::修改.添加 doubleValue(def); //同上


3.0.4.48::
1.IQuery::修改.取消 getValue(def,cacheCondition)
2.IQuery::修改.添加 getVariate();
3.IQuery::修改.添加 getVariate(cacheCondition);
4.Variate::修改.添加 intValue(def); //支持自动将 int,long,BigDecimal 转为 int
5.Variate::修改.添加 longValue(def); //同上
6.DataItem::修改.添加 intValue2(); //同上
7.DataItem::修改.添加 longValue2(); //同上

3.0.4.47::
1.DbTableQueryBase::修改.添加 insertList();

3.0.4.45::
1.DbTran::修改.添加 isSucceed();
2.DbTranQueue::修改.添加 isSucceed();

3.0.4.44::
1.+exists(expr)


3.0.4.43::
1.ICacheServiceEx::新增 扩展 ICacheService,增加对tags的获取与操作
2.EmptyCache,LocalCache 升级为：ICacheServiceEx
3.DbAccess::修改.取消 tran() //不能形成控制流,没意义

3.0.4.42::
1.SQLBuilder::修改.添加 removeLast() （去除最后一个字符）
2.DbContext::修改.添加 allowMultiQueries //是否支持多语句查询

*.添加 SQLBuilder 的示例代码

3.0.4.41::
1.DbTable::修必.添加 insertList （批量插入数据）
2.DbTableQuery::修改.添加 insertList （批量插入数据）
3.cache/EmptyCache::新增
4.DbProcedure::新增 作为 DbStoredProcedure,DbQueryProcedure 的基类
5.DbAccess::修改{
    caching()->DbAccess 改为: caching()->IQuery
    usingCache()->DbAccess 改为: usingCache()->IQuery
    cacheTag()->DbAccess 改为: cacheTag()->IQuery
}
6.DbContext::修改{
    call()->DbStoredProcedure 改为: call()->DbProcedure//新：根据输入情况反回：DbStoredProcedure 或 DbQueryProcedure
}
