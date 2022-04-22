package com.msop.core.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.msop.core.common.lock.DistributedLock;

/**
 * Service接口父类
 *
 * @author ruozhuliufeng
 */
public interface ISuperService<T> extends IService<T> {
    /**
     * 幂等性新增记录
     * 例子如下：
     * String usernaem = sysUser.getUsername();
     * boolean result = super.saveIdempotency(sysUser,lock,
     * LOCKE_KEY_USERNAME+username,
     * new QueryWrapper<SysUser>().eq("username",username));
     *
     * @param entiy        实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param message      对象已存在提示信息
     * @return boolean
     * @throws Exception
     */
    boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception;

    boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception;

    /**
     * 幂等性新增或更新记录
     * 例子如下：
     * String usernaem = sysUser.getUsername();
     * boolean result = super.saveOrUpdateIdempotency(sysUser,lock,
     * LOCKE_KEY_USERNAME+username,
     * new QueryWrapper<SysUser>().eq("username",username));
     *
     * @param entiy        实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param message      对象已存在提示信息
     * @return boolean
     * @throws Exception
     */
    boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception;

    boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception;
}
