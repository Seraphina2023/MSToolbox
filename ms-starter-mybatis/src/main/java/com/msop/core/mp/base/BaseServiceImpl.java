package com.msop.core.mp.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

public class BaseServiceImpl<M extends BaseMapper<T>,T extends BaseEntity> extends ServiceImpl<M,T> implements BaseService<T> {
    /**
     * 逻辑删除
     *
     * @param ids id 集合
     * @return 是否操作成功
     */
    @Override
    public boolean deleteLogic(List<Long> ids) {
        return false;
    }

    /**
     * 变更状态
     *
     * @param ids    id 集合
     * @param status 状态值
     * @return 是否操作成功
     */
    @Override
    public boolean changeStatus(List<Long> ids, Integer status) {
        return false;
    }
}
