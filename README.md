[![Maven Central](https://img.shields.io/maven-central/v/org.noear/weed3.svg)](https://mvnrepository.com/search?q=weed3)

` QQ交流群：22200020 `

# Weed for java
微型ORM（支持：java sql，xml sql，annotation sql；事务；缓存；等...）


#### Weed3 特点和理念：
* 高性能：两年前有个同事测过四个ORM框架，它是性能最好的（不知道现在是不是）。
* 跨平台：可以嵌入到JVM脚本引擎（js, groovy, lua, python, ruby）；有.net，php版本（久没维护了）。
* 很小巧：0.1Mb（且是功能完整，方案丰富；可极大简化数据库开发）。
* 有个性：不喜欢反射、不喜欢配置...（除了连接，不需要任何配置）。
* 其它的：支持缓存控制和跨数据库事务（算是分布式事务的一种吧）。



#### 相关文章：

* [Weed3 for java 新的微型ORM框架](https://my.oschina.net/noear/blog/3144349) （推荐一看）
* [Weed3 ORM框架专题系列文章](https://www.jianshu.com/nb/40211584?order_by=seq)



#### 组件： 

| 组件 | 说明 |
| --- | --- |
| org.noear:weed3 | 主框架 |
| org.noear:weed3-maven-plugin| Maven插件，用于生成Xml sql mapper |
| | |
| org.noear:weed3.cache.memcached| 基于 Memcached 封装的扩展缓存服务 |
| org.noear:weed3.cache.redis| 基于 Redis 封装的扩展缓存服务 |
| org.noear:weed3.cache.ehcache| 基于 ehcache 封装的扩展缓存服务 |
| org.noear:weed3.cache.j2cache| 基于 j2cache 封装的扩展缓存服务 |



#### 核心对象和功能：

* 上下文：DbContext db
* 四个接口：db.mapper(), db.table(), db.call(), db.sql()



#### Meven配置：

```xml
<!-- 框架包 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.2.4.3</version>
</dependency>

<!-- maven 插件，用于生成Xml sql mapper接口 -->
<plugin>
    <groupId>org.noear</groupId>
    <artifactId>weed3-maven-plugin</artifactId>
    <version>3.2.4.3</version>
</plugin>
```



#### 示例：
```java
/** 1.实例化上下文 */
//DbContext db  = new DbContext(properties); //使用Properties配置的示例
//DbContext db  = new DbContext(map); //使用Map配置的示例
//DbContext db  = new DbContext("user","proxool.xxx_db"); //使用proxool线程池配置的示例
//DbContext db  = new DbContext("user",new HikariDataSource(...)); //使用DataSource配置的示例
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");

/** 2.Mapper用法 */
@Namespace("demo.dso.db")
public interface UserDao extends BaseMapper<UserModel>{
    @Sql("select * from user where id=@{id} limit 1")
    UserModel getUser(int id);
  
    @Sql("select * from user where id=? limit 1")
    UserModel getUser2(int id);

    void addUser(UserModel user); //没注解，需要配xml
}

UserDao userDao = db.mapper(UserDao.class);
userDao.selectById(12); //调用 BaseMapper 方法
UserModel user = userDao.getUser(2); //调用 @Sql 方法
userDao.addUser(user); //调用 xml sql


/** 3.Table用法 */
//增::
db.table("user").setEntity(user).insert();
//删::
db.table("user").where("id=?",2).delete();
//改::
db.table("user").set("sex",1).where("id=?",2).delete();
//查::
db.table("user").where("id=?",1).select("*").getItem(User.class);
```



#### 附：语法参考：

##### （一）Xml sql 语法
* 示例
```xml
<?xml version="1.0" encoding="utf-8" ?>
<mapper namespace="weed3demo.xmlsql2">
    <sql id="getUser" :return="demo.model.UserModel" :note="获取用户信息">
        SELECT * FROM user WHERE id = @{id:int}
    </sql>
</mapper>
```

* 配置说明
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

##### （二）Table 语法

1. 条件操作（与Mapper共享）

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

2. 表操作（Table独占）

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

3. IQuery接口

* `long getCount() throws SQLException;`
* `Object getValue() throws SQLException;`
* `<T> T getValue(T def) throws SQLException;`
* `Variate getVariate() throws SQLException;`
* `<T> T getItem(Class<T> cls) throws SQLException;`
* `<T> List<T> getList(Class<T> cls) throws SQLException;`
* `DataList getDataList() throws SQLException;`
* `DataItem getDataItem() throws SQLException;`
* `List<Map<String,Object>> getMapList() throws SQLException;`
* `Map<String,Object> getMap() throws SQLException;`
* `<T> List<T> getArray(String column) throws SQLException;`
* `<T> List<T> getArray(int columnIndex) throws SQLException;`
* 等...


