package com.msop.core.mybatis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户基础实体类
 *
 * @author ruozhuliufeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {
    /**
     * 租户ID
     */
    private String tenantId;
}
