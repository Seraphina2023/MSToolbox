package tech.msop.core.tool.support;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 跟踪字段变动
 *
 * @author ruozhuliufeng
 */
@Getter
@ToString
public class BeanDiff {
    /**
     * 变更字段
     */
    private final Set<String> fields = new HashSet<>();
    /**
     * 旧值
     */
    private final Map<String, Object> oldValues = new HashMap<>();
    /**
     * 新值
     */
    private final Map<String, Object> newValues = new HashMap<>();
}
