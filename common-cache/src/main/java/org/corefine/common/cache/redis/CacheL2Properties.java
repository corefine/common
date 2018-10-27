package org.corefine.common.cache.redis;

/**
 * cache l2 config
 */
public class CacheL2Properties {
    private Integer maxCacheCount;
    private Integer maxCacheMemory;
    private boolean enabled;

    public Integer getMaxCacheCount() {
        return maxCacheCount;
    }

    public void setMaxCacheCount(Integer maxCacheCount) {
        this.maxCacheCount = maxCacheCount;
    }

    public Integer getMaxCacheMemory() {
        return maxCacheMemory;
    }

    public void setMaxCacheMemory(Integer maxCacheMemory) {
        this.maxCacheMemory = maxCacheMemory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
