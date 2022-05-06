# Weed3 mapper 注解语法


### 例1：变量占位符模式
```java
@Sql("select * from where id=@{id}")
```


### 例2：位置占符模式
```java
@Sql("select * from where id=?")
```


### 完整示例

```java
@Namespace("demo.dso.db")
public interface UserDao extends BaseMapper<UserModel>{
    @Sql("select * from user where id=@{id} limit 1")
    UserModel getUser(int id);
  
    @Sql("select * from user where id=? limit 1")
    UserModel getUser2(int id);

    void addUser(UserModel user); //没注解，需要配xml
}
```

