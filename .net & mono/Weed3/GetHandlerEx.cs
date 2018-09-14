using System;

namespace Noear.Weed {
    //通过Variate中转，可以简化绑定时的处理(一般为直接赋值;即不方便有判断代码的)
    //
    //old::user_id= (long)(s("user_id")??0); //感观逻辑复杂//??操作符不具跨平台性
    //new::user_id= s("user_id").value(0l);
    //
    public delegate Variate GetHandlerEx (String name); 
}
