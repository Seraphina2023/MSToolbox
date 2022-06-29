package com.msop.core.mp.service;

import com.msop.core.mp.base.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 自定义Service
 *
 * @param <T> 泛型
 * @author ruozhuliufeng
 */
public interface MsService<T> extends BaseService<T> {
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
