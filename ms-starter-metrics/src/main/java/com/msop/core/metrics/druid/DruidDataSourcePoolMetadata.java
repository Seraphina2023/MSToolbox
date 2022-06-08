package com.msop.core.metrics.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

/**
 * Druid连接池
 *
 * @author ruozhuliufeng
 */
public class DruidDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<DruidDataSource> {
    protected DruidDataSourcePoolMetadata(DruidDataSource dataSource) {
        super(dataSource);
    }

    /**
     * @return
     */
    @Override
    public Integer getActive() {
        return ((DruidDataSource) this.getDataSource()).getActiveCount();
    }

    /**
     * @return
     */
    @Override
    public Integer getMax() {
        return ((DruidDataSource) this.getDataSource()).getMaxActive();
    }

    /**
     * @return
     */
    @Override
    public Integer getMin() {
        return ((DruidDataSource) this.getDataSource()).getMinIdle();
    }

    /**
     * @return
     */
    @Override
    public String getValidationQuery() {
        return ((DruidDataSource) this.getDataSource()).getValidationQuery();
    }

    /**
     * @return
     */
    @Override
    public Boolean getDefaultAutoCommit() {
        return ((DruidDataSource) this.getDataSource()).isDefaultAutoCommit();
    }
}
