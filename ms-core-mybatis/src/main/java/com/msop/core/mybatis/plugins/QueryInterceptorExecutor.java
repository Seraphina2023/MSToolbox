package com.msop.core.mybatis.plugins;

import com.msop.core.common.utils.ObjectUtil;
import com.msop.core.mybatis.intercept.QueryInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 查询拦截器 执行器
 * 方便后期同步更新
 *
 * @author ruozhuliufeng
 */
@SuppressWarnings({"rawtypes"})
public class QueryInterceptorExecutor {
    /**
     * 执行查询拦截器
     */
    static void exec(QueryInterceptor[] interceptors, Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql){
        if (ObjectUtil.isEmpty(interceptors)){
            return;
        }
        for (QueryInterceptor interceptor:interceptors){
            interceptor.intercept(executor,ms,parameter,rowBounds,resultHandler,boundSql);
        }
    }
}
