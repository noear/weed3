package org.noear.weed;

import java.io.Serializable;

/**
 * Created by noear on 14-6-13.
 * 万能绑定接口
 */
public interface IBinder extends Serializable {

    void bind(GetHandlerEx source);


    IBinder clone();
}
