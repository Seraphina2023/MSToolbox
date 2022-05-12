package com.msop.core.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.msop.core.common.constant.MsConstant;
import com.msop.core.common.utils.Func;
import com.msop.core.common.utils.ObjectUtil;
import com.msop.core.mybatis.intercept.QueryInterceptor;
import com.msop.core.mybatis.plugins.MsPaginationInterceptor;
import com.msop.core.mybatis.plugins.SqlLogInterceptor;
import com.msop.core.mybatis.properties.MybatisPlusProperties;
import com.msop.core.secure.utils.SecureUtil;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/**
 * Mybatis plus 配置项
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@MapperScan("**.mapper.**")
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusConfiguration {

    /**
     * 配置租户拦截器
     */
    @Bean
    @ConditionalOnMissingBean(TenantLineInnerInterceptor.class)
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new StringValue(Func.toStr(SecureUtil.getTenantId(), MsConstant.ADMIN_TENANT_ID));
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return true;
            }
        });
    }

    public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<QueryInterceptor[]> queryInterceptors,
                                                         TenantLineInnerInterceptor tenantLineInnerInterceptor,
                                                         MybatisPlusProperties mybatisPlusProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置租户拦截器
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        // 配置分页拦截器
        MsPaginationInterceptor paginationInterceptor = new MsPaginationInterceptor();
        // 配置自定义查询拦截器
        QueryInterceptor[] queryInterceptorArray = queryInterceptors.getIfAvailable();
        if (ObjectUtil.isNotEmpty(queryInterceptorArray)) {
            AnnotationAwareOrderComparator.sort(queryInterceptorArray);
            paginationInterceptor.setQueryInterceptors(queryInterceptorArray);
        }
        paginationInterceptor.setMaxLimit(mybatisPlusProperties.getPageLimit());
        paginationInterceptor.setOverflow(mybatisPlusProperties.getOverflow());
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }

    /**
     * SQL 日志
     *
     * @return SqlLogInterceptor
     */
    @Bean
    @ConditionalOnProperty(value = "ms.mybatis-plus.sql-log", matchIfMissing = true)
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }


}
