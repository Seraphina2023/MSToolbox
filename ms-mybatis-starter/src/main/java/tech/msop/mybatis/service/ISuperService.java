package tech.msop.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import tech.msop.core.tool.lock.DistributedLock;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

/**
 * service接口父类
 *
 * @author ruozhuliufeng
 */
public interface ISuperService<T> extends IService<T> {
    /**
     * 幂等性新增记录
     * 例子如下：
     * String username = sysUser.getUsername();
     * boolean result = super.saveIdempotency(sysUser, lock
     *                 , LOCK_KEY_USERNAME+username
     *                 , new QueryWrapper<SysUser>().eq("username", username));
     *
     * @param entity       实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param msg          对象已存在提示信息
     * @return
     */
    boolean saveIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception;

    boolean saveIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception;

    /**
     * 幂等性新增或更新记录
     * 例子如下：
     * String username = sysUser.getUsername();
     * boolean result = super.saveOrUpdateIdempotency(sysUser, lock
     *                 , LOCK_KEY_USERNAME+username
     *                 , new QueryWrapper<SysUser>().eq("username", username));
     *
     * @param entity       实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param msg          对象已存在提示信息
     * @return
     */
    boolean saveOrUpdateIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception;

    boolean saveOrUpdateIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception;

    /**
     * 逻辑删除
     *
     * @param ids id 集合
     * @return 是否操作成功
     */
    boolean deleteLogic(@NotEmpty List<Long> ids);

    /**
     * 变更状态
     * @param ids id 集合
     * @param status 状态值
     * @return 是否操作成功
     */
    boolean changeStatus(@NotEmpty List<Long> ids,Integer status);
    /**
     * 插入如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entity entity
     * @return 是否成功
     */
    boolean saveIgnore(T entity);

    /**
     * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
     *
     * @param entity entity
     * @return 是否成功
     */
    boolean saveReplace(T entity);

    /**
     * 插入（批量）,插入如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entityList 实体对象集合
     * @param batchSize  批次大小
     * @return 是否成功
     */
    boolean saveIgnoreBatch(Collection<T> entityList, int batchSize);

    /**
     * 插入（批量）,表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
     *
     * @param entityList 实体对象集合
     * @param batchSize  批次大小
     * @return 是否成功
     */
    boolean saveReplaceBatch(Collection<T> entityList, int batchSize);

    /**
     * 插入（批量）,表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
     *
     * @param entityList 实体对象集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveReplaceBatch(Collection<T> entityList) {
        return saveReplaceBatch(entityList, 1000);
    }
}
