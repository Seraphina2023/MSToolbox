package com.msop.core.mybatis.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页工具
 *
 * @author ruozhuliufeng
 */
@Data
@Accessors(chain = true)
public class Query {
    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页的数量
     */
    private Integer size;

    /**
     * 排序的字段名
     */
    private String ascs;
    /**
     * 排序方式
     */
    private String descs;
}
