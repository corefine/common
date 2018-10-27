package org.corefine.common.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * runner
 */
public class RedisExecuter {
    private final JedisPool pool;

    public RedisExecuter(JedisPool pool) {
        this.pool = pool;
    }

    public <T> T query(RedisCaller<T> caller) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return caller.call(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void execute(RedisRunner runner) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            runner.run(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
