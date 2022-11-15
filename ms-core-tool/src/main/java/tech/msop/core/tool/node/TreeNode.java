package tech.msop.core.tool.node;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.msop.core.tool.utils.Func;

import java.util.Objects;

/**
 * 树形节点类
 */
@Data
public class TreeNode extends BaseNode<TreeNode> {
    private static final long serialVersionUID = 1L;

    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long key;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if(object == null){
            return false;
        }
        TreeNode other = (TreeNode) object;
        return Func.equals(this.getId(),other.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,parentId);
    }
}
