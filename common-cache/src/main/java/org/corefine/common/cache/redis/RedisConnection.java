package org.corefine.common.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Fe
 */
public class RedisConnection {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String host;
    private String password;
    private int port = 6379;
    private int maxIdle = 1;
    private int maxTotal = 100;
    private int timeout = 3000;
    private int database = 0;
    private RedisExecuter redisExecuter;

    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        if (password != null && password.trim().length() == 0) {
            password = null;
        }
        JedisPool pool = new JedisPool(config, host, port, timeout, password, database);
        redisExecuter = new RedisExecuter(pool);
        logger.info("redis connection is init. {}:{} maxIdle={},maxTotal={}", host, port, maxIdle, maxTotal);
    }

    public RedisExecuter getExecuter() {
        return redisExecuter;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
