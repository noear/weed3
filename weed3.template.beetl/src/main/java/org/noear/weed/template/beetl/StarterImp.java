package org.noear.weed.template.beetl;

import org.noear.weed.IStarter;
import org.noear.weed.SQLRenderManager;

public class StarterImp implements IStarter {

    @Override
    public void start() {
        SQLRenderManager.global().mapping(".btl.sql", SQLBeetlRender.global());
    }
}
