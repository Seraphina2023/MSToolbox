package com.msop.core.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msop.core.common.exception.IdempotencyException;
import com.msop.core.common.exception.LockException;
import com.msop.core.common.lock.DistributedLock;
import com.msop.core.common.lock.MLock;
import com.msop.core.common.utils.StringUtil;
import com.msop.core.mybatis.service.ISuperService;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Service实现父类
 *
 * @author ruozhulifeng
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T>
        extends ServiceImpl<M, T>
        implements ISuperService<T> {
    /**
     * 幂等性新增记录
     * 例子如下：
     * String usernaem = msUser.getUsername();
     * boolean result = super.saveIdempotency(msUser,lock,
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
    @Override
    public boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception {
        if (locker == null) {
            throw new LockException("分布式锁为空");
        }
        if (StringUtil.isEmpty(lockKey)) {
            throw new LockException("锁的值为空");
        }
        try (MLock lock = locker.tryLock(lockKey, 10, 60, TimeUnit.SECONDS);) {
            if (lock != null) {
                // 判断记录是否已存在
                int count = super.count(countWrapper);
                if (count == 0) {
                    return super.save(entiy);
                } else {
                    if (StringUtil.isEmpty(message)) {
                        message = "已存在";
                    }
                    throw new IdempotencyException(message);
                }
            } else {
                throw new LockException("锁等待超时");
            }
        }
    }

    @Override
    public boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveIdempotency(entiy, locker, lockKey, countWrapper, null);
    }

    /**
     * 幂等性新增或更新记录
     * 例子如下：
     * String usernaem = msUser.getUsername();
     * boolean result = super.saveOrUpdateIdempotency(msUser,lock,
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
    @Override
    public boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception {
        if (null != entiy) {
            Class<?> cls = entiy.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && !StringUtil.isEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getFieldValue(entiy, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    if (StringUtil.isEmpty(message)) {
                        message = "已存在";
                    }
                    return this.saveIdempotency(entiy, locker, lockKey, countWrapper, message);
                } else {
                    return updateById(entiy);
                }
            } else {
                throw ExceptionUtils.mpe("错误：无法执行语句，找不到@TableId标记的主键！");
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveOrUpdateIdempotency(entiy, locker, lockKey, countWrapper, null);
    }
}
