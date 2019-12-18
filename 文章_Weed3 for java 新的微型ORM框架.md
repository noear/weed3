# Weed3 for java 新的微型ORM框架

微型ORM（支持：java sql，xml sql，annotation sql；存储过程；事务；缓存；监听；等...）


05年时开发了第一代；

08年时开发了第二代；

14年时开发了第三代。因为不喜欢滥用反射，不喜欢有很多配置，所以一直在执着的没放弃。



前两代，都是在.net开发的；第三代，重点放在了java上。这应该算是个微型的ORM框架，因为只有0.1mb且无其它依赖。对外的接口也不多，主要由DbContext上的四个接口发起所有的操作。



因为一些执念本人写的东西都算是微型的：

* Snack3（Json框架 70kb，有序列化，有Jsonpath，有格式转换机制）
* Solon（Web框架 80kb）
* 一个浏览器（0.1mb，可是有完整功能哦）



#### Weed3 特点和理念：
高性能：两年前有个同事测过四个ORM框架，它是性能最好的（不知道现在是不是）。

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
    <version>3.2.4</version>
</dependency>
```



### 一、 上下文对象 DbContext

##### 所有weed3的操作，都是基于DbContext上的接口的操作。即，一切从实例化DbContext开始：

* 1.使用`application.yml`配置数据源（或别的格式配置，或配置服务），格式示例：

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
如果是 solon 框架，可以通过注解 或 接口获取配置

```java
//使用Properties配置的示例
Properties properties = XApp.cfg().getProp("demodb"); //这是solon框架的接口
DbContext db  = new DbContext(properties); 

//使用Map配置的示例
DbContext db  = new DbContext(map); 

//使用proxool线程池配置的示例（好像现在不流行了）//proxool通过xml配置
DbContext db  = new DbContext("user","proxool.xxx_db"); 

//使用DataSource配置的示例（一般使用连接池框架时用；推荐 Hikari 连接池）
//下行demo里用的正是 Hikari 连接池
DataSource dataSource = new HikariDataSource(...);
DbContext db  = new DbContext("user", dataSource); 

//还有就是用url,username,password（这个就不需要配置了）
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");

/* 我平时都用配置服务，所以直接由配置提供数据库上下文对象。 */
//使用配置服务直接拿到DbContext
DbContext db = WaterClient.Config.get("demodb").getDb();
```

### 二、四大接口 db.mapper(), db.table(), db.call(), db.sql()

> 四大接口，也即是DbContext在不同场景上的应用方式
>
> 核心接口：db.mapper(), db.table()；代表两种完全不同的风格和口味。
>
> 补充接口：db.call(), db.sql()。

##### （一）db.mapper()，提供mapper操作支持

 > mapper风格，是现在极为流行的一种。大多人都在用。

* 1.db.mapperBase(clz) 获取BaseMapper实例

  > 自Xxx-plus之后，要是个没有BaseMapper，好像都不好意思说自己是个ORM框架了。
  >
  > 这个接口确实带来了极大的方法，简单的CRUD完全省掉了。
```java
//直接使用BaseMapper
BaseMapper<User> userDao= db.mapperBase(User.class);

//增
userDao.insert(user,false);

//删
userDao.deleteById(12);

//改：通过ID改
userDao.updateById(user,false);
//改：通过条件改
userDao.update(user,false,m->m.whereEq(User::getType,12).andEq(User::getSex,1));

//查.通过ID查
User user = userDao.selectById(12);
//查.通过条件查
User user = userDao.selectItem(m -> m.whereEq(User::getId,12));
```
* 2.db.mapper(clz)，获取Mapper实例
```java
@Namespace("demo.dso.db")
public interface UserDao { //此接口，可以扩展自 BaseMapper<T>
    @sql("select * from `user` where id=@{id}") //变量风格
    User getUserById(int id);
  
  	@sql("select * from `user` where id=?") //占位符风格
    User getUserById2(int id);
  
    long addUser(User m); //没有注解，需编写xml sql
}

UserDao userDao = db.mapper(UserDao.class);

User user = userDao.getUserById(12);
userDao.addUser(user);
```
* 3.db.mapper(xsqlid, args)，获取Xml sql mapper结果
```java
Map<String,Object> args = new HashMap<>();
args.put("id",22);

//xsqlid = @{sqlid} = @{namespace}.{id}
User user = db.mapper("@demo.dso.db.getUserById",args);
```

##### （二）db.table()，提供纯java链式操作

  > 这是Weed3最初的样子，这也是我最喜难的方法。
  >
  > BaseMapper内部也是由db.table()实现的，简单几行代就OK了。
  >
  > 灵活，有弹性，直接，可以实现任何SQL代码效果。开发管理后台，很爽（因为查询条件又杂又乱）。
  >
  > 不过有不少人认为这不是ORM。大家口味不同：）

  `db.table() 支持 字符串风格 和 lambda 风格。`

* 增,INSEERT
```java
User user = new User();
..
//单条插入
db.table("user").set("name","xxx").insert();
db.table("user").setEntity(user).insert();
db.table("user").setEntityIf(user, (k,v)->v!=null).insert(); //过滤null

//批量插入
db.table("user").insertList(list);
```

* 删,DELETE
```java
//删掉id<12的记录
db.table("user").whereLt("id",12).delete();

//删掉id=12的记录 (lamdba风格)
db.table(User.class).whereEq(User::getId,12).delete(); 
```

* 改,UPDATE
```java
//改掉id=23的sex字段
db.table("user").set("sex",1).whereEq("id",23).update();

//根据手机号，新增或更新
public void saveUser(UserModel m){
  db.talbe("user").setEntityIf(m, (k,v)->v!=null).upsert("mobile");
}
```

* 查,SELECT
```java
//统计id<10的记录数
db.table("user").where("id<?", 10).count();

//检查是否存在id<10的记录
db.table("user").whereLt("id", 10).exists();

//关联查询并输出一个实体(lamdba风格) //还是字符串风格更有弹性和简洁
db.table(User.class)
  .innerJoin(UserEx.class).onEq(User::getId,UserEx::getUserId)
  .where(User::getId, 10).andEq(UserEx::getSex,1)
  .limit(1)
  .select(User.class,$(UserEx::getSex).alias("user_sex"))
  .getItem(User.class);

```

##### （三）db.call()，提供call操作
* call数据库存储过程
```java
//数据库存储过程使用
//
User user = db.call("user_get").set("id",1).getItem(User.class);
```

* call查询过程
```java
//查询储过程使用 
//@sql 和 Xml sql的内部由db.call()实现
//
User user = db.call("select * from user where id=@{id}").set("id",1).getItem(User.class);
```

* call Xmlsql
```java
//Xml sql的另一种使用方式 //需@开始
//
User user = db.call("@demo.dso.db.getUser").set("id",1).getItem(User.class);
```

##### （四）db.sql()，提供手写sql操作
```java
//所以接口最终都会转为db.sql()，算是最底层的一个接口
//
User user = db.sql("select * from user where id=?",12).getItem(User.class);

Long total = db.sql("select count(*) from user").getValue(0l);

//db.sql() 的快捷版: db.exe()，用于快速批处理
//
db.exe("delete from user where id=12");
db.exe("update user sex=1 where id=12");
```

### 三、Mapper 语法

##### （一）BaseMapper 接口

* Long insert(T entity, boolean excludeNull);
* void insertList(List<T> list);

* Integer deleteById(Object id);
* Integer deleteByIds(Iterable<Object> idList);
* Integer deleteByMap(Map<String, Object> columnMap);
* Integer delete(Act1<WhereQ> condition);

* Integer updateById(T entity, boolean excludeNull);
* Integer update(T entity, boolean excludeNull, Act1<WhereQ> condition);

* Long upsert(T entity, boolean excludeNull);
* Long upsertBy(T entity, boolean excludeNull, String conditionFields);

* boolean existsById(Object id);
* boolean exists(Act1<WhereQ> condition);

* T selectById(Object id);
* List<T> selectByIds(Iterable<Object> idList);
* List<T> selectByMap(Map<String, Object> columnMap);

* T selectItem(T entity);
* T selectItem(Act1<WhereQ> condition);
* Map<String, Object> selectMap(Act1<WhereQ> condition);

* Object selectValue(String column, Act1<WhereQ> condition);

* Long selectCount(Act1<WhereQ> condition);

* List<T> selectList(Act1<WhereQ> condition);
* List<Map<String, Object>> selectMapList(Act1<WhereQ> condition);
* List<Object> selectArray(String column, Act1<WhereQ> condition);

* List<T> selectPage(int start, int end, Act1<WhereQ> condition);
* List<Map<String, Object>> selectMapPage(int start, int end, Act1<WhereQ> condition);


##### （二）annotation sql
* 示例
```java
ICacheServiceEx cache = new LocalCache().nameSet("cache");

//顺带加了缓存
@Sql(value="select * from user where id=@{id}", caching="cache")
public UserModel getUser(int id);
```

* Sql 注解说明
```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {
    String value() default "";      //代码
    String caching() default "";    //缓存服务名称
    String cacheClear() default ""; //缓存清除标签
    String cacheTag() default "";   //缓存标签
    int usingCache() default 0;     //缓存时间
}
```
##### （三）Xml sql

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

### 四、Table 语法

##### （一）条件操作（与Mapper共用）

| 方法 | 效果说明 |
| --- | --- |
| where,whereIf |  |
| whereEq,whereNeq | ==, != |
| whereLt,whereLte | \<, \<= |
| whereGt,whereGte | \>, \>= |
| whereLk,whereNlk | LIKE, NOT LIKE |
| whereIn,whereNin | IN(..), NOT IN(..) |
| whereBtw,whereNbtw | BETWEEN, NOT BETWEEN |
| and系统方法 | 同where |
| or系统方法 | 同where |
| begin | \( |
| end | \) |

##### （二）表操作（Table独占）
| 方法 | 效果说明 |
| --- | --- |
| table | 主表 |
| innerJoin,leftJoin,rightJoin | 关联表 |
| on,onEq | 关联条件 |
| orderBy,orderByAsc,orderByDesc | 排序 |
| groupBy | 组 |
| having | 组条件 |
| limit | 限制范围 |
| select | 查询（返回IQuery） |
| update | 更新 |
| insert | 插入 |
| delete | 删除 |

### 五、 缓存和事务
* 缓存
```java
ICacheServiceEx cache = new LocalCache().nameSet("cache");

User user = db.table("user")
              .where("id=?",12)
              .caching(cache)
              .select("*").getItem(User.class);
```

* 缓存控制
```java
//查询时，缓存
User user = db.table("user")
              .where("id>?",12)
              .limit(100,20)
              .caching(cache).usingCache(60*5).cacheTag("user_all") //缓存5分钟，加标签user_
              .select("*").getList(User.class);

//更新时，清除缓存 //下次查询时，又可拿到最新数据
db.table("user").set("sex",0).where("id=101").update();
cache.clear("user_all");
```

* 单库数据库事务
```java
db.tran(t->{
  //注册用户
  long user_id = userDao.addUser(user);
  
  //注册后送10个金币（在同一个事务里完成）
  userDao.addUserGold(user_id, 10);
});
```

* 跨库数据库事务
```java
DbTranQueue.run(qt->{
  //订单库，添加记录
  db1.tran(qt, t-> orderDao.addOrder(order));
});
```

### (六) 监听

