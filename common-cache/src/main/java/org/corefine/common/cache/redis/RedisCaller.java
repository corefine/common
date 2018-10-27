package org.corefine.common.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * redis 执行方法
 */
public interface RedisCaller<T> {

    public T call(Jedis jedis);
}
