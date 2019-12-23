# Weed3模板语法

### 公共语法
@{name} ：变量占位符，会由Weed3进一步转化为JDBC变量

### 具体引擎语法例

#### weed3.render.beetl
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

#### weed3.render.enjoy
* 参考 enjoy 语法
```sql
#if(agroup_id == 0)
    select * from appx where app_id =#(id)
#else
    select * from appx where agroup_id = @{agroup_id} limit 1
#end
```

#### weed3.render.freemarker
* 参考 freemarker 语法
```sql
<#if agroup_id == 0>
    select * from appx where app_id =${id}
<#else>
    select * from appx where agroup_id = @{agroup_id} limit 1
</#if>
```


#### weed3.render.velocity
* 参考 velocity 语法
```sql
#if($agroup_id == 0)
    select * from appx where app_id =${id}
#else
    select * from appx where agroup_id = @{agroup_id} limit 1
#end
```
