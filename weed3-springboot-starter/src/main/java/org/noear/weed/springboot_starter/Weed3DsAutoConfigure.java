package org.noear.weed.springboot_starter;

import org.noear.weed.annotation.Db;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Db.class)
public class Weed3DsAutoConfigure {
    @Bean
    //如果用户自定义了一个 ChyRpcApplication 就不创建,如果没有就在spring 容器中加入一个默认配置的rpc容器,ChyRpcApplication也是我RPC的核心类,类似 sessionFactory
    @ConditionalOnMissingBean(Db.class)
    public Object initChyRpcApplication(){
//        ChyRpcApplication chyRpcApplication = new ChyRpcApplication(rpcProperties.getZookeepeer());
//        chyRpcApplication.setPort(rpcProperties.getPort());
//        chyRpcApplication.setIp(rpcProperties.getIp());
//        return chyRpcApplication;
        return null;
    }
}
