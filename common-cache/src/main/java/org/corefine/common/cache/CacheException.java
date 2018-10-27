package org.corefine.common.cache;

/**
 * 缓存异常
 *
 * @author Fe
 * @version 2014-6-2 <strong>1.0</strong>
 */
public class CacheException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -6141519604065333480L;

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable e) {
        super(e);
    }

    public CacheException(String message, Throwable e) {
        super(message, e);
    }
}
