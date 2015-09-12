<File>	//输出的文件名字（包括后缀名）
<Items>	//用于循环构建的项目格式,可随意定制
<Main>	//输出的主体


固定属性说明:
---------------------------------
{namespace}	//命名空间
{classname}	//首字母大写,没有空隔号(以_取代空隔),单数形式的表名
{tablename}	//数据库里取出来的表名/视图名/存储过程名

{tablemap}	//映射表名
{classmap}	//映射类名
{time}		//生成时间

{keycolname} //关键字列
{keysqltype} //关键字列类型
{keycolnote} //关键字列注释


以下属性专为<Items>下的项调用
-----------------------------------
{colname}	//数据库取出来的字段名
{popname}	//首字母大写,没有空隔号

{type}		//C#基础类型
{ntype}     //Nullbe type; like int?
//{qtype}		//QDbType类型
{dbtype}	//DbType类型

{sqltype}	//SQL类型(根据模板配置) //直接从数据库里取出来
{iskey}		//是否为关键字	(true	  :false)
{tryout}	//试着输出		(" OUTPUT":"")
{colnote}	//列注释

{unkeycolname} //非关键字列
{note}		   //注释
{default}	   //默认值