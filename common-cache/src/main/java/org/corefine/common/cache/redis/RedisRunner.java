package org.corefine.common.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * redis 执行方法
 */
public interface RedisRunner {

    public void run(Jedis jedis);
}
