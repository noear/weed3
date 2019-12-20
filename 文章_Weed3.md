
### 简介
#### 什么是Weed3
Weed3 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。
Weed3 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。
Weed3 可以使用简单的 XML 或注解来配置和映射原生类型、接口和 Java 的 POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

### 入门
#### 安装
如果使用 Maven 来构建项目，则需将下面的 dependency 代码置于 pom.xml 文件中：
```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>weed3</artifactId>
    <version>3.2.4.4-SNAPSHOT</version>
</dependency>
```

#### 在Java中构建DbContext
每个基于 Weed3 的应用都是以一个 DbContext 的实例为核心的。DbContext 的结构很简单：
```java
//使用Properties配置的示例
DbContext db  = new DbContext(properties); 

//使用Map配置的示例
DbContext db  = new DbContext(map); 

//使用proxool线程池配置的示例
DbContext db  = new DbContext("user","proxool.xxx_db"); 

//使用DataSource配置的示例
DataSource ds = new HikariDataSource(...);
DbContext db  = new DbContext("user",ds); 

//参数一个个写
DbContext db  = new DbContext("user","jdbc:mysql://x.x.x:3306/user","root","1234");
```

## 草案（末写完...）
