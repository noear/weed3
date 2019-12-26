[![Maven Central](https://img.shields.io/maven-central/v/org.noear/weed3.svg)](https://mvnrepository.com/search?q=weed3)

` QQ交流群：22200020 `

# Weed for java
微型ORM框架（支持：java sql，xml sql，annotation sql，template sql；事务；缓存；监控；等...）



#### Weed3 特点和理念：
* 高性能：两年前有个同事测过四个ORM框架，它是性能最好的（不知道现在是不是）。
* 跨平台：可以嵌入到JVM脚本引擎（js, groovy, lua, python, ruby）；有.net，php版本（久没维护了）。
* 很小巧：0.1Mb（且是功能完整，方案丰富；可极大简化数据库开发）。
* 有个性：不喜欢反射、不喜欢配置...（除了连接，不需要任何配置）。
* 其它的：支持缓存控制和跨数据库事务（算是分布式事务的一种吧）。



#### 核心对象和功能：

* 上下文：DbContext db
* 四个接口：db.mapper(), db.table(), db.call(), db.sql()

```java
//Mapper接口
db.mapperBase(User.class).selectById(1);

//Table接口
db.table("user u")
  .innerJoin("user_ext e").onEq("u.id","e.user_id")
  .whereEq("u.type",11)
  .limit(100,20)
  .select("u.*,e.sex,e.label")
  .getList(User.class);
```



#### 相关文章：

* [Weed3 for java 新的微型ORM框架](https://my.oschina.net/noear/blog/3144349) （推荐一看）
* [Weed3 ORM框架专题系列文章](https://www.jianshu.com/nb/40211584?order_by=seq)
* [Weed3 for solon DEMO](https://gitee.com/noear/solon_weed3_demo)（一个用户写的DEMO）




#### 组件列表： 

| 组件 | 说明 |
| --- | --- |
| org.noear:weed3 | 主框架（没有任何依赖） |


| 可选组件 | 说明 |
| --- | --- |
| org.noear:weed3-maven-plugin| Maven插件，用于生成Xml sql mapper |
| org.noear:weed3-solon-plugin | Solon插件，支持@Db注解、Mapper直接注入 |
| | |
| org.noear:weed3.cache.memcached| 基于 Memcached 适配的扩展缓存服务 |
| org.noear:weed3.cache.redis| 基于 Redis 适配的扩展缓存服务 |
| org.noear:weed3.cache.ehcache| 基于 ehcache 适配的扩展缓存服务 |
| org.noear:weed3.cache.j2cache| 基于 j2cache 适配的扩展缓存服务 |
| | |
| org.noear:weed3.render.beetl | 基于 beetl 适配的扩展模板引擎 |
| org.noear:weed3.render.enjoy | 基于 enjoy 适配的扩展模板引擎 |
| org.noear:weed3.render.freemarker | 基于 freemarker 适配的扩展模板引擎 |
| org.noear:weed3.render.velocity | 基于 velocity 适配的扩展模板引擎 |





#### Meven配置：

```xml
<!-- 框架包 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.2.6.3</version>
</dependency>

<!-- 可选：render 插件，用于支持模板接口（Xxx 换成具体的名字） -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3.render.Xxx</artifactId>
    <version>3.2.6.3</version>
</dependency>

<!-- 可选：maven 插件，用于生成Xml sql mapper接口 -->
<plugin>
    <groupId>org.noear</groupId>
    <artifactId>weed3-maven-plugin</artifactId>
    <version>3.2.6.3</version>
</plugin>
```



#### 入门示例：
```java
/** 1.实例化上下文 */
//DbContext db  = new DbContext(properties); //使用Properties配置的示例
//DbContext db  = new DbContext(map); //使用Map配置的示例
//DbContext db  = new DbContext("user","proxool.xxx_db"); //使用proxool线程池配置的示例
//DbContext db  = new DbContext("user",new HikariDataSource(...)); //使用DataSource配置的示例
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");


/** 2.1.Mapper用法 */
@Namespace("demo.dso.db")
public interface UserDao extends BaseMapper<UserModel>{
    @Sql("select * from user where id=@{id} limit 1")
    UserModel getUser(int id);
  
    @Sql("select * from user where id=? limit 1")
    UserModel getUser2(int id);

    @Sql("#report/user_stat.sql") //#开头表示执行模板SQL（应对超复杂统计查询）
    StatModel userStat(int date);

    void addUser(UserModel user); //没注解，需要配xml
}

UserDao userDao = db.mapper(UserDao.class);
//调用 BaseMapper 方法
userDao.selectById(12); 

//调用 @Sql 方法
UserModel user = userDao.getUser(2); 

//调用 Xml sql
userDao.addUser(user); 

//调用Template sql
StatModel stat = userDao.userStat(20201010);



/** 2.2.Table用法 */
//增::
db.table("user").setEntity(user).insert();
//删::
db.table("user").whereEq("id",2).delete();
//改::
db.table("user").set("sex",1).whereEq("id",2).delete();
//查::
db.table("user u")
  .innerJoin("user_ext e").onEq("u.id","e.user_id")
  .whereEq("u.id",1001)
  .select("u.*,e.sex,e.label")
  .getItem(User.class);



/** 2.3.Call用法 */
//调用存储过程
db.call("user_get_list_by").set("_type",12).getList(User.class);

//调用xml sql
db.call("@demo.dso.db.user_get").set("id",1001).getItem(User.class);

//调用Template sql
Map<String,Object> args = new DataItem().set("date",20201010).getMap();
db.call("#tml/user_stat.sql", args).getMapList();



/** 2.4.Sql用法 */
//快速执行SQL语句
db.sql("select * from user id=?",12).getItem(User.class);

```



#### 附：语法参考：

##### （一）Template sql 语法
* @{name} ：变量占位符，会由Weed3进一步转化为JDBC变量
* 支持四种引擎
* 具体参考：[《WEED3 模板语法》](WEED3_模板_语法.md)

##### （二）Xml sql 语法
* 示例
```xml
<?xml version="1.0" encoding="utf-8" ?>
<mapper namespace="weed3demo.xmlsql2"
        import="demo.model.*"
        :baseMapper="UserModel">
    <sql id="getUser" :return="UserModel" :remarks="获取用户信息">
        SELECT * FROM user WHERE id = @{id:int}
    </sql>
</mapper>
```

* 具体参考：[《WEED3 XML 语法》](WEED3_XML_语法.md)

##### （三）Table 语法

* 条件操作（与Mapper共享）

| 方法 | 效果说明 |
| --- | --- |
| where, whereIf |  |
| whereEq, whereNeq | ==, != |
| whereLt, whereLte | \<, \<= |
| whereGt, whereGte | \>, \>= |
| whereLk, whereNlk | LIKE, NOT LIKE |
| whereIn, whereNin | IN(..), NOT IN(..) |
| whereBtw, whereNbtw | BETWEEN, NOT BETWEEN |
| and系统方法 | 同where |
| or系统方法 | 同where |
| begin | \( |
| end | \) |

* 表操作（Table独占）

| 方法 | 效果说明 |
| --- | --- |
| set, setIf | 设置值 |
| setMap, setMapIf | 设置值 |
| setEntity, setEntityIf | 设置值 |
| table | 主表 |
| innerJoin, leftJoin, rightJoin | 关联表 |
| on, onEq | 关联条件 |
| orderBy, orderByAsc, orderByDesc | 排序 |
| groupBy | 组 |
| having | 组条件 |
| limit | 限制范围 |
| select | 查询（返回IQuery） |
| count | 查询快捷版，统计数量 |
| exists | 查询快捷版，是否存在 |
| update | 更新 |
| insert | 插入 |
| delete | 删除 |

* 更多参考：[《WEED3 JAVA 接口字典》](WEED3_JAVA_接口字典.md)


