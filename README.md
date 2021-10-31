[![Maven Central](https://img.shields.io/maven-central/v/org.noear/weed3.svg)](https://mvnrepository.com/search?q=weed3)

` QQ交流群：22200020 `

# Weed for java
微型ORM框架（支持：java sql，xml sql，annotation sql，template sql；事务；缓存；监控；等...）



#### Weed3 特点和理念：
* 跨平台：可以嵌入到JVM脚本引擎（js, groovy, lua, python, ruby）及GraalVM支持的部分语言。
* 很小巧：0.1Mb（且是功能完整，方案丰富；可极大简化数据库开发）。
* 有个性：不喜欢反射、不喜欢配置...（除了连接，不需要任何配置）。
* 其它的：支持缓存控制和跨数据库事务（算是分布式事务的一种吧）。



#### 核心对象和功能：

* 上下文：DbContext db
* 四个接口：db.mapper(), db.table(), db.call(), db.sql()

```java
//BaseMapper 接口
db.mapperBase(User.class).selectById(1);

//BaseMapper 接口，条件查询
db.mapperBase(User.class).selectList(mq->mq.whereLt(User::getGroup,1).andEq(User::getLabel,"T"));


//Table 接口
db.table("user u")
  .innerJoin("user_ext e").onEq("u.id","e.user_id")
  .whereEq("u.type",11)
  .limit(100,20)
  .selectList("u.*,e.sex,e.label", User.class);

//Table 接口，拼装条件查询（特别适合管理后台）
db.table(logger)
  .where("1 = 1")
  .andIf(TextUtils.isNotEmpty(trace_id), "trace_id = ?", trace_id)
  .andIf(TextUtils.isNotEmpty(tag), "tag = ?", tag)
  .andIf(TextUtils.isNotEmpty(tag1), "tag1 = ?", tag1)
  .andIf(TextUtils.isNotEmpty(tag2), "tag2 = ?", tag2)
  .andIf(TextUtils.isNotEmpty(tag3), "tag3 = ?", tag3)
  .andIf(log_date > 0, "log_date = ?", log_date)
  .andIf(log_id > 0, "log_id <= ?", log_id)
  .andIf(level > 0, "level=?", level)
  .orderBy("log_fulltime desc")
  .limit(size)
  .selectList("*", LogModel.class);
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
| | |
| org.noear:weed3.cache.memcached| 基于 Memcached 适配的扩展缓存服务 |
| org.noear:weed3.cache.redis| 基于 Redis 适配的扩展缓存服务 |
| org.noear:weed3.cache.ehcache| 基于 ehcache 适配的扩展缓存服务 |
| org.noear:weed3.cache.j2cache| 基于 j2cache 适配的扩展缓存服务 |





#### Meven配置：

```xml
<!-- 框架包 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.4.8</version>
</dependency>

<!-- 可选：maven 插件，用于生成Xml sql mapper接口 -->
<plugin>
    <groupId>org.noear</groupId>
    <artifactId>weed3-maven-plugin</artifactId>
    <version>3.4.8</version>
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
db.table("user").setMap(map).insert();
//删::
db.table("user").whereEq("id",2).delete();
//改::
db.table("user").set("sex",1).whereEq("id",2).update();
//查::
db.table("user u")
  .innerJoin("user_ext e").onEq("u.id","e.user_id")
  .whereEq("u.id",1001)
  .selectItem("u.*,e.sex,e.label", User.class);



/** 2.3.Call用法 */
//调用存储过程
db.call("user_get_list_by").set("_type",12).getList(User.class);

//调用xml sql
db.call("@demo.dso.db.user_get").set("id",1001).getItem(User.class);


/** 2.4.Sql用法 */
//快速执行SQL语句
db.sql("select * from user id=?",12).getItem(User.class);

```



#### 附：语法参考：

##### （一）Xml sql 语法
* 示例
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//noear.org//DTD Mapper 3.0//EN" "http://noear.org/dtd/weed3-mapper.dtd">
<mapper namespace="weed3demo.xmlsql2"
        import="demo.model.*"
        baseMapper="UserModel">
    <sql id="getUser" return="UserModel" remarks="获取用户信息">
        SELECT * FROM user WHERE id = @{id:int}
    </sql>
</mapper>
```

* 具体参考：[《WEED3 XML 语法》](WEED3_XML_语法.md)

##### （二）Table 语法

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


