# Weed3模板语法

### 公共语法
@{name} ：变量占位符，会由Weed3转换为JDBC变量

### 具体引擎语法例

#### beetl
* 参考 beetl 语法
* 边界符调整（-- 或 #）
```sql
//例1
#if(agroup_id == 0){
    select * from appx where app_id =${id}
#}else{
    select * from appx where agroup_id = @{agroup_id} limit 1
#}

//例2
--if(agroup_id == 0){
    select * from appx where app_id =@{id}
--}else{
    select * from appx where agroup_id = @{agroup_id} limit 1
--}
```

#### enjoy
* 参考 enjoy 语法
```sql
#if(agroup_id == 0)
    select * from appx where app_id =#(id)
#else
    select * from appx where agroup_id = @{agroup_id} limit 1
#end
```

#### freemarker
* 参考 freemarker 语法
```sql
<#if agroup_id == 0>
    select * from appx where app_id =${id}
<#else>
    select * from appx where agroup_id = @{agroup_id} limit 1
</#if>
```


#### velocity
* 参考 velocity 语法
```sql
#if($agroup_id == 0)
    select * from appx where app_id =${id}
#else
    select * from appx where agroup_id = @{agroup_id} limit 1
#end
```
