package com.msop.core.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 * @author ruozhuliufeng
 * @date 2021-07-19
 */
public interface DistributedLock {
    /**
     * 获取锁，如果获取不成功，则一直等待直到lock被获取
     * @param key 锁的key
     * @param leaseTime 加锁的时间，超过这个时间锁便会自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit leaseTime参数的时间单位
     * @param isFair 是否为公平锁
     * @return 锁对象
     * @throws Exception 获取锁中产生的异常
     */
    MLock lock(String key, long leaseTime, TimeUnit unit,boolean isFair) throws Exception;

    default MLock lock(String key, long leaseTime, TimeUnit unit) throws Exception{
        return this.lock(key,leaseTime,unit,false);
    }
    default MLock lock(String key, boolean isFair) throws Exception{
        return this.lock(key,-1,null,isFair);
    }
    default MLock lock(String key) throws Exception{
        return this.lock(key,-1,null,false);
    }


    /**
     * 尝试获取锁，如果锁不可用，则等待最多waitTime时间后放弃
     * @param key 锁的key
     * @param waitTime 获取锁的最大尝试时间(单位unit)
     * @param leaseTime 加锁的时间，超过这个时间锁便会自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit waitTime和leaseTime参数的时间单位
     * @param isFair 是否公平锁
     * @return 锁对象，如果获取锁失败则为null
     * @throws Exception 异常
     */
    MLock tryLock(String key,long waitTime,long leaseTime,TimeUnit unit,boolean isFair) throws Exception;

    default MLock tryLock(String key,long waitTime,long leaseTime,TimeUnit unit) throws Exception{
        return this.tryLock(key,waitTime,leaseTime,unit,false);
    }
    default MLock tryLock(String key,long waitTime,TimeUnit unit,boolean isFair) throws Exception{
        return this.tryLock(key,waitTime,-1,unit,isFair);
    }
    default MLock tryLock(String key,long waitTime,TimeUnit unit) throws Exception{
        return this.tryLock(key,waitTime,-1,unit,false);
    }
    /**
     * 释放锁
     * @param lock 锁对象
     * @throws Exception 异常
     */
    void unlock(Object lock) throws Exception;

    default void unlock(MLock lock) throws Exception{
        if (lock != null){
            this.unlock(lock.getLock());
        }
    }
}
