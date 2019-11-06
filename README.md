` QQ交流群：22200020 `

# Weed for java
微型ORM（支持：java sql，xml sql，注解 sql；事务；缓存）<br/>

支持什么数据库？？？<br/>
与具体数据库无关（或许支持所有数据库）<br/>

理念：<br/>
高性能、跨平台、轻量、有个性；不喜欢反射、不喜欢配置...<br/>

特点：<br/>
1.零反射零注解（后来加了一点点反射的的可选功能...）<br/>
2.漂亮的缓存控制（缓存服务由外部提供）<br/>
3.纯代码无任何配置<br/>
4.分布式事务集成<br/>
5.万能的数据绑定<br/>

占位符说明：<br/>
 $.       //数据库名占位数<br/>
 $fun     //SQL函数占位符<br/>
 ?        //参数占位符<br/>
 ?...     //数组型参数占位符<br/>
 @{.}     //参数变量占位符<br/>
 ${.}     //替换变量占位符<br/>

相关文章：
* [一个新的微型ORM开源框架](https://www.jianshu.com/p/0311afb5cd60)
 
引用 Maven： 
```xml
<!-- 主框架 -->
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3</artifactId>
  <version>3.2.2.1</version>
</dependency>

<!-- meven 插件，用于生成xml sql mapper -->
<plugin>
    <groupId>org.noear</groupId>
    <artifactId>weed3-maven-plugin</artifactId>
    <version>3.2.2</version>
</plugin>
```

实例化数据库上下文（一切都在上面操作）
```java
//DbContext db  = new DbContext(properties); //使用Properties配置的示例
//DbContext db  = new DbContext(map); //使用Map配置的示例
//DbContext db  = new DbContext("user","proxool.xxx_db"); //使用proxool线程池配置的示例
//DbContext db  = new DbContext("user",new HikariDataSource(...)); //使用DataSource配置的示例
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");
```

##### 缓存服务支持
###### 1.内置缓存服务
* org.noear.weed.cache.EmptyCache // 空缓存
* org.noear.weed.cache.LocalCache // 轻量级本地缓存（基于Map实现）
* org.noear.weed.cache.SecondCache // 二级缓存（组装两个 ICacheServiceEx 实现）
###### 2.扩展缓存服务
* org.noear.weed.cache.ehcache.EhCache // 基于ehcache封装
```xml
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3.cache.ehcache</artifactId>
  <version>3.2.1.1</version>
</dependency>
```
* org.noear.weed.cache.j2cache.J2Cache // 基于国人开发的J2Cache封装
```xml
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3.cache.j2cache</artifactId>
  <version>3.2.1.1</version>
</dependency>
```
* org.noear.weed.cache.memcached.MemCache // 基于memcached封装
```xml
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3.cache.memcached</artifactId>
  <version>3.2.1.3</version>
</dependency>
```
* org.noear.weed.cache.redis.RedisCache // 基于redis封装
```xml
<dependency>
  <groupId>org.noear</groupId>
  <artifactId>weed3.cache.redis</artifactId>
  <version>3.2.1.1</version>
</dependency>
```
* 也可以自己封装个 ICacheServiceEx ...


 ### 一、纯java用法
示例1.1.1::入门级<br/>
```java
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
  .getItem(UserInfoModel.class);  //基于反射

//简易.查询过程调用示例，及使用使用示例
db.call("select * from user where user_id=@userID") //@userID,参数占位符
  .set("@userID", 1) 
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserInfoModel());  //要求：UserInfoModel 为 IBinder

//简易.存储过程调用示例，及使用事务示例
db.tran(tran->{
    db.call("user_set").set("xxx", 1) 
      .execute(); //将在事务内执行
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

示例1.2::缓存控制<br/>
```java
/*
 * 内置了 EmptyCache（空缓存）、LocalCache（本地缓存）、SecondCache（二级缓存） 
 * 可以自己对memcached，redis 等进行包装后使用
 */
ICacheServiceEx cache = new EmptyCache();// 

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

//2.1.可根据标签清除缓存
cache.clear("user_" + 1);

//2.2.可根据标签更新缓存
cache.update<UserInfoModel>("user_" + 1, (m)=>{
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

示例3.1::[存储过程]映射类（存储过程实体化）<br/>
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

示例3.2::[查询过程]映身类（查询过程实体化）<br/>
```java
public class user_get_by_id extends DbQueryProcedure
{
    public user_get_by_id()
    {
        super(Config.user);
        sql("SELECT * FROM `user` where user_id = @{user_id} and type in (@{types});");

        //set("{colname}", ()->{popname});
        //
        set("user_id", ()->user_id);
        set("types",()->types);
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

### 二、sql 注解用法
```java
public interface DbAppApi {
    @Sql("select app_id from appx limit 1")
    int appx_get() throws Exception;
}

//使用
var DbAppApi api = db.mapper(DbAppApi.class);
int app_id = api.appx_get();
```

### 三、xml mapper用法
示例::xml配置（~/resources/weed3/DbUserMapper.xml）<br/>
```xml
<?xml version="1.0" encoding="utf-8" ?>
<mapper namespace="sql.weed.test" :db="userdb">
    <sql id="user_add" :return="long">
        INSERT user(user_id) VALUES(@{user_id:int})
    </sql> 
</mapper>
```
示例::java调用（直接调用即可，其它不用干）<br/>
```java
//使用方案1（已支持）
db.call("@sql.weed.test.user_add").set("user_id",12).insert();

//使用方案2
// DbUserMapper.java 生成代码（类的名字与xml文件名一致） //文件可以移到别处去
package sql.weed.test;

@Namespace("sql.weed.test")
public interface DbUserMapper{
    long user_add(int user_id);
}

DbUserMapper um = db.mapper(DbUserMapper.class); //获取个单例
um.user_add(12);

```

### 四、事务控制

```java
//demo1:: //事务组
DbUserMapper um = XmlSqlProxy.getSingleton(DbUserMapper.class);

db.tran((t) => {
    //以下操作，会在 t 事务内执行（下面的操作，汇集了weed3所有的接口模式）
    db.table("test").set("txt", "cc").insert();
    db.sql("update test set txt='1' where id=1").execute();
    db.call("user_add_sp").set("_txt","bb").insert();
    um.user_add(12);
});

//demo2:: //事务队列
DbTranQueue queue = new DbTranQueue();

db.tran().join(queue).execute((t) => {
    //以下操作，会在 t 事务内执行
    db.sql("insert into test(txt) values(?)", "cc").execute();
    db.sql("insert into test(txt) values(?)", "dd").execute();
    db.sql("insert into test(txt) values(?)", "ee").execute();
});

db2.tran().join(queue).execute((t2) => {
    //以下操作，会在 t2 事务内执行
    db2.sql("insert into test(txt) values(?)", "gg").execute();
});

//统一提交执行（失败，则统一回滚）
queue.complete();

//demo2.1:: //事务队列简化写法
new DbTranQueue().execute(qt->{
     for (order_add_sync_stone sp : processList) {
         sp.tran(qt).execute();
     }
 });
 ```

### 五、全局控制和执行监听
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
