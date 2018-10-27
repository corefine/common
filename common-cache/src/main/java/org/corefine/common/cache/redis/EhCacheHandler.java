//package org.corefine.common.cache.redis;
//
//import com.fclassroom.common.cache.CacheException;
//import com.fclassroom.common.cache.CacheHandler;
//
//import org.ehcache.Cache;
//import org.ehcache.CacheManager;
//import org.ehcache.config.CacheConfiguration;
//import org.ehcache.config.builders.CacheConfigurationBuilder;
//import org.ehcache.config.builders.CacheManagerBuilder;
//import org.ehcache.config.builders.ResourcePoolsBuilder;
//import org.ehcache.config.units.EntryUnit;
//import org.ehcache.config.units.MemoryUnit;
//import org.ehcache.expiry.Duration;
//import org.ehcache.expiry.Expirations;
//
//import java.io.Serializable;
//import java.util.concurrent.TimeUnit;
//
//public class EhCacheHandler {
//    private Cache<String, Serializable> cache;
//    private Integer maxCacheCount = 1024;
//    private Integer maxCacheMemory = 200;
//
//    public void set(String key, Object value) {
//        if (value != null && !(value instanceof Serializable)) {
//            throw new CacheException(value.getClass().getName() + "未实现java.io.Serializable");
//        }
//        cache.put(key, (Serializable) value);
//    }
//
//    public <T> T get(String key) {
//        return (T) cache.get(key);
//    }
//
//    public void delete(String key) {
//        cache.remove(key);
//    }
//
//    public void init() {
//        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder
//                .newResourcePoolsBuilder()
//                .heap(maxCacheCount, EntryUnit.ENTRIES)
//                .offheap(maxCacheMemory, MemoryUnit.MB);
//        CacheConfiguration<String, Serializable> usesConfiguredInCacheConfig = CacheConfigurationBuilder
//                .newCacheConfigurationBuilder(String.class, Serializable.class, resourcePoolsBuilder)
//                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS)))
//                .build();
//        CacheManager cacheManager = CacheManagerBuilder
//                .newCacheManagerBuilder()
//                .build(true);
//        cache = cacheManager.createCache("firstCache", usesConfiguredInCacheConfig);
//        CacheHandler.LOG.info(
//                "EhCache cache is start. maxCacheCount={},maxCacheMemory={}M", maxCacheCount, maxCacheMemory);
//    }
//
//    public Integer getMaxCacheCount() {
//        return maxCacheCount;
//    }
//
//    public void setMaxCacheCount(Integer maxCacheCount) {
//        this.maxCacheCount = maxCacheCount;
//    }
//
//    public Integer getMaxCacheMemory() {
//        return maxCacheMemory;
//    }
//
//    public void setMaxCacheMemory(Integer maxCacheMemory) {
//        this.maxCacheMemory = maxCacheMemory;
//    }
//}
