package tech.msop.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import tech.msop.core.tool.utils.ObjectUtil;
import tech.msop.mybatis.intercept.QueryInterceptor;

/**
 * 查询拦截器执行器
 *
 * <p>
 * 目的：抽取此方法是为了后期方便同步更新 {@link MsPaginationInterceptor}
 * </p>
 *
 * @author ruozhuliufeng
 */
@SuppressWarnings({"rawtypes"})
public class QueryInterceptorExecutor {

    /**
     * 执行查询拦截器
     */
    static void exec(QueryInterceptor[] interceptors, Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws Throwable {
        if (ObjectUtil.isEmpty(interceptors)) {
            return;
        }
        for (QueryInterceptor interceptor : interceptors) {
            interceptor.intercept(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        }
    }

}
