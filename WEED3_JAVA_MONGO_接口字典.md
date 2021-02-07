# Weed3 JAVA for mongodb 接口字典



#### db.table("table") -> new:MgTableQuery
```swift
//
// 构建相关
//

-build(builder:(tq)->{}) -> self //通过表达式构建自己

-whereTrue()-> self

-whereMap(map:Map<String,Object>) -> self  //null会默认排除

-whereEq(field:String,val:Object) -> self //添加SQL where = 语句      //val为null时，转为 IS NULL
-whereNeq(field:String,val:Object) -> self //添加SQL where != 语句    //val为null时，转为 IS NOT NULL
-whereLt(field:String,val:Object) -> self //添加SQL where < 语句
-whereLte(field:String,val:Object) -> self //添加SQL where <= 语句
-whereGt(field:String,val:Object) -> self //添加SQL where > 语句
-whereGte(field:String,val:Object) -> self //添加SQL where >= 语句
-whereLk(field:String,val:String) -> self //添加SQL where like 语句
-whereNlk(field:String,val:String) -> self //添加SQL where not like 语句
-whereBtw(field:String, start:Object, end:Object) -> self //添加SQL where BETWEEN .. AND 语句
-whereNbtw(field:String, start:Object, end:Object) -> self //添加SQL where NOT BETWEEN .. AND 语句
-whereIn(field:String, ary:Iterable<Object>)-> self //添加SQL where IN (...) 语句
-whereNin(field:String, ary:Iterable<Object>)-> self //添加SQL where not IN (...) 语句
-whereMod(field:String, base:long, val:long) -> self //添加SQL where mod 语句
-whereNmod(field:String, base:long, val:long) -> self //添加SQL where not mod 语句
-whereAll(field:String, ary:Iterable<Object>)-> self //添加SQL where all (...) 语句
-whereSize(field:String, size:long) -> self //添加SQL where size 语句
-whereExists(field:String, exists:bool) -> self //添加SQL where size 语句

-andEq(field:String,val:Object) -> self //添加SQL and = 语句      //val为null时，转为 IS NULL
-andNeq(field:String,val:Object) -> self //添加SQL and != 语句    //val为null时，转为 IS NOT NULL
-andLt(field:String,val:Object) -> self //添加SQL and < 语句
-andLte(field:String,val:Object) -> self //添加SQL and <= 语句
-andGt(field:String,val:Object) -> self //添加SQL and > 语句
-andGte(field:String,val:Object) -> self //添加SQL and >= 语句
-andLk(field:String,val:String) -> self //添加SQL and like 语句
-andNlk(field:String,val:String) -> self //添加SQL and not like 语句
-andBtw(field:String, start:Object, end:Object) -> self //添加SQL and BETWEEN .. AND 语句
-andNbtw(field:String, start:Object, end:Object) -> self //添加SQL and NOT BETWEEN .. AND 语句
-andIn(field:String, ary:Iterable<Object>)-> self //添加SQL and IN (...) 语句
-andNin(field:String, ary:Iterable<Object>)-> self //添加SQL and not IN (...) 语句
-andMod(field:String, base:long, val:long) -> self //添加SQL and mod 语句
-andNmod(field:String, base:long, val:long) -> self //添加SQL and not mod 语句
-andAll(field:String, ary:Iterable<Object>)-> self //添加SQL and all (...) 语句
-andSize(field:String, size:long) -> self //添加SQL and size 语句
-anemExists(field:String, exists:bool) -> self //添加SQL and size 语句


-set(name:String,value:Object) -> self  //设置变量
-setInc(name:String,value:long) -> self //设置自增变量
-setMap(data:Map<String,Object>) -> self    //设置变量(将map输入)  //null会默认排除
-setEntity(data:Object) -> self     //设置变量(将实体输入) //null会默认排除


-orderByAsc(field:String) -> self //添加SQL order by .. ASC语句
-orderByDesc(field:String) -> self //添加SQL order by .. DESC语句
-andByAsc(field:String) -> self //添加SQL order by .. ASC语句
-andByDesc(field:String) -> self //添加SQL order by .. DESC语句

-limit(size:int) -> self //添加SQL 分页语句（已兼容不同数据库）
-limit(start:int, size:int) -> self //添加SQL 分页语句（已兼容不同数据库）

//
// 执行相关
//
-insert() -> long //执行插入并返回自增值，使用set接口的数据
-insert(data:Map<String,Object>) -> long //执行插入并返回自增值，使用data数据
-insertList(dataList:List<Map<String, Object>>) -> void //执行批量合并插入，使用集合数据

-update() -> long //执行更新并返回影响行数，使用set接口的数据

-replace() - > long

-delete() -> long //执行删除，并返回影响行数

-selectExists() -> boolean //执行查询，并返回存在情况
-selectCount() -> long //执行查询，并返回COUNT(*)值
-selectItem(Class<T> clz)-> T
-selectList(Class<T> clz)-> List<T>
-selectMap()-> Map<String,Object>
-selectMapList()-> List<Map<String,Object>>
-selectArray(field:String)-> List<Object>


//
// 缓存控制相关
//
-caching(service:ICacheService) //使用一个缓存服务
-usingCache(isCache:boolean) //是否使用缓存
-usingCache(seconds:int) //使用缓存时间（单位：秒）
-cacheTag(tag:String) //为缓存添加标签
```

