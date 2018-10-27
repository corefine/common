package org.corefine.common.cache;

/**
 * 二级缓存支持
 *
 * @author Fe
 * @version 2017年8月20日
 */
public interface L2CacheHandler extends CacheHandler {

    <T> T getL2(String key, Class<T> clazz);

    default <T> T cacheL2(String key, Class<T> clazz, CacheData data) {
        return cacheL2(key, clazz, data, defaultCacheTime);
    }

    <T> T cacheL2(String key, Class<T> clazz, CacheData data, Integer expireTime);

    void deleteL2(String key, String... keys);
}
