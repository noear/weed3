package org.noear.weed.template.freemarker;

import org.noear.weed.IStarter;
import org.noear.weed.SQLRenderManager;

public class StarterImp implements IStarter {
    @Override
    public void start() {
        SQLRenderManager.global().mapping(".ftl", SQLFreemarkerRender.global());
    }
}
