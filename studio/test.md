## 示例

~~~[api]
get:/url
*id=默认值#说明文字
name#说明文字
<<<
success
{
    "errNum": 0,
    "retMsg": "success",
    "retData": {}
}
<<<
error
这里填写错误的返回码
以此类推，每个状态使用 <<< 分割,
第一行添加状态名称
~~~
