package com.msop.core.secure.model;

import com.msop.core.tool.support.Kv;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 *
 * @author ruozhuliufeng
 */
@Data
public class MsUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 客户端id
     */
    @ApiModelProperty(hidden = true)
    private String clientId;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;
    /**
     * 租户ID
     */
    @ApiModelProperty(hidden = true)
    private String tenantId;
    /**
     * 部门id
     */
    @ApiModelProperty(hidden = true)
    private String deptId;
    /**
     * 用户名
     */
    @ApiModelProperty(hidden = true)
    private String userName;
    /**
     * 用户昵称
     */
    @ApiModelProperty(hidden = true)
    private String nickName;
    /**
     * 角色id
     */
    @ApiModelProperty(hidden = true)
    private String roleId;
    /**
     * 角色名
     */
    @ApiModelProperty(hidden = true)
    private String roleName;

    /**
     * 用户详情
     */
    private Kv detail;
}
