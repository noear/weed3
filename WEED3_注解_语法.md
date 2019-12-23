# Weed3注解语法


### 例1：变量占位符模式
```java
@Sql("select * from where id=@{id}")
```


### 例2：位置占符模式
```java
@Sql("select * from where id=?")
```


### 例3：模本文件模式（\#开头）
```java
@Sql("#user_stat.sql")
```