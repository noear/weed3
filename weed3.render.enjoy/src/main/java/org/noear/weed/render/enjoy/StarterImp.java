package org.noear.weed.render.enjoy;

import org.noear.weed.IStarter;
import org.noear.weed.SQLRenderManager;

public class StarterImp implements IStarter {
    @Override
    public void start() {
        SQLRenderManager.global().mapping(".enj.sql", SQLEnjoyRender.global());
    }
}
