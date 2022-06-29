package com.msop.core.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 自定义的Mapper
 *
 * @param <T> 泛型
 * @author ruozhuliufeng
 */
public interface MsMapper<T> extends BaseMapper<T> {
    /**
     * 如果数据库中已存在相同的记录，则忽略当前新数据
     *
     * @param entity 实体
     * @return 更改的条数
     */
    int insertIgnore(T entity);

    /**
     * 插入替换数据，需要表中有PrimaryKey，或者unique索引。如果表中已经你存在数据，则用新数据替换，如果没有数据，则插入
     *
     * @param entity 实体
     * @return 更改的条数
     */
    int replace(T entity);

    /**
     * 插入(批量)
     *
     * @param entityList 实体对象集合
     * @return 成功行数
     */
    int insertBatchSomeColumn(List<T> entityList);
}
