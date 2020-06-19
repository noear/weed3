# Weed3 JAVA 接口字典



#### db.mapper(clz) -> SqlMapper? proxy
* db.mapper(XxxMapper.class) -> XxxMapper
* db.mapperBase(XxxEntity.class) -> BaseMapper\<XxxEntity>
* db.mapper(xsqlid, args) -> Object
``` java
//xml sql 和 annotation sql 参考另外的资料

//例1：UserMapper dao = db.mapper(UserMapper.class);
//   UserModel um = dao.getUser(12);

//例2：BaseMapper<User> dao = db.mapperBase(User.class);
//   UserModel um = dao.selectById(12);

//例3：UserModel um = db.mapper("@demo.dao.db.user_get",{app_id:12});
```


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
-whereIf(condition:bool, code:String,args:Object...) -> self
-whereMap(map:Map<String,Object>) -> self  //null会默认排除
-whereMapIf(map:Map<String,Object>, condition:(k,v)->bool) -> self
-whereEntity(entity:Object) -> self //null会默认排除
-whereEntityIf(entity:Object, condition:(k,v)->bool) -> self;
-where() -> self //添加SQL where 关键字
-whereEq(col:String,val:Object) -> self //添加SQL where = 语句      //val为null时，转为 IS NULL
-whereNeq(col:String,val:Object) -> self //添加SQL where != 语句    //val为null时，转为 IS NOT NULL
-whereLt(col:String,val:Object) -> self //添加SQL where < 语句
-whereLte(col:String,val:Object) -> self //添加SQL where <= 语句
-whereGt(col:String,val:Object) -> self //添加SQL where > 语句
-whereGte(col:String,val:Object) -> self //添加SQL where >= 语句
-whereLk(col:String,val:String) -> self //添加SQL where like 语句
-whereNlk(col:String,val:String) -> self //添加SQL where not like 语句
-whereBtw(col:String, start:Object, end:Object) -> self //添加SQL where BETWEEN .. AND 语句
-whereNbtw(col:String, start:Object, end:Object) -> self //添加SQL where NOT BETWEEN .. AND 语句
-whereIn(col:String, ary:Iterable<Object>)-> self //添加SQL where IN (...) 语句
-whereNin(col:String, ary:Iterable<Object>)-> self //添加SQL where not IN (...) 语句

//例1：.and("name=?","x")
//例2: .and("(name=? or id=?)","x",1)
-and(code:String,args:Object...) -> self //添加SQL and 语句 //可使用?,?...占位符（ ?... 表示数组占位符）
-andIf(condition:bool, code:String,args:Object...) -> self
-and() -> self //添加SQL where 关键字
-andEq(col:String,val:Object) -> self //添加SQL and = 语句      //val为null时，转为 IS NULL
-andNeq(col:String,val:Object) -> self //添加SQL and != 语句    //val为null时，转为 IS NOT NULL
-andLt(col:String,val:Object) -> self //添加SQL and < 语句
-andLte(col:String,val:Object) -> self //添加SQL and <= 语句
-andGt(col:String,val:Object) -> self //添加SQL and > 语句
-andGte(col:String,val:Object) -> self //添加SQL and >= 语句
-andLk(col:String,val:String) -> self //添加SQL and like 语句
-andNlk(col:String,val:String) -> self //添加SQL and not like 语句
-andBtw(col:String, start:Object, end:Object) -> self //添加SQL and BETWEEN .. AND 语句
-andNbtw(col:String, start:Object, end:Object) -> self //添加SQL and NOT BETWEEN .. AND 语句
-andIn(col:String, ary:Iterable<Object>)-> self //添加SQL and IN (...) 语句
-andNin(col:String, ary:Iterable<Object>)-> self //添加SQL and not IN (...) 语句

//例1：.or("name=?","x"); 
//例2: .or("(name=? or id=?)","x",1)
-or(code:String,args:Object...) -> self //添加SQL or 语句 //可使用?,?...占位符（ ?... 表示数组占位符）
-orIf(condition:bool, code:String,args:Object...) -> self
-or() -> self //添加SQL or 关键字
-orEq(col:String,val:Object) -> self //添加SQL or = 语句        //val为null时，转为 IS NULL
-orNeq(col:String,val:Object) -> self //添加SQL or != 语句      //val为null时，转为 IS NOT NULL
-orLt(col:String,val:Object) -> self //添加SQL or < 语句
-orLte(col:String,val:Object) -> self //添加SQL or <= 语句
-orGt(col:String,val:Object) -> self //添加SQL or > 语句
-orGte(col:String,val:Object) -> self //添加SQL or >= 语句
-orLk(col:String,val:String) -> self //添加SQL or like 语句
-orNlk(col:String,val:String) -> self //添加SQL or not like 语句
-orBtw(col:String, start:Object, end:Object) -> self //添加SQL or BETWEEN .. AND 语句
-orNbtw(col:String, start:Object, end:Object) -> self //添加SQL or NOT BETWEEN .. AND 语句
-orIn(col:String, ary:Iterable<Object>)-> self //添加SQL or IN (...) 语句
-orNin(col:String, ary:Iterable<Object>)-> self //添加SQL or not IN (...) 语句

-begin() -> self //添加左括号
-begin(code:String,args:Object...) -> self //添加左括号并附加代码//可使用?,?...占位符（ ?... 表示数组占位符）
-end() -> self //添加右括号

-set(name:String,value:Object) -> self  //设置变量
-setIf(condition:bool,name:String,value:Object) -> self
-setMap(data:Map<String,Object>) -> self    //设置变量(将map输入)  //null会默认排除
-setMapIf(data:Map<String,Object>,(k,v)->bool) -> self
-setEntity(data:Object) -> self     //设置变量(将实体输入) //null会默认排除
-setEntityIf(data:Object,(k,v)->bool) -> self



-innerJoin(table:String) -> self //添加SQL inner join语句
-leftJoin(table:String) -> self //添加SQL left join语句
-rightJoin(table:String) -> self //添加SQL right join语句
-on(code:String) -> self //添加SQL on语句
-onEq(col1:String, col2:String) -> self //添加SQL on == 语句

-groupBy(code:String) -> self //添加SQL group by语句
-having(code:String) -> self //添加SQL having语句

-orderBy(code:String) -> self //添加SQL order by语句
-orderByAsc(col:String) -> self //添加SQL order by .. ASC语句
-orderByDesc(col:String) -> self //添加SQL order by .. DESC语句

-limit(size:int) -> self //添加SQL 分页语句（已兼容不同数据库）
-limit(start:int, size:int) -> self //添加SQL 分页语句（已兼容不同数据库）

-paging(start:int, size:int) //与limt一样，提供不同的用语习惯
-top(size:int) //与limt一样，提供不同的用语习惯

-append(code:String,args:Object...) ->self //添加无限制代码 //可使用?,?...占位符（ ?... 表示数组占位符）

//
// 执行相关
//
-insert() -> long //执行插入并返回自增值，使用set接口的数据
-insert(data:IDataItem) -> long //执行插入并返回自增值，使用data数据
-insert(dataBuilder:(d:DataItem)->{}) -> long //执行插入并返回自增值，使用dataBuilder构建的数据
-insertList(valuesList:List<DataItem>) -> void //执行批量合并插入，使用集合数据
-insertList(valuesList:Collection<T>,dataBuilder:(t,d:DataItem)->{}) -> void //执行批量合并插入，使用集合数据（由dataBuilder构建数据）

-insertBy(constraints:String)//使用set接口的数据,根据约束字段自动插入 // exists + insert 的结合体
-insertBy(data:IDataItem,constraints:String)//使用data的数据,根据约束字段自动插入 // exists + insert 的结合体


-update() ->int //执行更新并返回影响行数，使用set接口的数据
-update(data:IDataItem) ->int //执行更新并返回影响行数，使用set接口的数据
-update(dataBuilder:(d:DataItem)->{}) ->int //执行更新并返回影响行数，使用dataBuilder构建的数据

-upsertBy(constraints:String)//使用set接口的数据,根据约束字段自动插入或更新 // exists + update + insert 的结合体
-upsertBy(data:IDataItem,constraints:String)//使用data的数据,根据约束字段自动插入或更新 // exists + update + insert 的结合体

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
-set(name:String,value:Object) -> self  //设置变量
-setIf(condition:bool,name:String,value:Object) -> self
-setMap(data:Map<String,Object>) -> self    //设置变量(将map输入)
-setMapIf(data:Map<String,Object>,(k,v)->bool) -> self
-setEntity(data:Object) -> self     //设置变量(将实体输入)
-setEntityIf(data:Object,(k,v)->bool) -> self

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


#### 附：IQuery 接口
```java

public interface IQuery extends ICacheController<IQuery> {
     long getCount() throws SQLException;
     Object getValue() throws SQLException;
     <T> T getValue(T def) throws SQLException;

     Variate getVariate() throws SQLException;
     Variate getVariate(Act2<CacheUsing,Variate> cacheCondition) throws SQLException;

     <T extends IBinder> T getItem(T model) throws SQLException;
     <T extends IBinder> T getItem(T model, Act2<CacheUsing, T> cacheCondition) throws SQLException;


     <T extends IBinder> List<T> getList(T model) throws SQLException;
     <T extends IBinder> List<T> getList(T model, Act2<CacheUsing, List<T>> cacheCondition) throws SQLException;

     <T> T getItem(Class<T> cls) throws SQLException;
     <T> T getItem(Class<T> cls,Act2<CacheUsing, T> cacheCondition) throws SQLException;

     <T> List<T> getList(Class<T> cls) throws SQLException;
     <T> List<T> getList(Class<T> cls,Act2<CacheUsing, List<T>> cacheCondition) throws SQLException;

     DataList getDataList() throws SQLException;
     DataList getDataList(Act2<CacheUsing, DataList> cacheCondition) throws SQLException;
     DataItem getDataItem() throws SQLException;
     DataItem getDataItem(Act2<CacheUsing, DataItem> cacheCondition) throws SQLException;

     List<Map<String,Object>> getMapList() throws SQLException;
     Map<String,Object> getMap() throws SQLException;

     <T> List<T> getArray(String column) throws SQLException;
     <T> List<T> getArray(int columnIndex) throws SQLException;
}
```

