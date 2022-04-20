package com.msop.core.common.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /**
     * 锁的key
     */
    String key();

    /**
     * 获取锁的最大尝试时间(单位unit)，该值大于0，则使用locker.tryLock方法解锁，否则使用locker.lock方法
     */
    long waitTime() default 0;

    /**
     * 加锁的时间(单位unit),超过这个时间锁便自动解锁；如果leaseTime为-1，则保持锁定直至显示解锁
     */
    long leaseTime() default -1;

    /**
     * 参数的时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 是否公平锁
     */
    boolean isFair() default false;
}
