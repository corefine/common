//package org.corefine.common.cache.redis;
//
//import redis.clients.jedis.JedisPubSub;
//
//public class L2CacheHandlerImpl extends RedisCacheHandlerImpl implements L2CacheHandler {
//    public static final String SUBSCRIBE_KEY = "L2_CACHE_SUBSCRIBE";
//    private EhCacheHandler ehCacheHandler;
//
//    public <T> T getL2(String key, Class<T> clazz) {
//        T object = ehCacheHandler.get(key);
//        if (object == null) {
//            object = get(key, clazz);
//            if (object != null) {
//                ehCacheHandler.set(key, object);
//            }
//        }object
//        return object;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <T> T cacheL2(String key, Class<T> clazz, CacheData data, Integer expireTime) {
//        T result = ehCacheHandler.get(key);
//        if (result == null) {
//            result = super.get(key, clazz);
//            if (result == null) {
//                result = (T) data.findData();
//                if (result != null) {
//                    this.set(key, result, expireTime);
//                }
//            }
//            if (result != null) {
//                ehCacheHandler.set(key, result);
//            }
//        }
//        return (T) result;
//    }
//
//    public void deleteL2(String key, String... keys) {
//        if (key != null) {
//            executer.execute(con -> con.publish(SUBSCRIBE_KEY, key));
//        }
//        for (String k : keys) {
//            executer.execute(con -> con.publish(SUBSCRIBE_KEY, k));
//        }
//        super.delete(key, keys);
//    }
//
//    public void init() {
//        super.init();
//        final JedisPubSub pubSub = new JedisPubSub() {
//            public void onMessage(String channel, String message) {
//                ehCacheHandler.delete(message);
//                LOG.debug("delete key: {}", message);
//            }
//        };
//        Thread thread = new Thread(() -> executer.execute(con -> con.subscribe(pubSub, SUBSCRIBE_KEY)));
//        thread.setName("L2_CACHE_SUBSCRIBE");
//        thread.setDaemon(true);
//        thread.start();
//        LOG.info("l2 cache is start!");
//    }
//
//    public EhCacheHandler getEhCacheHandler() {
//        return ehCacheHandler;
//    }
//
//    public void setEhCacheHandler(EhCacheHandler ehCacheHandler) {
//        this.ehCacheHandler = ehCacheHandler;
//    }
//}
