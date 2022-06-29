package com.msop.core.mp.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.msop.core.mp.base.BaseEntity;
import com.msop.core.mp.base.BaseServiceImpl;
import com.msop.core.mp.injector.MsSqlMethod;
import com.msop.core.mp.mapper.MsMapper;
import com.msop.core.mp.service.MsService;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

/**
 * 自定义Service 实现类
 *
 * @param <M> mapper 对象
 * @param <T> 实体对象
 * @author ruozhuliufeng
 */
public class MsServiceImpl<M extends MsMapper<T>, T extends BaseEntity>
        extends BaseServiceImpl<M, T> implements MsService<T> {


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
