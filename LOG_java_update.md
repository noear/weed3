#### 3.2.22
* 曾加entity生成器

#### 3.2.18
* 取消模板引擎支持，专做xml sql

#### 3.2.16
* WeedConfig 增加命名策略配置支持
* WeedConfig 增加字段类型转换器配置支持
    * 增加数字类型的自由转换；
    * 增加字符串转Date的自动转换;
* Command::test2() 更名为 Command::toSqlString();


#### 3.2.12
* 优化 call("select * from a where id=@{a}") 性能
* 将 limit 1,2 转译为 limit ?,?
* DataItem 支持乎略大小写

#### 3.2.10
* 优化返射能力，可以填充继承字段
* 修改事务的外抛异常为SQLException

#### 3.2.8.6
* Command 增加 text2() ，用于sql 格式化输出

#### 3.2.8.5
* 二级缓存添加缓冲时间支持

#### 3.2.8.1
* 添加 whereMapIf(), whereEntity(), whereEntityIf()
* 统一 whereMap(),whereEntity(), setMap(), setEntity() 内部逻辑；将null排除
* 原weed3.reader包，更名为；weed3.teamplate
* 调整项目目录结构，将非核心框架移到 _extend

#### 3.2.6.3
* cmd.paramMap() 添加var name 输出（如果有）

####  3.2.6.1
* 添加 db.call(process, args) 并支持模板SQL

####  3.2.6
* 添加模板SQL功能；添加4个引擎支持

####  3.2.5.x
* 取消table() lambda 表达式支持（表别表不女子控制）

####  3.2.4.2
* 添加分页组件
* 添加数据库兼容测试
* 取消JSON和序列化处理（交给专业工具处理）

####  3.2.3.16
* 添加更多反射功能

####  3.2.3.10
* 添加BaseMapper
* 添加FieldWrap,ClassWrap, 重写EntityUtils
* ICacheServiceEx 添加 getBy(int,string,()->)
* DataItem取消toJson()，取消java序列化；交给专业框架处理

####  3.2.3.8
* 添加setIf(c,k,v)
* 添加setMapIf(m,(k,v)->bool),setEntityIf(m,(k,v)->bool);

####  3.2.3.7
* 添加setIf(..),whereIf(..),andIf(..),orIf(..)

####  3.2.1.6
* 为缓存服务组件开发提供支持
  >修改 ICacheServiceEx 的 tag 相关操作，改为默认实现
  >添加 ISerializer<T> 接口，定义统一的缓存序列化接口
  >添加 EncryptUtils 提供加密接口，用于维短key的长度

* 增加 weed3.cache.* 相关项目，提供包装好的缓存扩展

####  3.2.1::
* 完善 xm mapper 功能

####  3.2.0.17::
* xml mapper 添加 jar 包运行支持
* DbAccess 添加 update(), delete(); //本质是执行：execute()
* 添加whereEq(),whereLt(),whereLte(),whereGt(),whereGte(),whereLk()
   添加andEq(),andLt(),andLte(),andGt(),andGte(),andLk()
   添加orEq(),orLt(),orLte(),orGt(),orGte(),orLk()

####  3.2.0.15::
* xml mapper 添加 trim 指令

####  3.2.0.2::
* 添加xml mapper 代码生成器（meven plugin）

####  3.2.0.1::
* 添加xml mapper 支持

####  3.1.9
* set(),table("aaa","aaa a", "aaa as a"),join(like table()),select("a,b b,c as c,SUM(d)"),orderBy("a ASC,B,a.a"),groupBy("a.a") 添加关键字处理

####  3.1.8
* DbContext构造函数，取消fieldFormat参数
* DbTableQueryBase，添加append()无限制添加代码
* 添加objectFormat机制（用于格式化对象）

####  3.1.7
* 添加db.exec()接口

####  3.1.6
* 将包名：noear.weed 改为：org.noear.weed
* List<Map<String,Object>> getMapList()
* Map<String,Object> getMap()

####  3.0.5.15
* DataList添加getMapList() -> list<map>

####  3.0.5.11
* 添加更新无条件时抛异常（可以改掉配置）

####  3.0.5.10
* 添加tb.usingNull(true)接口 //默认为false

####  3.0.5.1 （添加部分反射功能）
* DbProcedure： 添加 setMap(map), setEntity(obj)

####  3.0.4.106 （添加部分反射功能）
* DataItem：
   添加 fromEntity(obj), toEntity(cls)
   改名 setData->setMap, getData->getMap
* DbTableQuery：
   添加 setMap(map), setEntity(obj)
* DataList：
   添加 toEntityList(cls)
* IQuery：
   添加 getList(cls), getItem(cls) //多态

####  3.0.4.103
* 彻底解决@key相互包含的问题

####  3.0.4.102
* @key 能数添加List类型支持

####  3.0.4.100
* cmd.isLog 改为 int 型（def:0 no:-1 yes:1）

####  3.0.4.98
* 添加性能计数
* 所有执行对象都添加log(bool)

####  3.0.4.93
* DataItem 增加 Iterable 接口
* DataList 增加 Iterable 接口

####  3.0.4.91
* DbTableQueryBase
>插入添加null值的支持（之前会过滤掉）
>批量插入添加null值的支持
>批量更新添加null值的支持

####  3.0.4.86
* DbTableQueryBase 添加控制接口（可实现更自由的条件控制）
> +begin()
> +begint(...)
> +end()
> -exists() //重构实现代码

* DataItem 添加序列化接口
> +unserialize(...)
> +tryUnserialize(...)
> +serialize(...)
> +trySerialize(...)

####  3.0.4.81
* 添加 updateExt(...) //即添加也更新

####  3.0.4.80
* 添加 where(),and(),or() 空参数接口

#### 3.0.4.74
* DataItem::修复 toJson 对 Date 的处理

####  3.0.4.72
* Variate::修改.添加 stringValue();

####  3.0.4.70
* DataItem::修改.添加 unserialize(..),serialize()

####  3.0.4.69
* DbContext::修改.添加 fieldFormat(...)

####  3.0.4.67
* DbTableQueryBase::修改.添加缓存控制接口::
> +caching()
> +usingCache();
> +usingCache();
> +cacheTag();

####  3.0.4.66::
* DbTableQueryBase::修改.添加rightJoin(...)

####  3.0.4.65::
* DbContxt::修改.添加 tranQueue(...);

####  3.0.4.64::
* 增加对$表达式的验证和过滤
* WeedConfig::修改.添加 isUsingValueExpression //是否开启$表达式支持（默认开启）
* DbTableQueryBase::修改.添加 usingExpr(...) //当前操作是否开启$表达式支持（默认为 isUsingValueExpression）



####  3.0.4.60::
* DbContxt::修改.添加  isCompilationMode (编译模式;)
/*
DbContxt db = new DbContxt(...);
db.isCompilationMode = true;
db.table(...).where(...).select(...); //不会真正的执行
db.lastCommand.text; //拿到执行的代码
*/

####  3.0.4.59:: //为JtSQL提供更友好的支持
* DataItem::修改.添加 get(index)
* DataList::修改.添加 toArray(index)

####  3.0.4.54::
* DataItem::修改.添加 toJson()
* DataList::修改.添加 toJson()

####  3.0.4.53::
* DbTableQueryBase::修复 .top(...) 错误

####  3.0.4.52::
* DbTableQueryBase::修改.添加 count(...);

####  3.0.4.51::
* WeedConfig::修改.添加 onExecuteAft
* WeedConfig::修改.添加 onExecuteBef
* SQLer::修改.添加 监听的支持（通过:onExecuteAft,onExecuteBef）
* DbAccess::修改.添加 onCommandBuilt(...); //可再次为cmd做处理
* DbTableQueryBase::修改.添加 count();
* DbTableQueryBase::修改.添加 from();
* DbTableQueryBase::修改.添加 log(...);
* DbTableQueryBase::修改.添加 updateList(); //测试

####  3.0.4.50::
* DataItem::修改.取消 intValue2(); //同上
* DataItem::修改.取消 longValue2(); //同上
* 由 DataItem.getVariate(name) 解决问题
* Variate::修改.添加 doubleValue(def); //同上


####  3.0.4.49::
* 使用getColumnLabel替代getColumnName获取列名

####  3.0.4.48::
* IQuery::修改.取消 getValue(def,cacheCondition)
* IQuery::修改.添加 getVariate();
* IQuery::修改.添加 getVariate(cacheCondition);
* Variate::修改.添加 intValue(def); //支持自动将 int,long,BigDecimal 转为 int
* Variate::修改.添加 longValue(def); //同上
* DataItem::修改.添加 intValue2(); //同上
* DataItem::修改.添加 longValue2(); //同上

####  3.0.4.47::
* DbTableQueryBase::修改.添加 insertList();

####  3.0.4.45::
* DbTran::修改.添加 isSucceed();
* DbTranQueue::修改.添加 isSucceed();

####  3.0.4.44::
* +exists(expr)

####  3.0.4.43::
* ICacheServiceEx::新增 扩展 ICacheService,增加对tags的获取与操作
* EmptyCache,LocalCache 升级为：ICacheServiceEx
* DbAccess::修改.取消 tran() //不能形成控制流,没意义

####  3.0.4.42::
* SQLBuilder::修改.添加 removeLast() （去除最后一个字符）
* DbContext::修改.添加 allowMultiQueries //是否支持多语句查询
* 添加 SQLBuilder 的示例代码

####  3.0.4.41::
* DbTable::修改.添加 insertList(...) （批量插入数据）
* DbTableQuery::修改.添加 insertList(...) （批量插入数据）
* cache/EmptyCache::新增
* DbProcedure::新增 作为 DbStoredProcedure,DbQueryProcedure 的基类
* DbAccess::修改{
    caching()->DbAccess 改为: caching()->IQuery
    usingCache()->DbAccess 改为: usingCache()->IQuery
    cacheTag()->DbAccess 改为: cacheTag()->IQuery
}
* DbContext::修改{
    call()->DbStoredProcedure 改为: call()->DbProcedure//新：根据输入情况反回：DbStoredProcedure 或 DbQueryProcedure
}

* 添加java版本 demo（即：Weed3Demo）

####  3.0.4.38::
* DbTable::修改.添加 set(k,v) 设置值
* DbTableQuery::修改.添加 set(k,v) 设置值

