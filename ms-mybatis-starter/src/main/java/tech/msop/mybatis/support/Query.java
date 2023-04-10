package tech.msop.mybatis.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页查询工具
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
     * 正排序规则
     */
    private String ascs;
    /**
     * 倒排序规则
     */
    private String descs;
}
