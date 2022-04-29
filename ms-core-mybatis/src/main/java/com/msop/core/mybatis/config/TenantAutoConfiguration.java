package com.msop.core.mybatis.config;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.msop.core.common.context.TenantContextHolder;
import com.msop.core.common.propertis.TenantProperties;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 多租户自动配置
 *
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfiguration {

    @Resource
    private TenantProperties tenantProperties;

    @Bean
    public TenantHandler tenantHandler() {
        return new TenantHandler() {
            /**
             * 获取租户id
             * @param select 查询
             * @return 租户id
             */
            @Override
            public Expression getTenantId(boolean select) {
                String tenant = TenantContextHolder.getTenant();
                if (tenant != null) {
                    return new StringValue(TenantContextHolder.getTenant());
                }
                return new NullValue();
            }

            /**
             * 获取租户列名
             * @return 租户列名
             */
            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            /**
             * 过滤不需要进行租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean doTableFilter(String tableName) {
                return tenantProperties.getIgnoreTables().stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
            }
        };
    }

    /**
     * 过滤不需要根据租户隔离的MappedStatement
     */
    @Bean
    public ISqlParserFilter sqlParserFilter() {
        return metaObject -> {
            MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
            return tenantProperties.getIgnoreSqls().stream().anyMatch((e) -> e.equalsIgnoreCase(ms.getId()));
        };
    }
}
