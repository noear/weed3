# Weed for .net/mono/java
超强跨平台轻量级ORM（无反射；缓存控制；分布式事务；万能绑定）<br/>

占位符说明：<br/>
 $.       //表空间占位数（即数据库名）<br/>
 $fcn     //SQL函数占位符<br/>
 ?        //参数占位符<br/>
 ?...     //数组型参数占位符<br/>

网站:<br/>
 http://www.noear.org<br/>

QQ群：<br/>
 22200020<br/>
 
--------------------------------------<br/>
示例::<br/>
db.table("user_info") //表操作(简易版)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.where("user_id<?", 10)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.select("user_id,name,sex")<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.getList&lt;UserInfoModel&gt;();<br/>

db.table("test")<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.insert(new DataItem().set("log_time", "$DATE(NOW())"));<br/>

db.table("test")<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.where("id IN (?...)", new int[] { 15,14,16}) //数据参数<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.update(new DataItem().set("txt", "NOW()xx").set("num", 44)); <br/>

db.call("user_get").set("xxx", 1) //存储过程操作(简易版)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.caching(cache)//使用缓存<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.usingCache(60 * 100) //缓存时间<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.getItem<UserInfoModel>(); <br/>

db.call("user_set").set("xxx", 1) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;.tran() //使用事务<br/>
&nbsp;&nbsp;&nbsp;&nbsp;.execute();<br/>
  
(高定版)及更多示例请参考Weed3Demo <br/>
--------------------------------------<br/>

by noear
