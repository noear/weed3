using System;

namespace Noear.Weed {
    //适合不需要中转的绑定处理(一般为非直接赋值;即有判断代码的)
    //
    public delegate object GetHandler (String name);
}
