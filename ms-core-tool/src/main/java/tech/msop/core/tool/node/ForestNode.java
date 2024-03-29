package tech.msop.core.tool.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 森林节点类
 *
 * @author ruozhuliufeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForestNode extends BaseNode<ForestNode> {
    private static final long serialVersionUID = 1L;

    /**
     * 节点内容
     */
    private Object content;

    public ForestNode(Long id,Long parentId,Object content){
        this.id = id;
        this.parentId = parentId;
        this.content = content;
    }
}
