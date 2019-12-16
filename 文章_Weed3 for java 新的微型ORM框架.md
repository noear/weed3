# Weed3 for java 新的微型ORM框架

微型ORM（支持：java sql，xml sql，annotation sql；存储过程；事务；缓存；监听；等...）

05年时开发了第一代；08年时开发了第二代；14年时开发了第三代。因为不喜欢反射，不喜欢有很多配置，所以一直在执着的没放弃。

前两代，都是在.net开发的；第三代，重点放在了java上。这应该算是个微型的ORM框架，因为只有0.1mb嘛。对外的接口上也不多，由DbContext上的四个接口发起所有的操作。



因为一些执念本人写的东西都算是微型的：

* Snack3（Json框架 70kb，有序列化，有Jsonpath，有格式转换机制）
* Solon（Web框架 70kb）
* 一个浏览器（0.1mb，可是有完整功能哦）



#### Weed3 特点和理念：
高性能：前年有个同事测过四个ORM框架，它是性能最好的。

跨平台：可以嵌入到JVM脚本；有.net版本，php版本（有些时间没维护了）。

很小巧：只有0.1Mb嘛

有个性：不喜欢反射、不喜欢配置...（除了连接，不需要任何配置）

其它的：支持缓存控制和跨数据库事务。



#### Weed3 组件： 
| 组件 | 说明 |
| --- | --- |
| org.noear:weed3-parent | 框架版本管理 |
| org.noear:weed3 | 主框架（没有任何依赖） |
| org.noear:weed3-maven-plugin| Maven插件，用于生成Xml sql mapper |
| | |
| org.noear:weed3.cache.memcached| 基于 Memcached 封装的扩展缓存服务 |
| org.noear:weed3.cache.redis| 基于 Redis 封装的扩展缓存服务 |
| org.noear:weed3.cache.ehcache| 基于 ehcache 封装的扩展缓存服务 |
| org.noear:weed3.cache.j2cache| 基于 j2cache 封装的扩展缓存服务 |



#### Weed3 meven配置： 

```xml 
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.2.3.11</version>
</dependency>
```



### 一、 上下文对象 DbContext

##### 所有weed3的操作，都是基于DbContext上的接口。实例化DbContext，很简单：

* 1.使用`application.yml`配置（或别的格式配置，或配置服务），格式示例：

```ini
demodb:
    schema: demo
    url: jdbc:mysql://localdb:3306/demo?...
    driverClassName: com.mysql.cj.jdbc.Driver
    username: demo
    password: UL0hHlg0Ybq60xyb
```


* 2.有配置之后开始实列化DbContext：

> 如果是 Spring 框架，可以通过注解获取配置
如果是 solon 框架，可以通过注解 或 XApp.cfg().getProp("demodb")获取配置

```java
//使用Properties配置的示例
Properties properties = XApp.cfg().getProp("demodb"); //这是solon框架的接口
DbContext db  = new DbContext(properties); 

//使用Map配置的示例
DbContext db  = new DbContext(map); 

//使用proxool线程池配置的示例（好像现在不流行了）
DbContext db  = new DbContext("user","proxool.xxx_db"); 

//使用DataSource配置的示例（一般使用连接池框架时用；推荐 Hikari 连接池）
//下行demo里用的正是 Hikari 连接池
DbContext db  = new DbContext("user",new HikariDataSource(...)); 

//还有就是用url,username,password
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");

/* 我平时都用配置服务，所以直接由配置提供数据库上下文对象。 */
```

### 二、四大接口 db.mapper(), db.table(), db.call(), db.sql()
##### （一）db.mapper()，提供mapper操作支持
* 1.db.mapperBase(clz) 获取BaseMapper实例
```java
BaseMapper<User> userDao= db.mapperBase(User.class);
User user = userDao.selectById(12);
userDao.insert(user,false);
```
* 2.db.mapper(clz)，获取Mapper实例
```java
@Namespace("demo.dso.db")
public interface UserDao{ //此接口，可以扩展自 BaseMapper<T>
    @sql("select * from `user` where id=@{id}")
    User getUserById(int id);
  
    long addUser(User m); //没@sql的，需编写xml
}

UserDao userDao = db.mapper(UserDao.class);
User user = userDao.getUserById(12);
userDao.addUser(user);
```
* 3.db.mapper(xsqlid,args)，获取Xml sql mapper结果
```java
Map<String,Object> args = new HashMap<>();
args.put("id",22);
User user = db.mapper("@demo.dso.db.getUserById",args);
```

##### （二）db.table()，提供纯java链式操作
* 增
```java
User user = new User();
..
//单条插入
db.table("user").setEntity(user).insert();

//批量插入
db.table("user").insertList(list);
```

* 删
```java
//删掉id<12的记录
db.table("user").whereLt("id",12).delete();
```

* 改
```java
//改掉id=23的sex字段
db.table("user").set("sex",1).whereEq("id",23).update();
```

* 查
```java
//统计id<10的记录数
db.table("user").where("id<?", 10).count();

//检查是否存在id<10的记录
db.table("user").where("user_id<?", 10).exists();

//关联查询并输出一个实体
db.table("user u")
  .innerJoin("user_ex e").on("u.id = e.user_id")
  .where("u.id<?", 10).andEq("u.sex",1)
  .limit(1)
  .select("u.user_id,u.name,u.sex")
  .getItem(User.class);

```

##### （三）db.call()，提供call操作
* call数据库存储过程
```java
//数据库存储过程使用
User user = db.call("user_get").set("id",1).getItem(User.class);
```

* call查询过程
```java
//查询储过程使用 //与@sql很像 //可以将sql执行化后拿出来用
User user = db.call("select * from user where id=@{id}").set("id",1).getItem(User.class);
```

* call Xmlsql
```java
//Xmlsql非Mapper使用
User user = db.call("@demo.dso.db.getUser").set("id",1).getItem(User.class);
```

##### （四）db.sql()，提供手写sql操作
```java
User user = db.sql("select * from user where id=?",12).getItem(User.class);

Long total = db.sql("select count(*) from user").getValue(0l);

//db.sql() 的快捷版: db.exe()
//
db.exe("delete from user where id=12");
db.exe("update user sex=1 where id=12");
```

### 三、Xml sql 语法
* 示例
```xml
<?xml version="1.0" encoding="utf-8" ?>
<mapper namespace="weed3demo.xmlsql2">
    <sql id="getUser" :return="demo.model.UserModel" :note="获取用户信息">
        SELECT * FROM user WHERE id = @{id:int}
    </sql>
</mapper>
```

* 语法
```
mapper 开始标签
  namespace （属性：命名空间，{namespace}.{id} = sqlid）
    
sql 代码块定义指令
  id （属性：id）
  :require（属性：导入包或类）
  :param?（属性：外部输入变量申明；默认会自动生成::新增***）
  :declare（属性：内部变量类型预申明）
  :return（属性：返回类型）

  :note（属性：描述、说明、注解）

  :caching（属性：缓存服务name） //是对 ICacheController 接口的映射
  :cacheClear?（属性：清除缓存）
  :cacheTag?（属性：缓存标签，支持在入参或结果里取值替换）
  :usingCache?（属性：缓存时间,int）

if 判断控制指令（没有else）
  test （属性：判断检测代码）
     //xml避免语法增强:
     //lt(<) lte(<=) gt(>) gte(>=) and(&&) or(||)
        //例：m.sex gt 12 :: m.sex >=12
     //简化语法增强:
     //??(非null,var!=null) ?!(非空字符串,StringUtils.isEmpty(var)==false)
        //例：m.icon??  ::m.icon!=null
        //例：m.icon?!  ::StringUtils.isEmpty(m.icon)==false

for 循环控制指令 （通过 ${var}_index 可获得序号，例：m_index::新增***）
  var （属性：循环变量申明）
  items （属性：集合变量名称）
  sep? （属性：分隔符::新增***）

trim 修剪指令
  trimStart（属性：开始位去除）
  trimEnd（属性：结尾位去除）
  prefix（属性：添加前缀）
  suffix（属性：添加后缀）

ref 引用代码块指令
  sql （属性：代码块id）

name:type    = 变量申明（可用于属性：:param, :declare，var，或宏定义 @{..},${..}）
@{name:type} = 变量注入
${name:type} = 变量替换

//列表([]替代<>)
:return="List[weed3demo.mapper.UserModel]" => List<UserModel>
:return="List[String]" => List<String> （Date,Long,...大写开头的单值类型）
:return="MapList" => List<Map<String,Object>>
:return="DataList" => DataList

//一行
:return="weed3demo.mapper.UserModel" => UserModel
:return="Map" => Map<String,Object>
:return="DataItem" => DataItem

//单值
:return="String" => String （任何单职类型）
```
### 四、 缓存和事务
* 缓存
```java
ICacheServiceEx cache = new LocalCache().nameSet("test")

User user = db.table("user")
              .where("id=?",12)
              .caching(cache)
              .select("*").getItem(User.class);
```

* 缓存控制
```java

```

* 事务
```java

```

* 跨库事务
```java

```

### (五) 监听