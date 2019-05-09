package org.corefine.common.cache.redis;

import com.fasterxml.jackson.core.type.TypeReference;

import org.corefine.common.cache.CacheData;
import org.corefine.common.cache.CacheHandler;
import org.corefine.common.cache.RankingData;

import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.corefine.common.cache.redis.SerializeUtils.deserializeKey;
import static org.corefine.common.cache.redis.SerializeUtils.deserializeObject;
import static org.corefine.common.cache.redis.SerializeUtils.deserializeValue;
import static org.corefine.common.cache.redis.SerializeUtils.serializeKey;
import static org.corefine.common.cache.redis.SerializeUtils.serializeObject;
import static org.corefine.common.cache.redis.SerializeUtils.serializeValue;

/**
 * redis缓存实现
 *
 * @author Fe  2016年3月3日
 */
public class RedisCacheHandlerImpl implements CacheHandler {
    private RedisConnection redisConnection;
    protected RedisExecuter executer;

    @Override
    public <T> T query(RedisCaller<T> caller) {
        return executer.query(caller);
    }

    @Override
    public void execute(RedisRunner runner) {
        executer.execute(runner);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        byte[] bKey = serializeKey(key);
        byte[] bValue = executer.query(con -> con.get(bKey));
        return (T) deserializeObject(bValue);
    }

    @Override
    public void setObject(String key, Object value, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        byte[] bValue = serializeObject(value);
        executer.execute(con -> {
            con.set(bKey, bValue);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
        });
    }

    @Override
    public <T> T cache(String key, Class<T> clazz, CacheData<T> data, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        byte[] bValue = executer.query(con -> con.get(bKey));
        if (bValue == null || Arrays.equals(bValue, NULL)) {
            T value = data.findData();
            if (value == null) {
                return null;
            }
            bValue = serializeValue(value);
            final byte[] bValue0 = bValue;
            executer.execute(con -> {
                con.set(bKey, bValue0);
                if (expireTime != null) {
                    con.expire(bKey, expireTime);
                }
            });
        }
        return deserializeValue(bValue, clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeReference) {
        byte[] bKey = serializeKey(key);
        return deserializeValue(executer.query(con -> con.get(bKey)), typeReference);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        byte[] bKey = serializeKey(key);
        return deserializeValue(executer.query(con -> con.get(bKey)), clazz);
    }

    @Override
    public void set(String key, Object value, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        byte[] bValue = serializeValue(value);
        executer.execute(con -> {
            con.set(bKey, bValue);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
        });
    }

    @Override
    public void delete(String key, String... keys) {
        if (key != null) {
            byte[] bKey = serializeKey(key);
            executer.execute(con -> con.del(bKey));
        }
        if (keys.length > 0) {
            byte[][] bKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                bKeys[i] = serializeKey(keys[i]);
            }
            executer.execute(con -> con.del(bKeys));
        }
    }

    @Override
    public boolean exists(String key) {
        return executer.query(con -> con.exists(key));
    }

    @Override
    public long incr(String key, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        return executer.query(con -> {
            Long c = con.incr(bKey);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
            return c;
        });
    }

    @Override
    public long incrBy(String key, long increment, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        return executer.query(con -> {
            Long c = con.incrBy(bKey, increment);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
            return c;
        });
    }

    @Override
    public int ttl(String key) {
        return executer.query(con -> con.ttl(key).intValue());
    }

    @Override
    public long scard(String key) {
        return executer.query(con -> con.scard(serializeKey(key)));
    }

    @Override
    public String spop(String key) {
        byte[] bKey = serializeKey(key);
        return deserializeKey(executer.query(con -> con.spop(bKey)));
    }

    @Override
    public void sadd(String key, String value) {
        byte[] bKey = serializeKey(key);
        executer.execute(con -> con.sadd(bKey, serializeKey(value)));
    }

    @Override
    public Set<String> smembers(String key) {
        byte[] bKey = serializeKey(key);
        Set<byte[]> result = executer.query(con -> con.smembers(bKey));
        Set<String> set = new HashSet<>(result.size());
        result.forEach(data -> set.add(deserializeKey(data)));
        return set;
    }

    @Override
    public void expire(String key, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        if (expireTime != null) {
            executer.execute(con -> con.expire(bKey, expireTime));
        }
    }

    @Override
    public boolean setNx(String key, Object value, Integer expireTime) {
        byte[] bkey = serializeKey(key);
        byte[] bvalue = serializeValue(value);
        return executer.query(con -> {
            boolean r = con.setnx(bkey, bvalue) == 1;
            if (r && expireTime != null) {
                con.expire(bkey, expireTime);
            }
            return r;
        });
    }

    @Override
    public void lpush(String key, Integer expireTime, Object... value) {
        byte[] bKey = serializeKey(key);
        byte[][] bValues = new byte[value.length][];
        for (int i = 0; i < bValues.length; i++) {
            bValues[i] = serializeValue(value[i]);
        }
        executer.execute(con -> {
            con.lpush(bKey, bValues);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
        });
    }

    @Override
    public Long llen(String key) {
        byte[] bKey = serializeKey(key);
        return executer.query(con -> con.llen(bKey));
    }

    @Override
    public <T> List<T> lrange(String key, Class<T> clazz, long start, long end) {
        byte[] bkey = serializeKey(key);
        List<byte[]> list = executer.query(con -> con.lrange(bkey, start, end));
        if (list == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>(list.size());
        list.forEach(data -> result.add(deserializeValue(data, clazz)));
        return result;
    }

    @Override
    public void ltrim(String key, long start, long end) {
        executer.execute(con -> con.ltrim(serializeKey(key), start, end));
    }

    @Override
    public void zincrby(String key, double score, String target, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        byte[] bTarget = serializeKey(target);
        executer.execute(con -> {
            con.zincrby(bKey, score, bTarget);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
        });
    }

    @Override
    public long zadd(String key, double score, String target, Integer expireTime) {
        byte[] bKey = serializeKey(key);
        byte[] bTarget = serializeKey(target);
        return executer.query(con -> {
            Long count = con.zadd(bKey, score, bTarget);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
            return count;
        });
    }

    @Override
    public <T> List<RankingData<T>> zrevrangeWithScores(String key, Class<T> clazz, long start, long end) {
        byte[] bKey = serializeKey(key);
        Set<Tuple> result = executer.query(con -> con.zrevrangeWithScores(bKey, start, end));
        List<RankingData<T>> list = new ArrayList<>(result.size());
        result.forEach(tuple -> list.add(new RankingData<>(deserializeValue(tuple.getBinaryElement(), clazz),
                tuple.getScore())));
        return list;
    }

    @Override
    public void zrem(String key, String... members) {
        byte[] bKey = serializeKey(key);
        byte[][] bMembers = new byte[members.length][];
        for (int i = 0; i < bMembers.length; i++) {
            bMembers[i] = serializeKey(members[i]);
        }
        executer.execute(con -> con.zrem(bKey, bMembers));
    }

    @Override
    public Long zrevrank(String key, String member) {
        byte[] bKey = serializeKey(key);
        byte[] bMember = serializeKey(member);
        return executer.query(con -> con.zrevrank(bKey, bMember));
    }

    @Override
    public Double zscore(String key, String member) {
        byte[] bKey = serializeKey(key);
        byte[] bMember = serializeKey(member);
        return executer.query(con -> con.zscore(bKey, bMember));
    }

    @Override
    public void hset(String key, Integer expireTime, String field, String value) {
        byte[] bKey = serializeKey(key);
        byte[] bField = serializeKey(field);
        byte[] bValue = serializeKey(value);
        executer.execute(con -> {
            con.hset(bKey, bField, bValue);
            if (expireTime != null) {
                con.expire(bKey, expireTime);
            }
        });
    }

    @Override
    public void hmset(String key, Integer expireTime, Map<String, String> hash) {
        byte[] bKey = serializeKey(key);
        final Map<byte[], byte[]> bHash = new HashMap<>(hash.size());
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            bHash.put(serializeKey(entry.getKey()), serializeKey(entry.getValue()));
        }
        executer.execute(con -> {
            con.hmset(bKey, bHash);
        });
    }

    @Override
    public long hdel(String key, String... fields) {
        byte[] bKey = serializeKey(key);
        if (fields.length == 0) {
            return 0;
        }
        byte[][] bFields = new byte[fields.length][];
        for (int i = 0; i < fields.length; i++) {
            bFields[i] = serializeKey(fields[i]);
        }
        return executer.query(con -> con.hdel(bKey, bFields));
    }

    @Override
    public String hget(String key, String field) {
        byte[] bKey = serializeKey(key);
        byte[] bField = serializeKey(field);
        return deserializeKey(executer.query(con -> con.hget(bKey, bField)));
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        byte[] bKey = serializeKey(key);
        if (fields.length == 0) {
            return new ArrayList<>();
        }
        byte[][] bFields = new byte[fields.length][];
        for (int i = 0; i < fields.length; i++) {
            bFields[i] = serializeKey(fields[i]);
        }
        List<byte[]> bResult = executer.query(con -> con.hmget(bKey, bFields));
        List<String> result = new ArrayList<>(bResult.size());
        for (byte[] bytes : bResult) {
            result.add(deserializeKey(bytes));
        }
        return result;
    }

    @Override
    public List<String> hvals(String key) {
        byte[] bKey = serializeKey(key);
        List<byte[]> bResult = executer.query(con -> con.hvals(bKey));
        List<String> result = new ArrayList<>(bResult.size());
        for (byte[] bytes : bResult) {
            result.add(deserializeKey(bytes));
        }
        return result;
    }

    @Override
    public Long hlen(String key) {
        byte[] bKey = serializeKey(key);
        return executer.query(con -> con.hlen(bKey));
    }

    @Override
    public Set<String> hkeys(String key) {
        byte[] bKey = serializeKey(key);
        Set<byte[]> bResult = executer.query(con -> con.hkeys(bKey));
        Set<String> result = new HashSet<>(bResult.size());
        for (byte[] bytes : bResult) {
            result.add(deserializeKey(bytes));
        }
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        byte[] bKey = serializeKey(key);
        Map<byte[], byte[]> bResult = executer.query(con -> con.hgetAll(bKey));
        Map<String, String> result = new HashMap<>(bResult.size());
        for (Map.Entry<byte[], byte[]> entry : bResult.entrySet()) {
            result.put(deserializeKey(entry.getKey()), deserializeKey(entry.getValue()));
        }
        return result;
    }

    @Override
    public boolean hexists(String key, String field) {
        byte[] bKey = serializeKey(key);
        byte[] bField = serializeKey(field);
        return executer.query(con -> con.hexists(bKey, bField));
    }

    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void init() {
        if (redisConnection == null) {
            throw new NullPointerException("redisConnection不能为空");
        }
        this.executer = redisConnection.getExecuter();
    }
}
