package com.msop.core.redis.lock;

import com.msop.core.tool.function.CheckedSupplier;
import com.msop.core.tool.utils.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author ruozhuliufeng
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLockClientImpl implements RedisLockClient {
    private final RedissonClient redissonClient;

    /**
     * 尝试获取锁
     *
     * @param lockName  锁名
     * @param lockType  锁类型
     * @param waitTime  等待时间
     * @param leaseTime 自动解锁时间，一定要大于方法执行时间
     * @param timeUnit  时间单位
     * @return 是否成功
     * @throws InterruptedException InterruptedException
     */
    @Override
    public boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = getLock(lockName, lockType);
        return lock.tryLock(waitTime, leaseTime, timeUnit);
    }

    /**
     * 解锁
     *
     * @param lockName 锁名
     * @param lockType 锁类型
     */
    @Override
    public void unLock(String lockName, LockType lockType) {
        RLock lock = getLock(lockName, lockType);
        // 仅仅在已经锁定和当前线程持有锁时解锁
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    private RLock getLock(String lockName, LockType lockType) {
        RLock lock;
        if (LockType.REENTRANT == lockType) {
            lock = redissonClient.getLock(lockName);
        } else {
            lock = redissonClient.getFairLock(lockName);
        }
        return lock;
    }

    /**
     * 自动获取锁后执行方法
     *
     * @param lockName  锁名
     * @param lockType  锁类型
     * @param waitTime  等待锁超时时间
     * @param leaseTime 自动解锁时间，默认100
     * @param timeUnit  时间单位
     * @param supplier  获取锁后的回调
     * @return 返回数据
     */
    @Override
    public <T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier) {
        try {
            boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
            if (result) {
                return supplier.get();
            }
        } catch (Throwable throwable) {
            throw Exceptions.unchecked(throwable);
        } finally {
            this.unLock(lockName, lockType);
        }
        return null;
    }
}
