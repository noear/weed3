# Weed for .net/mono/java
超强跨平台轻量级ORM（无反射；缓存控制；分布式事务；万能绑定）<br/>

支持什么数据库？？？<br/>
与具体数据库无关（或许支持所有数据库）<br/>

特点：<br/>
1.0反射0注解<br/>
2.漂亮的缓存控制<br/>
3.纯代码无任何配置<br/>
4.分布式事务集成<br/>
5.还有万能的数据绑定<br/>

占位符说明：<br/>
 $.       //数据库名占位数<br/>
 $fun     //SQL函数占位符<br/>
 ?        //参数占位符<br/>
 ?...     //数组型参数占位符<br/>

网站:<br/>
 http://www.noear.org<br/>

QQ群：<br/>
 22200020<br/>
 
--------------------------------------<br/>
示例::<br/>
```java
//简易.查询示例
db.table("user_info") 
  .where("user_id<?", 10)
  .select("user_id,name,sex")
  .getList(new UserInfoModel());

//简易.插入示例
db.table("$.test")
  .set("log_time", "$DATE(NOW())")
  .insert();

//简易.更新示例
db.table("test")
  .set("txt", "NOW()xx")
  .set("num", 44)
  .where("id IN (?...)", new int[] { 15,14,16}) //数据参数
  .update();

//简易.存储过程调用示例，及使用使用示例
db.call("user_get")
  .set("xxx", 1) 
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserInfoModel()); 

//简易.存储过程调用示例，及使用事务示例
db.call("$.user_set").set("xxx", 1) 
  .tran() //使用事务
  .execute();
```
示例2::<br/>
```java
//demo1:: //事务组
db.tran((t) => {
    //以下操作在同一个事务里执行
    t.db().sql("insert into test(txt) values(?)", "cc").tran(t).execute();
    t.db().sql("insert into test(txt) values(?)", "dd").tran(t).execute();
    t.db().sql("insert into test(txt) values(?)", "ee").tran(t).execute();

    t.db().sql("update test set txt='1' where id=1").tran(t).execute();
});

//demo2:: //事务队列
DbTranQueue queue = new DbTranQueue();

db.tran().join(queue).execute((t) => {
    db.sql("insert into test(txt) values(?)", "cc").tran(t).execute();
    db.sql("insert into test(txt) values(?)", "dd").tran(t).execute();
    db.sql("insert into test(txt) values(?)", "ee").tran(t).execute();
});

db2.tran().join(queue).execute((t) => {
    db2.sql("insert into test(txt) values(?)", "gg").tran(t).execute();
});

queue.complete();
```

更多高级示例请参考Weed3Demo <br/>
--------------------------------------<br/>

by noear
