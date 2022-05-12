package com.msop.core.mybatis.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msop.core.mybatis.model.BaseEntity;
import com.msop.core.mybatis.service.ISuperService;

/**
 * 业务封装基础列
 *
 * @param <M> mapper
 * @param <T> model
 * @author ruozhuliufeng
 */
public class ISuperServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T>
        implements ISuperService<T> {
}
