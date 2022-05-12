package com.msop.core.secure.auth;

import com.msop.core.secure.utils.SecureUtil;
import com.msop.core.tool.constant.RoleConstant;
import com.msop.core.tool.utils.CollectionUtil;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.StringUtil;

/**
 * 权限判断
 */
public class AuthFun {
    /**
     * 放行所有请求
     *
     * @return {boolean}
     */
    public boolean permitAll() {
        return true;
    }

    /**
     * 只有超管角色才可访问
     * @return {boolean}
     */
    public boolean denyAll(){
        return hasRole(RoleConstant.ADMIN);
    }

    /**
     * 判断是否有该角色权限
     * @param role 单角色
     * @return {boolean}
     */
    public boolean hasRole(String role){
        return hasAnyRole(role);
    }

    public boolean hasAnyRole(String... role){
        String userRole = SecureUtil.getUser().getRoleName();
        if (StringUtil.isBlank(userRole)){
            return false;
        }
        String[] roles = Func.toStrArray(userRole);
        for (String r: role){
            if (CollectionUtil.contains(roles,r)){
                return true;
            }
        }
        return false;
    }
}
