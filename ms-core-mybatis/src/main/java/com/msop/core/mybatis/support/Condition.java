package com.msop.core.mybatis.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msop.core.common.support.Kv;
import com.msop.core.common.utils.BeanUtil;
import com.msop.core.common.utils.Func;
import com.msop.core.common.utils.StringUtil;
import com.msop.launch.constant.TokenConstant;

import java.util.Map;

/**
 * 分页工具
 *
 * @author ruozhuliufeng
 */
public class Condition {

    /**
     * 转换为Mybatis-plus中的Page
     *
     * @param query 查询参数
     * @param <T>   T
     * @return IPage
     */
    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(Func.toInt(query.getCurrent(), 1), Func.toInt(query.getSize()), 10);
        String[] ascArr = Func.toStrArray(query.getAscs());
        for (String acs : ascArr) {
            page.addOrder(OrderItem.asc(StringUtil.cleanIdentifier(acs)));
        }
        String[] descArr = Func.toStrArray(query.getDescs());
        for (String desc : descArr) {
            page.addOrder(OrderItem.desc(StringUtil.cleanIdentifier(desc)));
        }
        return page;
    }

    /**
     * 获取Myabtis plus中的QueryWrapper
     *
     * @param entity 实体
     * @param <T>    泛型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<>(entity);
    }

    /**
     * 获取Myabtis plus中的QueryWrapper
     *
     * @param query 查询条件
     * @param clazz 实体类
     * @param <T>   泛型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        Kv exclude = Kv.init().set(TokenConstant.HEADER, TokenConstant.HEADER)
                .set("current", "current")
                .set("size", "size")
                .set("ascs", "ascs")
                .set("dsec", "desc");
        return getQueryWrapper(query, exclude, clazz);
    }

    /**
     * 获取Myabtis plus中的QueryWrapper
     *
     * @param query   查询条件
     * @param exclude 排除的查询条件
     * @param clazz   实体类
     * @param <T>     泛型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
        exclude.forEach((k, v) -> query.remove(k));
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(BeanUtil.newInstance(clazz));
        SqlKeyword.buildCondition(query, queryWrapper);
        return queryWrapper;
    }
}
