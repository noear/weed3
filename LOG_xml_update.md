##### WEED3 XML SQL更新记录

* 2020.09.17:
1. 添加DTD验证

* 2020.07.22:
1. 所有:开头的属性，都支持不加:

* 2019.12.12:
1. mapper 指令添加 import,:baseMapper,:db 属性
2. :note 属性更名为：:remarks

* 2019.10.22:
1. 添加 trim 指令

* 2019.10.18:
1. sql指令，添加:param属性，用于定制mapper接口的参数申明
2. sql指令，cacheTag属性，添加从结果里取值替换的支持

* 2019.10.17::
1. for指令，添加sep属性，用于拼接时自动添加的分隔符号
2. for指讼，添加${var}_index（例：m_index），用于当前序号获取

* 2019.10.15::
1. 添加ref[@sql]指令，用于引用代码块
2. sql指令，添加:note，用于代码块的描述或说明

