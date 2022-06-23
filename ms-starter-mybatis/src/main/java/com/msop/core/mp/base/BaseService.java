package com.msop.core.mp.base;

import com.baomidou.mybatisplus.extension.service.IService;
import javax.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 基础业务接口
 *
 * @param <T> T
 * @author ruozhuliufeng
 */
public interface BaseService<T> extends IService<T> {
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
}
