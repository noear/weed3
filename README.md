# Weed for .net/mono/java
超强跨平台轻量级ORM（无反射；缓存控制；分布式事务；万能绑定）<br/>

支持什么数据库？？？<br/>
与具体数据库无关（或许支持所有数据库）<br/>

占位符说明：<br/>
 $.       //数据库名占位数<br/>
 $fcn     //SQL函数占位符<br/>
 ?        //参数占位符<br/>
 ?...     //数组型参数占位符<br/>

网站:<br/>
 http://www.noear.org<br/>

QQ群：<br/>
 22200020<br/>
 
--------------------------------------<br/>
示例::<br/>
```java
db.table("user_info") //表操作(简易版)
  .where("user_id<?", 10)
  .select("user_id,name,sex")
  .getList(new UserInfoModel());

db.table("$.test")
  .insert(new DataItem().set("log_time", "$DATE(NOW())"));

db.table("test")
  .where("id IN (?...)", new int[] { 15,14,16}) //数据参数
  .update(new DataItem().set("txt", "NOW()xx").set("num", 44));

db.call("user_get").set("xxx", 1) //存储过程操作(简易版)
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserInfoModel()); 

db.call("$.user_set").set("xxx", 1) 
  .tran() //使用事务
  .execute();
```

(高定版)及更多示例请参考Weed3Demo <br/>
--------------------------------------<br/>

by noear
