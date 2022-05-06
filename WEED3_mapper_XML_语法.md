# Weed3 mapper xml sql 语法

### 语法说明
```
mapper 开始标签
  namespace （属性：命名空间，{namespace}.{id} = sqlid；不包括文件名）
  import（属性：导入包或类，多个以;号隔开。可以简化后面的模型写法，或引入工具类）
  baseMapper（属性：扩展BaseMapper 的模型，效果：BaseMapper<Xxx>）
  db（属性：关联 dataSource bean；效果：@Db("xxx")）

sql 代码块定义指令
  id
  param?（属性：外部输入变量申明；默认会自动生成）
  declare?（属性：内部变量类型预申明）
  return（属性：返回类型）

  remarks（属性：描述、摘要）

  caching?（属性：缓存服务name） //是对 ICacheController 接口的映射
  cacheClear?（属性：清除缓存）
  cacheTag?（属性：缓存标签，支持在入参或结果里取值替换）
  usingCache?（属性：缓存时间,int）

if 判断控制指令（没有else）
  test （属性：判断检测代码）
     //xml避免语法增强:
     //lt(<) lte(<=) gt(>) gte(>=) and(&&) or(||)
        //例：m.sex gte 12 :: m.sex >=12
     //简化语法增强:
     //??(非null,var!=null) ?!(非空字符串, Utils.isNoteEmpty(var))
        //例：m.icon??  ::m.icon!=null
        //例：m.icon?!  ::Utils.isNoteEmpty(m.icon)

for 循环控制指令 （通过 ${var}_index 可获得序号，例：m_index）
  var （属性：循环变量申明）
  items （属性：集合变量名称）
  sep? （属性：分隔符）

trim 修剪指令
  trimStart（属性：开始位去除）
  trimEnd（属性：结尾位去除）
  prefix（属性：添加前缀）
  suffix（属性：添加后缀）

ref 引用代码块指令
  sql （属性：代码块id）

name:type    = 变量申明（可用于属性：:param, :declare，var，或宏定义 @{..},${..}）
@{name:type?} = 变量注入
${name:type?} = 变量替换，即成为字符串的一部份

//列表([]替代<>)
return="List[weed3demo.mapper.UserModel]" => List<UserModel>
return="List[String]" => List<String> （Date,Long,...大写开头的单值类型）
return="MapList" => List<Map<String,Object>>
return="DataList" => DataList

//一行
return="weed3demo.mapper.UserModel" => UserModel
return="Map" => Map<String,Object>
return="DataItem" => DataItem

//单值
return="String" => String （任何单职类型）
```

### 示例
```xml

<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//noear.org//DTD Mapper 3.0//EN" "http://noear.org/dtd/weed3-mapper.dtd">
<mapper namespace="weed3demo.xmlsql2"
        import="weed3demo.mapper.*"
        baseMapper="UserModel"
        db="testdb">
    <sql id="user_add1"
         return="long"
         param="m:UserModel,sex:int"
         remarks="添加用户">
        INSERT user(user_id,mobile,sex) VALUES(@{m.user_id},@{m.mobile},@{sex})
    </sql>

    <sql id="user_add2" return="long" remarks="添加用户">
        INSERT user(user_id) VALUES(@{user_id:int})
    </sql>

    <sql id="user_add_for" return="long" declare="list:List[UserModel]" remarks="批量添加用户3">
        INSERT user(id,mobile,sex) VALUES
        <for var="m:UserModel" items="list">
            (@{m.user_id},@{m.mobile},@{m.sex})
        </for>
    </sql>

    <sql id="user_del" remarks="删除一个用户">
        DELETE FROM user WHERE id=@{user_id:long}
        <if test="sex gt 0">
            AND sex=@{sex:int}
        </if>
    </sql>

    <sql id="user_set"
         remarks="更新一个用户，并清理相关相存"
         caching="localCache"
         cacheClear="user_${user_id},user_1">
        UPDATE user SET mobile=@{mobile:String},sex=@{sex:int}
        <if test="icon != null">
            icon=@{icon:String}
        </if>
    </sql>

    <sql id="user_get" remarks="获取用户" return="UserModel">
        SELECT * FROM user WHERE user_id=@{user_id:long}
    </sql>

    <sql id="user_get_list"
         remarks="获取一批符合条件的用户"
         declare="foList:int,user_id:long"
         return="List[UserModel]"
         caching="localCache"
         cacheTag="user_${user_id},user_1">
        SELECT id,${cols:String} FROM user
        <trim prefix="WHERE" trimStart="AND ">
            <if test="mobile?!">
                AND mobile LIKE '${mobile:String}%'
            </if>
            <if test="foList == 0">
                AND type='article'
            </if>
            <if test="foList == 1">
                AND type='post'
            </if>
        </trim>
    </sql>

    <sql id="user_cols1">name,title,style,label</sql>
    <sql id="user_cols2">name,title</sql>

    <sql id="user_get_list2"
         remarks="获取一批符合条件的用户"
         declare="foList:int,user_id:long"
         return="List[UserModel]"
         caching="localCache"
         cacheTag="user_${user_id},user_1">
        SELECT id,
        <if test="foList == 0">
            <ref sql="user_cols1"/>
        </if>
        <if test="foList == 1">
            <ref sql="user_cols2"/>
        </if>
        <![CDATA[
        FROM user WHERE sex > 1 AND mobile LIKE '@{mobile:String}%'
        ]]>
    </sql>

    <sql id="user_get_list3"
         remarks="获取一批符合条件的用户"
         return="List[UserModel]">
        SELECT * FROM user WHERE user_id IN (@{user_ids:List[Long]})
    </sql>
</mapper>

```

### 示例应用
```java
//使用方案1
db.call("@weed3demo.xmlsql2.user_set").set("user_id",12).update()
db.call("@weed3demo.xmlsql2.user_add").setMap(map).insert()

//使用方案2
@Namespace("weed3demo.xmlsql2")
public interface UserDao extends BaseMapper<UserModel> {
    UserModel user_get(int user_id);
}

UserDao userDao = db.mapper(UserDao.class);
UserModel user = userDao.user_get(12);

//使用方案3
UserModel user = db.mapper("@weed3demo.xmlsql2.user_get", map);
```