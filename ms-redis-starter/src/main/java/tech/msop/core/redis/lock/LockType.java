package tech.msop.core.redis.lock;

/**
 * 锁类型
 *
 * @author ruozhuliufeng
 */
public enum LockType {
    /**
     * 重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR
}
