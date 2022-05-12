package com.msop.core.tool.node;

import java.io.Serializable;
import java.util.List;

/**
 * 节点
 *
 * @author ruozhuliufeng
 */
public interface INode extends Serializable {
    /**
     * 主键
     *
     * @return Integer
     */
    Long getId();

    /**
     * 父主键
     *
     * @return Integer
     */
    Long getParentId();

    /**
     * 子孙节点
     *
     * @return List
     */
    List<INode> getChildren();

    /**
     * 是否有子孙节点
     *
     * @return 默认为否
     */
    default Boolean getHasChildren() {
        return false;
    }
}
