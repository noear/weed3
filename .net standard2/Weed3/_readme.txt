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
