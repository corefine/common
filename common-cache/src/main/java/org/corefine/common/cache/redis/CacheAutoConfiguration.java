package org.corefine.common.cache.redis;

import org.corefine.common.cache.CacheHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

//    @Bean
//    @ConditionalOnProperty(name = "spring.cache.l2.enabled", havingValue = "true")
//    public EhCacheHandler ehCacheHander(CacheProperties properties) {
//        EhCacheHandler hander = new EhCacheHandler();
//        if (properties.getL2() != null) {
//            if (properties.getL2().getMaxCacheCount() != null) {
//                hander.setMaxCacheCount(properties.getL2().getMaxCacheCount());
//            }
//            if (properties.getL2().getMaxCacheMemory() != null) {
//                hander.setMaxCacheMemory(properties.getL2().getMaxCacheMemory());
//            }
//        }
//        hander.init();
//        return hander;
//    }

    @Bean
    public RedisConnection redisConnection(CacheProperties properties) {
        RedisConnection connection = new RedisConnection();
        connection.setHost(properties.getHost());
        if (properties.getPassword() != null) {
            connection.setPassword(properties.getPassword());
        }
        if (properties.getPort() != null) {
            connection.setPort(properties.getPort());
        }
        if (properties.getMaxIdle() != null) {
            connection.setMaxIdle(properties.getMaxIdle());
        }
        if (properties.getMaxTotal() != null) {
            connection.setMaxTotal(properties.getMaxTotal());
        }
        if (properties.getDatabase() != null) {
            connection.setDatabase(properties.getDatabase());
        }
        connection.init();
        return connection;
    }

    @Bean
//    @ConditionalOnMissingBean(EhCacheHandler.class)
    public CacheHandler cacheHandler(RedisConnection redisConnection) {
        RedisCacheHandlerImpl redisCacheHandler = new RedisCacheHandlerImpl();
        redisCacheHandler.setRedisConnection(redisConnection);
        redisCacheHandler.init();
        return redisCacheHandler;
    }

//    @Bean
//    @ConditionalOnBean(EhCacheHandler.class)
//    public L2CacheHandler l2CacheHander(EhCacheHandler ehCacheHandler, RedisConnection redisConnection) {
//        L2CacheHandlerImpl hander = new L2CacheHandlerImpl();
//        hander.setEhCacheHandler(ehCacheHandler);
//        hander.setRedisConnection(redisConnection);
//        hander.init();
//        return hander;
//    }

}
