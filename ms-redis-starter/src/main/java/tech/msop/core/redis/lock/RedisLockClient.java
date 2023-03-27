package tech.msop.core.redis.lock;

import tech.msop.core.tool.function.CheckedSupplier;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author ruozhuliufeng
 */
public interface RedisLockClient {

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
    boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 解锁
     *
     * @param lockName 锁名
     * @param lockType 锁类型
     */
    void unLock(String lockName, LockType lockType);

    /**
     * 自动获取锁后执行方法
     *
     * @param lockName  锁名
     * @param lockType  锁类型
     * @param waitTime  等待锁超时时间
     * @param leaseTime 自动解锁时间，默认100
     * @param timeUnit  时间单位
     * @param supplier  获取锁后的回调
     * @param <T>       泛型
     * @return 返回数据
     */
    <T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier);

    /**
     * 公平锁
     *
     * @param lockName  锁名
     * @param waitTime  等待锁超时时间
     * @param leaseTime 自动解锁时间，默认100
     * @param supplier  获取锁后的回调
     * @param <T>       泛型
     * @return 返回数据
     */
    default <T> T lockFair(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
        return lock(lockName, LockType.FAIR, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
    }

    /**
     * 可重入锁
     *
     * @param lockName  锁名
     * @param waitTime  等待锁超时时间
     * @param leaseTime 自动解锁时间，默认100
     * @param supplier  获取锁后的回调
     * @param <T>       泛型
     * @return 返回数据
     */
    default <T> T lockReentrant(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
        return lock(lockName, LockType.REENTRANT, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
    }
}
