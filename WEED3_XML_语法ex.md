# Weed3 Xml sql 语法


### 语法说明
```
mapper 开始标签
  namespace （属性：命名空间，{namespace}.{id} = sqlid；不包括文件名）
  import（属性：导入包或类，多个以;号隔开。可以简化后面的模型写法）
  :baseMapper（属性：扩展BaseMapper 的模型，效果：BaseMapper<Xxx>）
  :db（属性：关联 db bean；效果：@Db("xxx")）

sql 代码块定义指令
  id
  :param?（属性：外部输入变量申明；默认会自动生成::新增***）
  :declare（属性：内部变量类型预申明）
  :return（属性：返回类型）

  :remarks（属性：描述、摘要）

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
@{name:type?} = 变量注入
${name:type?} = 变量替换

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

### 示例
```xml

<?xml version="1.0" encoding="utf-8" ?>
<mapper namespace="weed3demo.xmlsql2"
        import="weed3demo.mapper.*"
        :baseMapper="UserModel"
        :db="testdb">
    <sql id="user_add1"
         :return="long"
         :param="m:UserModel,sex:int"
         :remarks="添加用户">
        <insert table="user" data="m.user_id,m.mobile,sex" notNull="true" />
    </sql>

    <sql id="user_add2"
         :return="long"
         :remarks="添加用户">
        <insert table="user" data="m:UserModel" notNull="true" />
    </sql>

    <sql id="user_add3" :return="long" :remarks="添加用户">
        <insert table="user" data="user_id:int,mobile:String" />
    </sql>

    <sql id="user_set2"
             :remarks="更新一个用户，并清理相关相存"
             :caching="localCache"
             :cacheClear="user_${user_id},user_1">
        <update table="user" data="mobile:String,sex:int,icon:String" notNull="true" />
        WHERE id=@{id:int}
     </sql>


</mapper>

```
