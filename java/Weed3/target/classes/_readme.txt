3.0.4.103
1.彻底解决@key相互包含的问题

3.0.4.102
1.@key 能数添加List类型支持

3.0.4.100
1.cmd.isLog 改为 int 型（def:0 no:-1 yes:1）

3.0.4.98
1.添加性能计数
2.所有执行对象都添加log(bool)

3.0.4.93
1.DataItem 增加 Iterable 接口
2.DataList 增加 Iterable 接口

3.0.4.91
1.DbTableQueryBase
>插入添加null值的支持（之前会过滤掉）
>批量插入添加null值的支持
>批量更新添加null值的支持

3.0.4.86
1.DbTableQueryBase 添加控制接口（可实现更自由的条件控制）
+begin()
+begint(...)
+end()
-exists() //重构实现代码

2.DataItem 添加序列化接口
+unserialize(...)
+tryUnserialize(...)
+serialize(...)
+trySerialize(...)

3.0.4.81
1.添加 updateExt(...) //即添加也更新

3.0.4.80
1.添加 where(),and(),or() 空参数接口

3.0.4.74
1.DataItem::修复 toJson 对 Date 的处理

3.0.4.72
1.Variate::修改.添加 stringValue();

3.0.4.70
1.DataItem::修改.添加 unserialize(..),serialize()

3.0.4.69
1.DbContext::修改.添加 fieldFormat(...)

3.0.4.67
1.DbTableQueryBase::修改.添加缓存控制接口::
+caching()
+usingCache();
+usingCache();
+cacheTag();

3.0.4.66::
1.DbTableQueryBase::修改.添加rightJoin(...)

3.0.4.65::
1.DbContxt::修改.添加 tranQueue(...);

3.0.4.64::
1.增加对$表达式的验证和过滤
2.WeedConfig::修改.添加 isUsingValueExpression //是否开启$表达式支持（默认开启）
3.DbTableQueryBase::修改.添加 usingExpr(...) //当前操作是否开启$表达式支持（默认为 isUsingValueExpression）



3.0.4.60::
1.DbContxt::修改.添加  isCompilationMode (编译模式;)
/*
DbContxt db = new DbContxt(...);
db.isCompilationMode = true;
db.table(...).where(...).select(...); //不会真正的执行
db.lastCommand.text; //拿到执行的代码
*/

3.0.4.59:: //为JtSQL提供更友好的支持
1.DataItem::修改.添加 get(index)
2.DataList::修改.添加 toArray(index)

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


3.0.4.49::
1.使用getColumnLabel替代getColumnName获取列名

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
1.DbTable::修改.添加 insertList(...) （批量插入数据）
2.DbTableQuery::修改.添加 insertList(...) （批量插入数据）
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

*.添加java版本 demo（即：Weed3Demo）

3.0.4.38::
1.DbTable::修改.添加 set(k,v) 设置值
2.DbTableQuery::修改.添加 set(k,v) 设置值

