[![Maven Central](https://img.shields.io/maven-central/v/org.noear/weed3.svg)](https://mvnrepository.com/search?q=weed3)

` QQ交流群：22200020 `

# Weed for java
微型ORM（支持：java sql，xml sql，annotation sql；事务；缓存；等...）


#### Weed3 特点和理念：
高性能：两年前有个同事测过四个ORM框架，它是性能最好的（不知道现在是不是）。
跨平台：可以嵌入到JVM脚本引擎（js, groovy, lua, python, ruby）；有.net，php版本（久没维护了）。
很小巧：0.1Mb（且是功能完整，方案丰富；可极大简化数据库开发）。
有个性：不喜欢反射、不喜欢配置...（除了连接，不需要任何配置）。
其它的：支持缓存控制和跨数据库事务（算是分布式事务的一种吧）。


#### 相关文章：
* [一个新的微型ORM框架](https://my.oschina.net/noear/blog/3144349)


#### 组件： 
| 组件 | 说明 |
| --- | --- |
| org.noear:weed3-parent | 框架版本管理 |
| org.noear:weed3 | 主框架 |
| org.noear:weed3-maven-plugin| Maven插件，用于生成Xml sql mapper |
| | |
| org.noear:weed3.cache.memcached| 基于 Memcached 封装的扩展缓存服务 |
| org.noear:weed3.cache.redis| 基于 Redis 封装的扩展缓存服务 |
| org.noear:weed3.cache.ehcache| 基于 ehcache 封装的扩展缓存服务 |
| org.noear:weed3.cache.j2cache| 基于 j2cache 封装的扩展缓存服务 |

#### 核心功能
* 上下文：DbContext db
* 四个接口：db.mapper(), db.table(), db.call(), db.sql()


#### Meven配置：

```xml
<!-- 框架包 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.2.4.1</version>
</dependency>

<!-- maven 插件，用于生成Xml sql mapper接口 -->
<plugin>
    <groupId>org.noear</groupId>
    <artifactId>weed3-maven-plugin</artifactId>
    <version>3.2.4.1</version>
</plugin>
```



#### 实例化数据库上下文
```java
//DbContext db  = new DbContext(properties); //使用Properties配置的示例
//DbContext db  = new DbContext(map); //使用Map配置的示例
//DbContext db  = new DbContext("user","proxool.xxx_db"); //使用proxool线程池配置的示例
//DbContext db  = new DbContext("user",new HikariDataSource(...)); //使用DataSource配置的示例
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");
```

### 一、Table用法
示例1.1.1::入门级
```java
//快速.执行示例
db.exec("DELETE FROM user_info WHERE user_id<?",10);

//简易.查询示例
db.table("user_info").where("user_id<?", 10).count();

db.table("user_info").where("user_id<?", 10).exists();
  
db.table("user_info") 
  .where("user_id<?", 10)
  .select("user_id,name,sex")
  .getMapList(); //.getDataList() //.getList(new UserModel()) //.getList(UserModel.class)

//简易.关联查询示例
db.table("user_info u")
  .innerJoin("user_ex e").on("u.useer_id = e.user_id")
  .where("u.user_id<?", 10)
  .limit(1)
  .select("u.user_id,u.name,u.sex")
  .getMap(); //.getDataItem() //.getItem(new UserModel()) //.getItem(UserModel.class)
                
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
  .getItem(UserModel.class);  //基于反射

//简易.查询过程调用示例，及使用使用示例
db.call("select * from user where user_id=@{userID}") //@{userID},为变量占位符
  .set("userID", 1) 
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(new UserModel());  //要求：UserModel 为 IBinder

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
  .where("1=1")
  .andIf(1 == 2, "mobile=?", "xxxx")
  .andIf(1 != 2, "icon=?", "xxxx")
  .select("*");
  
//连式处理::对不确定字段的插入
db.table("test")
  .set("name", "xxx")
  .setIf(1==2,"mobile", "xxxx")
  .setIf(1!=2,"icon", "xxxx")
  .insert(); 
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
db.call("select * from user where user_id=@{userID}")
  .setMap(map) //或 .setEntity(obj)
  .caching(cache)//使用缓存
  .usingCache(60 * 100) //缓存时间
  .getItem(UserModel.class);  //.getMap()
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
    .getItem(new UserModel()); //.getMap()
    
//根据查询结果控制缓存
db.call("user_get").set("xxx",1)
    .caching(cache)
    .usingCache(60 * 100)
    .getItem(new UserModel(), (cu, t) => { 
        if (t.user_id == 0)
            cu.usingCache(false);
});

//通过tag维护缓存
//1.缓存并添加简易标签
db.call("user_get").set("xxx", 1)
    .caching(cache)
    .cacheTag("user_"+ 1)
    .usingCache(60 * 1000)
    .getItem(new UserModel()); //.getMap()

//2.1.可根据标签清除缓存
cache.clear("user_" + 1);

//2.2.可根据标签更新缓存
cache.update<UserModel>("user_" + 1, (m)=>{
    m.name = "xxx";
    return m;
});
```

示例2::数据模型类（或叫实体类等）<br/>
```java
@Table("user")
public class UserModel {
    public long user_id;
    public int role;
    public String mobile;
    public String udid;
    public int city_id;
    public String name;
    public String icon;
}
```

### 二、Mapper用法
#### （一）BaseMapper
```java
BaseMapper<UserModel> userDao = db.mapperBase(UserModel.class);

UserModel user = userDao.selectById(12);
```
#### （二）注解
```java
public interface UserDao {
    @Sql("select * from user where id=@{id} limit 1")
    UserModel appx_get(int id) throws Exception;
  
    @Sql("select * from user where id=? limit 1")
    UserModel appx_get(int id) throws Exception;  
}

//使用
var UserDao api = db.mapper(UserDao.class);
int app_id = api.appx_get();
```

#### （三）Xml sql
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





by noear
