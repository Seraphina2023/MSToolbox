package com.msop.core.mp.config;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.msop.core.launch.properties.MsPropertySource;
import com.msop.core.mp.injector.MsSqlInjector;
import com.msop.core.mp.intercept.QueryInterceptor;
import com.msop.core.mp.plugins.MsPaginationInterceptor;
import com.msop.core.mp.plugins.SqlLogInterceptor;
import com.msop.core.mp.properties.MybatisPlusProperties;
import com.msop.core.mp.resolver.PageArgumentResolver;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.ObjectUtil;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mybatis-plus 配置
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@MapperScan("com.msop.**.mapper.**")
@EnableConfigurationProperties(MybatisPlusProperties.class)
@MsPropertySource(value = "classpath:/ms-mybatis.yml")
public class MybatisPlusConfiguration implements WebMvcConfigurer {

	/**
	 * 租户拦截器
	 */
	@Bean
	@ConditionalOnMissingBean(TenantLineInnerInterceptor.class)
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		return new TenantLineInnerInterceptor(new TenantLineHandler() {
			@Override
			public Expression getTenantId() {
				return new StringValue(Func.toStr(AuthUtil.getTenantId(), MsConstant.ADMIN_TENANT_ID));
			}

			@Override
			public boolean ignoreTable(String tableName) {
				return true;
			}
		});
	}

	/**
	 * mybatis-plus 拦截器集合
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<QueryInterceptor[]> queryInterceptors,
														 TenantLineInnerInterceptor tenantLineInnerInterceptor,
														 MybatisPlusProperties mybatisPlusProperties) {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 配置租户拦截器
		if (mybatisPlusProperties.getTenantMode()) {
			interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		}
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
		paginationInterceptor.setOptimizeJoin(mybatisPlusProperties.getOptimizeJoin());
		interceptor.addInnerInterceptor(paginationInterceptor);
		return interceptor;
	}

	/**
	 * sql 日志
	 */
	@Bean
	public SqlLogInterceptor sqlLogInterceptor(MybatisPlusProperties mybatisPlusProperties) {
		return new SqlLogInterceptor(mybatisPlusProperties);
	}

	/**
	 * sql 注入
	 */
	@Bean
	@ConditionalOnMissingBean(ISqlInjector.class)
	public ISqlInjector sqlInjector() {
		return new MsSqlInjector();
	}

	/**
	 * page 解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageArgumentResolver());
	}

}

