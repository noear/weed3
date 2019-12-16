# Weed3 for java 新的微型ORM框架

微型ORM（支持：java sql，xml sql，annotation sql；事务；缓存；监听；等...）

##### 理念：
高性能、跨平台、轻量、有个性；不喜欢反射、不喜欢配置...

##### 特点：
1.一个上下文和四个接口适用各种场景
2.可以零反射可以零注解
3.纯代码无多余配置
4.缓存控制支持
5.跨库事务支持

### (一) 一个上下文 DbContext
* 所有weed3的操作，都是基于DbContext上的操控
1. 需要有配置，可以在`application.properties`获取，可以通过配置服务获取，可以临时手写一下。。
> 如果是 Spring 框架，可以通过注解获取配置
如果是 solon 框架，可以通过注解 或 Aop.prop().get("xxx")获取配置

2.有配置之后开始实列化DbContext。这里临时手写一下。
```java
//使用Properties配置的示例
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

### (二) 四大接口 mapper(),table(),call(),sql()
##### db.mapper()，提供mapper操作支持
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

##### db.table()，提供纯java链式操作
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

##### db.call()，提供call操作
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

##### db.sql()，提供手写sql操作
```java
User user = db.sql("select * from user where id=?",12).getItem(User.class);

Long total = db.sql("select count(*) from user").getValue(0l);

//db.sql() 的快捷版: db.exe()
//
db.exe("delete from user where id=12");
db.exe("update user sex=1 where id=12");
```

### (三) Xml sql 语法
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
### (四) 缓存和事务
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