package tech.msop.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import tech.msop.core.tool.constant.MsConstant;
import tech.msop.core.tool.exception.IdempotencyException;
import tech.msop.core.tool.exception.LockException;
import tech.msop.core.tool.lock.DistributedLock;
import tech.msop.core.tool.lock.LockType;
import tech.msop.core.tool.utils.*;
import tech.msop.mybatis.injector.MsSqlMethod;
import tech.msop.mybatis.mapper.SuperMapper;
import tech.msop.mybatis.model.BaseEntity;
import tech.msop.mybatis.service.ISuperService;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * service实现父类
 *
 * @author ruozhuliufeng
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements ISuperService<T> {
    @Override
    public boolean saveIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
        if (locker == null) {
            throw new LockException("DistributedLock is null");
        }
        if (StringUtil.isBlank(lockKey)) {
            throw new LockException("lockKey is null");
        }
        try {
            boolean lock = locker.tryFairLock(lockKey, 10, 60, TimeUnit.SECONDS);
            if (lock) {
                //判断记录是否已存在
                long count = super.count(countWrapper);
                if (count == 0) {
                    return super.save(entity);
                } else {
                    if (StringUtil.isBlank(msg)) {
                        msg = "已存在";
                    }
                    throw new IdempotencyException(msg);
                }
            } else {
                throw new LockException("锁等待超时");
            }
        }finally {
            // 释放锁
            locker.unLock(lockKey, LockType.FAIR);
        }
    }

    @Override
    public boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveIdempotency(entity, lock, lockKey, countWrapper, null);
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtil.isBlank(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    if (StringUtil.isBlank(msg)) {
                        msg = "已存在";
                    }
                    return this.saveIdempotency(entity, lock, lockKey, countWrapper, msg);
                } else {
                    return updateById(entity);
                }
            } else {
                throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return this.saveOrUpdateIdempotency(entity, lock, lockKey, countWrapper, null);
    }
    @Override
    public boolean save(T entity) {
        this.resolveEntity(entity);
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntity);
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean updateById(T entity) {
        this.resolveEntity(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntity);
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return this.save(entity);
        } else {
            return this.updateById(entity);
        }
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntity);
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogic(@NotEmpty List<Long> ids) {
//        MsUser user = AuthUtil.getUser();
        List<T> list = new ArrayList<>();
        ids.forEach(id -> {
            T entity = BeanUtil.newInstance(currentModelClass());
//            if (user != null) {
//                entity.setUpdateUser(user.getUserId());
//            }
            entity.setUpdateTime(DateUtil.now());
            entity.setId(id);
            list.add(entity);
        });
        return super.updateBatchById(list) && super.removeByIds(ids);
    }

    @Override
    public boolean changeStatus(@NotEmpty List<Long> ids, Integer status) {
//        MsUser user = AuthUtil.getUser();
        List<T> list = new ArrayList<>();
        ids.forEach(id -> {
            T entity = BeanUtil.newInstance(currentModelClass());
//            if (user != null) {
//                entity.setUpdateUser(user.getUserId());
//            }
            entity.setUpdateTime(DateUtil.now());
            entity.setId(id);
            entity.setStatus(status);
            list.add(entity);
        });
        return super.updateBatchById(list);
    }

    @SneakyThrows
    private void resolveEntity(T entity) {
//        MsUser user = AuthUtil.getUser();
        Date now = DateUtil.now();
        if (entity.getId() == null) {
            // 处理新增逻辑
//            if (user != null) {
//                entity.setCreateUser(user.getUserId());
//                entity.setCreateDept(Func.firstLong(user.getDeptId()));
//                entity.setUpdateUser(user.getUserId());
//            }
            if (entity.getStatus() == null) {
                entity.setStatus(MsConstant.STATUS_NORMAL);
            }
            entity.setCreateTime(now);
        }
//        else if (user != null) {
//            // 处理修改逻辑
//            entity.setUpdateUser(user.getUserId());
//        }
        // 处理通用逻辑
        entity.setUpdateTime(now);
        entity.setIsDeleted(MsConstant.DB_NOT_DELETED);
        // 处理多租户逻辑，若字段值为空，则不进行操作
        Field field = ReflectUtil.getField(entity.getClass(), MsConstant.DB_TENANT_KEY);
        if (ObjectUtil.isNotEmpty(field)) {
            Method getTenantId = ClassUtil.getMethod(entity.getClass(), MsConstant.DB_TENANT_KEY_GET_METHOD);
            String tenantId = String.valueOf(getTenantId.invoke(entity));
            if (ObjectUtil.isEmpty(tenantId)) {
                Method setTenantId = ClassUtil.getMethod(entity.getClass(), MsConstant.DB_TENANT_KEY_SET_METHOD, String.class);
                setTenantId.invoke(entity, (Object) null);
            }
        }
    }


    @Override
    public boolean saveIgnore(T entity) {
        return SqlHelper.retBool(baseMapper.insertIgnore(entity));
    }

    @Override
    public boolean saveReplace(T entity) {
        return SqlHelper.retBool(baseMapper.replace(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveIgnoreBatch(Collection<T> entityList, int batchSize) {
        return saveBatch(entityList, batchSize, MsSqlMethod.INSERT_IGNORE_ONE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveReplaceBatch(Collection<T> entityList, int batchSize) {
        return saveBatch(entityList, batchSize, MsSqlMethod.REPLACE_ONE);
    }

    private boolean saveBatch(Collection<T> entityList, int batchSize, MsSqlMethod sqlMethod) {
        String sqlStatement = msSqlStatement(sqlMethod);
        executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        return true;
    }

    /**
     * 获取 MsSqlStatement
     *
     * @param sqlMethod ignore
     * @return sql
     */
    protected String msSqlStatement(MsSqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }
}
