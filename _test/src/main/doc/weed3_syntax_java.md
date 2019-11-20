# Weed3接口字典
#### db.table("table") -> new:DbTableQuery
```swift
//
// 构建相关
//

-build(builder:(tq)->{}) -> self //通过表达式构建自己

//例: db.table("user u")
//      .with("a","select type num from group by type")
//      .where("u.type in(select a.type) and u.type2 in (select a.type)")
//      .select("u.*")
//      .getMapList();
-with(name:String,code:String,args:Object...) -> self //添加SQL with 语句

//例1: .where("name=?","x")
//例2: .where("((name=? or id=?) and sex=0)","x",1)
-where(code:String,args:Object...) -> self //添加SQL where 语句 //可使用?,?...占位符（ ?... 表示数组占位符）
-where() -> self //添加SQL where 关键字
-whereEq(filed:String,val:Object) -> self //添加SQL where = 语句
-whereLt(filed:String,val:Object) -> self //添加SQL where < 语句
-whereLte(filed:String,val:Object) -> self //添加SQL where <= 语句
-whereGt(filed:String,val:Object) -> self //添加SQL where > 语句
-whereGte(filed:String,val:Object) -> self //添加SQL where >= 语句
-whereLk(filed:String,val:String) -> self //添加SQL where like 语句

//例1：.and("name=?","x")
//例2: .and("(name=? or id=?)","x",1)
-and(code:String,args:Object...) -> self //添加SQL and 语句 //可使用?,?...占位符（ ?... 表示数组占位符）
-and() -> self //添加SQL where 关键字
-andEq(filed:String,val:Object) -> self //添加SQL and = 语句
-andLt(filed:String,val:Object) -> self //添加SQL and < 语句
-andLte(filed:String,val:Object) -> self //添加SQL and <= 语句
-andGt(filed:String,val:Object) -> self //添加SQL and > 语句
-andGte(filed:String,val:Object) -> self //添加SQL and >= 语句
-andLk(filed:String,val:String) -> self //添加SQL and like 语句


//例1：.or("name=?","x"); 
//例2: .or("(name=? or id=?)","x",1)
-or(code:String,args:Object...) -> self //添加SQL or 语句 //可使用?,?...占位符（ ?... 表示数组占位符）
-or() -> self //添加SQL or 关键字
-orEq(filed:String,val:Object) -> self //添加SQL or = 语句
-orLt(filed:String,val:Object) -> self //添加SQL or < 语句
-orLte(filed:String,val:Object) -> self //添加SQL or <= 语句
-orGt(filed:String,val:Object) -> self //添加SQL or > 语句
-orGte(filed:String,val:Object) -> self //添加SQL or >= 语句
-orLk(filed:String,val:String) -> self //添加SQL or like 语句

-begin() -> self //添加左括号
-begin(code:String,args:Object...) -> self //添加左括号并附加代码//可使用?,?...占位符（ ?... 表示数组占位符）
-end() -> self //添加右括号

-set(name:String,value:Object) -> self
-setMap(data:Map<String,Object>) -> self
-setEntity(data:Object) -> self



-innerJoin(table:String) -> self //添加SQL inner join语句
-leftJoin(table:String) -> self //添加SQL left join语句
-rightJoin(table:String) -> self //添加SQL right join语句
-on(code:String) -> self //添加SQL on语句

-groupBy(code:String) -> self //添加SQL group by语句
-having(code:String) -> self //添加SQL having语句
-having(code:String) -> self //添加SQL having语句

-orderBy(code:String) -> self //添加SQL order by语句

-limit(rows:int) -> self //添加SQL limit语句
-limit(start:int, rows:int) -> self //添加SQL limit语句

-top(rows:int) //添加SQL top 语句

-append(code:String,args:Object...) ->self //添加无限制代码 //可使用?,?...占位符（ ?... 表示数组占位符）

//
// 执行相关
//
-insert() -> long //执行插入并返回自增值，使用set接口的数据
-insert(data:IDataItem) -> long //执行插入并返回自增值，使用data数据
-insert(dataBuilder:(d:DataItem)->{}) -> long //执行插入并返回自增值，使用dataBuilder构建的数据
-insertList(valuesList:List<DataItem>) -> void //执行批量合并插入，使用集合数据
-insertList(valuesList:Collection<T>,dataBuilder:(t,d:DataItem)->{}) -> void //执行批量合并插入，使用集合数据（由dataBuilder构建数据）

-update() ->int //执行更新并返回影响行数，使用set接口的数据
-update(data:IDataItem) ->int //执行更新并返回影响行数，使用set接口的数据
-update(dataBuilder:(d:DataItem)->{}) ->int //执行更新并返回影响行数，使用dataBuilder构建的数据

-updateExt(constraints:String)//使用set接口的数据,根据约束字段自动插入或更新
-updateExt(data:IDataItem,constraints:String)//使用data的数据,根据约束字段自动插入或更新

-delete() -> int //执行删除，并返回影响行数

-select(columns:String) -> IQuery //执行查询，并返回查询接口（有非富的数据获取方式）

-exists() -> boolean //执行查询，并返回存在情况
-count() -> long //执行查询，并返回COUNT(*)值
-count(code:String) -> long //执行查询，并返回COUNT(..) //count code 要自己手写

//
// 控制相关
//
-log(isLog:boolean) -> self //标记是否记录日志
-usingNull(isUsing:boolean) //充许使用null插入或更新
-usingExpr(isUsing:boolean) //充许使用$表达式做为set值


//
// 事务相关
//
-tran(transaction:DbTran) -> self //使用外部事务
-tran() -> self //使用事务


//
// 缓存控制相关
//
-caching(service:ICacheService) //使用一个缓存服务
-usingCache(isCache:boolean) //是否使用缓存
-usingCache(seconds:int) //使用缓存时间（单位：秒）
-cacheTag(tag:String) //为缓存添加标签
```
#### db.call("process") -> new:DbProcedure
```swift
//
// 注：DbProcedure implements IQuery
//

//例1: 调用数据库存储过程
//db.call("user_get").set("_user_id",1).getMap();

//例2: 调用xml sql
//db.call("@webapp.demo.dso.db.user_get").set("user_id",1).getMap();

//例3: 调用有变量的 sql
//db.call("select * from user where user_id=@{user_id}").set("user_id",1).getMap();

//
// 变量设置相关
//
-set(param:String,value:Object) -> self //设置变量
-set(param:String,valueGetter:()->Object) -> self //设置变量
-setMap(map:Map<String,Object>) -> self //设置变量(将map输入)
-setEntity(obj:Object) -> self //设置变量(将实体输入)

//
// 执行相关
// 
-insert() -> long //执行插入（返回自增ID）
-update() -> int //执行更新（返回受影响数）
-delete() -> int //执行删除（返回受影响数）
-execute() -> int //执行命令（返回受影响数）

* 执行查询见 IQuery 接口


//
// 事务相关
//
-tran(transaction:DbTran) -> self //使用外部事务
-tran() -> self //使用事务


//
// 缓存控制相关
//
-caching(service:ICacheService) //使用一个缓存服务
-usingCache(isCache:boolean) //是否使用缓存
-usingCache(seconds:int) //使用缓存时间（单位：秒）
-cacheTag(tag:String) //为缓存添加标签

```

#### db.sql("code") -> new:DbQuery
``` swift
//
// 注：DbQuery implements IQuery
//

//例：db.sql("select * from user_id=?",12).getMap();

//
// 执行相关
// 
-insert() -> long //执行插入（返回自增ID）
-update() -> int //执行更新（返回受影响数）
-delete() -> int //执行删除（返回受影响数）
-execute() -> int //执行命令（返回受影响数）

* 执行查询见 IQuery 接口


//
// 事务相关
//
-tran(transaction:DbTran) -> self //使用外部事务
-tran() -> self //使用事务


//
// 缓存控制相关
//
-caching(service:ICacheService) //使用一个缓存服务
-usingCache(isCache:boolean) //是否使用缓存
-usingCache(seconds:int) //使用缓存时间（单位：秒）
-cacheTag(tag:String) //为缓存添加标签
```

#### db.mapper(Class<?>) -> SqlMapper? proxy
``` java
//xml sql 和 annotation sql 参考另外的资料

//例：UserDbApi udb = db.mapper(UserDbApi.class);
//  UserModel um = m.getUser(12);
```
#### 附：IQuery 接口
```java
```

