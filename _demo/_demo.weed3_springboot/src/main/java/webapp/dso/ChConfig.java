package webapp.dso;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChConfig {
    @Bean("cache")
    public ICacheServiceEx cache(){
        //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
        return new LocalCache("test",60).nameSet("test");
    }
}
