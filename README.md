## QQ交流群：22200020

# Weed for java
超强跨平台轻量级ORM（无反射；无注解；缓存控制；事务队列；万能绑定）<br/>

支持什么数据库？？？<br/>
与具体数据库无关（或许支持所有数据库）<br/>

理念：<br/>
高性能、跨平台、轻量、有个性<br/>

特点：<br/>
1.零反射零注解(后来加了一点点反射的的可选功能...)<br/>
2.漂亮的缓存控制（缓存服务由外部提供）<br/>
3.纯代码无任何配置<br/>
4.分布式事务集成<br/>
5.万能的数据绑定<br/>

占位符说明：<br/>
 $.       //数据库名占位数<br/>
 $fun     //SQL函数占位符<br/>
 ?        //参数占位符<br/>
 ?...     //数组型参数占位符<br/>
 @...     //参数变量占位符<br/>

网站:<br/>
 http://www.noear.org<br/>
 
更新日志：<br/>
 https://github.com/noear/Weed3/blob/master/java/Weed3/_readme.txt <br/>
 
```xml
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3</artifactId>
  <version>3.1.8</version>
</dependency>
```
 
--------------------------------------<br/>
示例1.1.1::入门级<br/>
```java
//DbContext db  = new DbContext("user","proxool.xxx_db"); //使用proxool线程池配置的示例
//DbContext db  = new DbContext("user",new HikariDataSource(...)); //使用DataSource配置的示例
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");

//快速.执行示例
db.exec("DELETE FROM user_info WHERE user_id<?",10);

//简易.查询示例
db.table("user_info").where("user_id<?", 10).count();

db.table("user_info").where("user_id<?", 10).exists();
  
db.table("user_info") 
  .where("user_id<?", 10)
  .select("user_id,name,sex")
  .getMapList(); //.getDataList() //.getList(new UserInfoModel()) //.getList(UserInfoModel.class)

//简易.关联查询示例
db.table("user_info u")
  .innerJoin("user_ex e").on("u.useer_id = e.user_id")
  .where("u.user_id<?", 10)
  .limit(1)
  .select("u.user_id,u.name,u.sex")
  .getMap(); //.getDataItem() //.getItem(new UserInfoModel()) //.getItem(UserInfoModel.class)
                
//简易.插入示例
db.table("$.test")
  .set("log_time", "$DATE(NOW())")
  .insert();

//简易.批量插入示例
db.table("test")
  .insertList(list,(d,m)->{
      m.set("log_time", "$DATE(NOW())");
      m.set("name",d.name);
  });

//简易.更新示例
db.table("test")
  .set("txt", "NOW()xx")
  .set("num", 44)
  .where("id IN (?...)", new int[] { 15,14,16}) //数组参数
  .update();
  
//简易.更新插入一体
db.table("test")
  .set("obj_id", 1)
  .set("meta_key", "name")
  .set("meta_val", 44)
  .updateExt("obj_id,meta_key"); //如果存在则更新；否则插入

//简易.存储过程调用示例，及使用使用示例
db.call("user_get")
  .set("xxx", 1)  //保持与存储过程参数的序顺一致
  .getItem(new UserInfoModel()); 

//简易.查询过程调用示例，及使用使用示例
db.call("select * from user where user_id=@userID") //@userID,参数占位符
  .set("@userID", 1) 
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserInfoModel()); 

//简易.存储过程调用示例，及使用事务示例
db.tran(tran->{
    db.call("user_set").set("xxx", 1) 
      .tran(tran) //使用事务
      .execute();
});
```

示例1.1.2::不确定因素的连式处理支持<br/>
```java
//连式处理::对不确定的条件拼装
db.table("test")
  .expre(tb -> {
      tb.where("1=1");

      if (1 == 2) {
          tb.and("mobile=?", "xxxx");
      } else {
          tb.and("icon=?", "xxxx");
      }
  }).select("*");
  
//连式处理::对不确定字段的插入
db.table("test")
  .expre(tb -> {
      tb.set("name", "xxx");

      if (1 == 2) {
          tb.set("mobile", "xxxx");
      } else {
          tb.set("icon", "xxxx");
      }
  }).insert(); 
```
示例1.1.3::基于反射功能（应用户要求...）<br/>
```java
//简易.插入示例2 //::3.0.4.106起支持
db.table("$.test")
  .setMap(map) //或 .setEntity(obj)
  .insert();

//简易.更新示例2
db.table("test")
  .setMap(map) //或 .setEntity(obj)
  .where("id IN (?...)", new int[] { 15,14,16}) //数据参数
  .update();
  
//简易.查询过程调用示例，及使用使用示例
db.call("select * from user where user_id=@userID")
  .setMap(map) //或 .setEntity(obj)
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(UserInfoModel.class);  //.getMap()
```

示例1.2::事务控制<br/>
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

//demo2.1:: //事务队列简化写法
new DbTranQueue().execute(qt->{
     for (order_add_sync_stone sp : processList) {
         sp.tran(qt).execute();
     }
 });
```
示例1.3::缓存控制<br/>
```java
/*
 * 内置了 EmptyCache（空缓存）、LocalCache（本地缓存）、SecondCache（二级缓存） 
 * 可以自己对memcached，redis 等进行包装后使用
 */
ICacheService cache = new EmptyCache();// 

//最简单的缓存控制
db.call("user_get").set("xxx", 1)
    .caching(cache)
    .usingCache(60 * 1000)
    .getItem(new UserInfoModel()); //.getMap()
    
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
    .getItem(new UserInfoModel()); //.getMap()

CacheTags tags = new CacheTags(cache);
//2.1.可根据标签清除缓存
tags.clear("user_" + 1);

//2.2.可根据标签更新缓存
tags.update<UserInfoModel>("user_" + 1, (m)=>{
    m.name = "xxx";
    return m;
});
```

示例2::数据模型类（或叫实体类等）<br/>
```java
//方案1：基于IBinder接口，精细控制
public class UserInfoModel implements IBinder {
    public long user_id;
    public int role;
    public String mobile;
    public String udid;
    public int city_id;
    public String name;
    public String icon;


    public void bind(GetHandlerEx s) {
        user_id = s.get("user_id").value(0l); //.value(x) 直接强类型转换，提供更高的性能
        role    = s.get("role").value(0);
        mobile  = s.get("mobile").value("");
        udid    = s.get("udid").value("");
        city_id = s.get("city_id").intValue(0);//.xxxValue(x) 根据类型判断后再转换，兼容性更好（特殊情况下用）
        name    = s.get("name").value("");
        icon    = s.get("icon").value("");

    }

    public IBinder clone() {
        return new UserInfoModel();
    }
}
//方案2：最简化
public class UserInfoModel {
    public long user_id;
    public int role;
    public String mobile;
    public String udid;
    public int city_id;
    public String name;
    public String icon;
}
```

示例3.1::[存储过程]映射类<br/>
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

//使用示例
user_get_by_id sp = new user_get_by_id();
sp.user_id = 1;
sp.caching(cache)
  .getItem(new UserInfoModel()); //.getMap()
```

示例3.2::[查询过程]映身类<br/>
```java
public class user_get_by_id extends DbQueryProcedure
{
    public user_get_by_id()
    {
        super(Config.user);
        sql("SELECT * FROM `user` where user_id = @user_id and type in (@types);");

        //set("{colname}", ()->{popname});
        //
        set("@user_id", ()->user_id);
        set("@types",()->types);
    }

    public long user_id;
    public List<Integer> types;
}

//使用示例
user_get_by_id sp = new user_get_by_id();
sp.user_id = 1;
sp.caching(cache)
  .getItem(new UserInfoModel()); 
```

示例3.3::[数据表]映射类<br/>
```java
public class UserM extends DbTable {
    public UserM() {
        super(DbConfig.test);

        table("users u");
        set("UserID", () -> UserID);
        set("Nickname", () -> Nickname);
        set("Sex", () -> Sex);
        set("Icon", () -> Icon);
        set("City", () -> City);
    }

    public Long UserID;
    public String Nickname;
    public Integer Sex;
    public String Icon;
    public String City;
}

//使用示例1
UserM m = new UserM();
m.UserID=1;
m.Icon="http:///xxxx";
m.insert();

//使用示例2
UserM m = new UserM();
m.icon="http:///xxxx";
m.where("UserID=?",1).update();

//使用示例3
UserM m = new UserM();
m.where("sex=?",1)
 .select("*")
 .getList(new UserInfoModel()); 

```

示例4::全局控制<br/>
```java
//开始debug模式，会有更多类型检查
WeedConfig.isDebug = true; 

//执行前检查代码 //不充许select 代码没有 limit 限制
WeedConfig.onExecuteBef((cmd)->{
    String sqltmp = cmd.text.toLowerCase();
    if(sqltmp.indexOf("select ")>=0 && sqltmp.indexOf(" limit ")< 0&&sqltmp.indexOf("count")<0) {
        return false;
    }else{
        return true;
    }
});

//执行后打印代码
WeedConfig.onExecuteAft((cmd) -> {
    System.out.println(cmd.text); //执行后打印日志
});

//对执行进行日志处理
WeedConfig.onLog((cmd) -> {
    //....
});
db.table("user").set("sex",1).log(true).update(); //.log(true) 执行后进行onLog日志处理

```

更多高级示例请参考Weed3Demo <br/>
--------------------------------------<br/>

by noear
