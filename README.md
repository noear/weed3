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
示例::入门级<br/>
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
  .set("xxx", 1)  //保持与存储过程参数的序顺一致
  .getItem(new UserInfoModel()); 

//简易.查询过程调用示例，及使用使用示例
db.call("select * from user where user_id=@userID")
  .set("@userID", 1) 
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserInfoModel()); 

//简易.存储过程调用示例，及使用事务示例
db.tran(tran->{
    db.call("$.user_set").set("xxx", 1) 
      .tran(tran) //使用事务
      .execute();
});
```
示例2::事务相关<br/>
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
示例3::缓存控制<br/>
```java
//最简单的缓存控制
db.call("user_get").set("xxx", 1)
    .caching(cache)
    .usingCache(60 * 1000)
    .getItem(new UserInfoModel());
    
//根据查询结果控制缓存
db.call("user_get").set("xxx",1)
    .caching(cache)
    .usingCache(60 * 100)
    .getItem(new UserInfoModel(), (cu, t) => { 
        if (t.user_id == 0)
            cu.usingCache(false);
});

//通过tag维护缓存
//1.缓存并添加简易标签
db.call("user_get").set("xxx", 1)
    .caching(cache)
    .cacheTag("user_"+ 1)
    .usingCache(60 * 1000)
    .getItem(new UserInfoModel());

CacheTags tags = new CacheTags(cache);
//2.1.可根据标签清除缓存
tags.clear("user_" + 1);

//2.2.可根据标签更新缓存
tags.update<UserInfoModel>("user_" + 1, (m)=>{
    m.name = "xxx";
    return m;
});
```
示例4::[存储过程]映射类<br/>
```java
public class user_get_by_id extends DbStoredProcedure
{
    public user_get_by_id()
    {
        super(Config.user);
        call("user_get_by_id");

        //set("{colname}", ()->{popname});
        //
        set("_user_id", ()->user_id);
    }

    public long user_id;
}
```

示例5::[查询过程]映身类<br/>
```java
public class user_get_by_id extends DbQueryProcedure
{
    public user_get_by_id()
    {
        super(Config.user);
        sql("SELECT * FROM `user` where user_id = @user_id;");

        //set("{colname}", ()->{popname});
        //
        set("@user_id", ()->user_id);
    }

    public long user_id;
}
```

更多高级示例请参考Weed3Demo <br/>
--------------------------------------<br/>

by noear
