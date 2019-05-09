package org.corefine.common.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import org.corefine.common.cache.redis.RedisCaller;
import org.corefine.common.cache.redis.RedisRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存服务
 *
 * @author Fe 2016年3月3日
 */
public interface CacheHandler {
    Logger LOG = LoggerFactory.getLogger(CacheHandler.class);
    Integer defaultCacheTime = 2 * 60 * 60;
    Integer lockDefaultCacheTime = defaultCacheTime / 60;
    String Y = "Y";
    byte[] YES = Y.getBytes();
    byte[] NULL = "(nil)".getBytes();


    /**
     * 通用查询方法。
     */
    <T> T query(RedisCaller<T> caller);

    /**
     * 通用执行方法。
     */
    void execute(RedisRunner runner);

    /**
     * 以对象方式获取对象
     *
     * @param key key
     * @param <T> value
     * @return value
     */
    <T> T getObject(String key);

    /**
     * 以对象方式设置对象
     *
     * @param key   key
     * @param value value
     */
    default void setObject(String key, Object value) {
        setObject(key, value, defaultCacheTime);
    }

    /**
     * 以对象方式设置对象
     *
     * @param key        key
     * @param value      value
     * @param expireTime 超时
     */
    void setObject(String key, Object value, Integer expireTime);

    /**
     * 缓存数据。数据缓存两小时后强制重新查询
     *
     * @param key  缓存的key
     * @param data 数据来源
     */
    default <T> T cache(String key, Class<T> clazz, CacheData<T> data) {
        return cache(key, clazz, data, defaultCacheTime);
    }

    /**
     * 缓存数据。数据缓存两小时后强制重新查询
     *
     * @param key  缓存的key
     * @param data 数据来源
     */
    default <T> T cache(String key, TypeReference<T> typeReference, CacheData<T> data) {
        return cache(key, typeReference, data, defaultCacheTime);
    }

    /**
     * 缓存数据。数据缓存expireTime秒后强制重新查询
     *
     * @param key        缓存的key
     * @param data       数据来源
     * @param expireTime 超时时间（秒）
     * @return 结果
     */
    default <T> T cache(String key, Class<T> clazz, CacheData<T> data, Integer expireTime) {
        T result = this.get(key, clazz);
        if (result == null) {
            result = data.findData();
            if (result != null) {
                this.set(key, result, expireTime);
            }
        }
        return result;
    }

    /**
     * 缓存数据。数据缓存expireTime秒后强制重新查询
     *
     * @param key        缓存的key
     * @param data       数据来源
     * @param expireTime 超时时间（秒）
     * @return 结果
     */
    default <T> T cache(String key, TypeReference<T> typeReference, CacheData<T> data, Integer expireTime) {
        T result = this.get(key, typeReference);
        if (result == null) {
            result = data.findData();
            if (result != null) {
                this.set(key, result, expireTime);
            }
        }
        return result;
    }

    /**
     * 获取一个string值
     *
     * @param key key
     * @return 结果
     */
    default String getString(String key) {
        return get(key, String.class);
    }

    /**
     * 从缓存中获取一个数据
     *
     * @param key 缓存的key
     * @return 结果
     */
    <T> T get(String key, TypeReference<T> typeReference);

    /**
     * 从缓存中获取一个数据
     *
     * @param key 缓存的key
     * @return 结果
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 设置缓存数据。数据将缓存两小时
     *
     * @param key   缓存的key
     * @param value 缓存的value
     */
    default void set(String key, Object value) {
        this.set(key, value, defaultCacheTime);
    }

    /**
     * 设置缓存数据
     *
     * @param key        缓存的key
     * @param value      缓存的value
     * @param expireTime 超时时间（秒）
     */
    void set(String key, Object value, Integer expireTime);

    /**
     * 删除数据
     *
     * @param key  缓存的key
     * @param keys 缓存的key（多个）
     */
    void delete(String key, String... keys);

    /**
     * 判断是否存在指定key
     *
     * @param key key
     * @return 存在：true；不存在：false
     */
    boolean exists(String key);

    /**
     * 原子性增量实现
     *
     * @param key        key
     * @param expireTime 超时时间
     * @return 结果
     */
    long incr(String key, Integer expireTime);

    /**
     * 原子性增量实现(+1)
     *
     * @param key key
     * @return 结果
     */
    default long incr(String key) {
        return incr(key, defaultCacheTime);
    }

    /**
     * 原子性增量实现(+increment)
     *
     * @param key       key
     * @param increment 增量
     * @return 结果
     */
    default long incrBy(String key, long increment) {
        return incrBy(key, increment, defaultCacheTime);
    }

    /**
     * 原子性增量实现(+increment)
     *
     * @param key        key
     * @param increment  增量
     * @param expireTime 超时时间
     * @return 结果
     */
    long incrBy(String key, long increment, Integer expireTime);

    /**
     * 原子性增量实现(当前值)
     *
     * @param key key
     * @return 结果
     */
    default Long incrCurrent(String key) {
        Integer expireTime;
        if (ttl(key) == -2) {
            expireTime = defaultCacheTime;
        } else {
            expireTime = null;
        }
        return incrBy(key, 0, expireTime);
    }

    /**
     * 查询超时时间
     *
     * @param key key
     * @return 超时时间
     */
    int ttl(String key);

    /**
     * 获取集合的size
     *
     * @param key key
     * @return 结果
     */
    long scard(String key);

    /**
     * 从集合中随机取出一个值，并将值从集合中删除
     *
     * @param key key
     * @return 结果
     */
    String spop(String key);

    /**
     * 向集合中添加一个值。
     * 注意：此key是无超时的
     *
     * @param key   key
     * @param value value
     */
    void sadd(String key, String value);

    /**
     * 获取集合中的元素
     *
     * @param key key
     * @return 所有元素
     */
    Set<String> smembers(String key);

    /**
     * 设置超时
     *
     * @param key        key
     * @param expireTime 超时时间
     */
    void expire(String key, Integer expireTime);

    /**
     * 不存在时设置
     *
     * @param key        key
     * @param expireTime 超时时间
     * @return 结果
     */
    default boolean setNx(String key, Integer expireTime) {
        return setNx(key, Y, expireTime);
    }

    /**
     * 不存在时设置
     *
     * @param key        key
     * @param value      value
     * @param expireTime 超时时间
     * @return 结果
     */
    boolean setNx(String key, Object value, Integer expireTime);

    /**
     * 将数据放入list中
     *
     * @param key        key
     * @param objects    objects
     * @param expireTime 超时时间
     */
    void lpush(String key, Integer expireTime, Object... objects);

    /**
     * 获取list的总长度
     *
     * @param key key
     * @return 结果
     */
    Long llen(String key);

    /**
     * 获取list的结果
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 结果
     */
    <T> List<T> lrange(String key, Class<T> clazz, long start, long end);

    /**
     * 从list中删除部分数据
     *
     * @param key   key
     * @param start start
     * @param end   end
     */
    void ltrim(String key, long start, long end);

    /**
     * 排行榜功能添加积分
     *
     * @param key    key
     * @param score  score
     * @param target target
     */
    default void zincrby(String key, double score, String target) {
        zincrby(key, score, target, defaultCacheTime);
    }

    /**
     * 排行榜功能添加积分
     *
     * @param key        key
     * @param score      score
     * @param target     target
     * @param expireTime 超时时间
     */
    void zincrby(String key, double score, String target, Integer expireTime);

    /**
     * 排行榜功能添加积分
     *
     * @param key    key
     * @param score  score
     * @param target target
     */
    default long zadd(String key, double score, String target) {
        return zadd(key, score, target, defaultCacheTime);
    }

    /**
     * 排行榜功能添加积分
     *
     * @param key        key
     * @param score      score
     * @param target     target
     * @param expireTime 超时时间
     */
    long zadd(String key, double score, String target, Integer expireTime);

    /**
     * 排行榜功能-获取排行榜
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 结果
     */
    <T> List<RankingData<T>> zrevrangeWithScores(String key, Class<T> clazz, long start, long end);

    /**
     * 清楚排行榜中的某用户积分
     *
     * @param key    key
     * @param member member
     */
    void zrem(String key, String... member);

    /**
     * 获取指定成员的排名
     *
     * @param key    key
     * @param member member
     * @return 结果
     */
    Long zrevrank(String key, String member);

    /**
     * 获取指定成员的分值
     *
     * @param key    key
     * @param member member
     * @return 结果
     */
    Double zscore(String key, String member);

    /**
     * (hash)将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key        key
     * @param expireTime 超时时间，单位秒。如果为空，将永久存在；否则每次执行都会重置超时时间。
     * @param field      域
     * @param value      值
     */
    void hset(String key, Integer expireTime, String field, String value);

    /**
     * (hash)将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 每次执行都会重置超时时间为2小时。
     *
     * @param key   key
     * @param field 域
     * @param value 值
     */
    default void hset(String key, String field, String value) {
        hset(key, defaultCacheTime, field, value);
    }

    /**
     * (hash)同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域。
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     *
     * @param key        key
     * @param expireTime 超时时间，单位秒。如果为空，将永久存在；否则每次执行都会重置超时时间。
     * @param hash       域-值集合
     */
    void hmset(String key, Integer expireTime, Map<String, String> hash);

    /**
     * (hash)同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域。
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * 每次执行都会重置超时时间为2小时。
     *
     * @param key  key
     * @param hash 域-值集合
     */
    default void hmset(String key, Map<String, String> hash) {
        hmset(key, defaultCacheTime, hash);
    }

    /**
     * (hash)删除域
     *
     * @param key    key
     * @param fields 要删除的域集合
     * @return 删除的个数
     */
    long hdel(String key, String... fields);

    /**
     * (hash)返回哈希表 key 中给定域 field 的值
     *
     * @param key   key
     * @param field 域
     * @return 给定域的值
     */
    String hget(String key, String field);

    /**
     * (hash)返回哈希表 key 中，一个或多个给定域的值
     *
     * @param key    key
     * @param fields 域集合
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    List<String> hmget(String key, String... fields);

    /**
     * (hash)返回哈希表 key 中所有域的值
     *
     * @param key key
     * @return 一个包含哈希表中所有值的表。当 key 不存在时，返回一个空表。
     */
    List<String> hvals(String key);

    /**
     * (hash)返回哈希表 key 中域的数量。
     *
     * @param key key
     * @return 哈希表中域的数量。
     * 当 key 不存在时，返回 0 。
     */
    Long hlen(String key);

    /**
     * (hash)返回哈希表 key 中的所有域。
     *
     * @param key key
     * @return 一个包含哈希表中所有域的表。
     * 当 key 不存在时，返回一个空表。
     */
    Set<String> hkeys(String key);

    /**
     * (hash)返回哈希表 key 中，所有的域和值。
     *
     * @param key key
     * @return 返回哈希表的域和域的值。
     * 若 key 不存在，返回空列表。
     */
    Map<String, String> hgetAll(String key);

    /**
     * (hash)查看哈希表 key 中，给定域 field 是否存在。
     *
     * @param key   key
     * @param field 哉
     * @return 哈希表含有给定域
     */
    boolean hexists(final String key, final String field);
}
