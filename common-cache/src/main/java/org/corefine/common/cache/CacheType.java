package org.corefine.common.cache;

/**
 * cache的类型
 *
 * @author Fe 2016年3月3日
 */
public enum CacheType {
    CONTROLLER("C"), SERVICE("S"), FRAMEWORK("F");
    private final String name;

    private CacheType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
