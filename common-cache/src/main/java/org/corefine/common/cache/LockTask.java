package org.corefine.common.cache;

/**
 * 加锁执行的任务
 *
 * @author litieshuan 2016年7月19日
 */
public interface LockTask<T> {

    /**
     * 工作
     */
    public T work();
}
