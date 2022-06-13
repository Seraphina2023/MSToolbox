package com.msop.core.secure.constant;

import com.msop.core.tool.utils.StringUtil;

/**
 * 权限校验常量
 *
 * @author ruozhuliufeng
 */
public interface PermissionConstant {
    /**
     * 获取角色所有的权限编号
     *
     * @param size 数量
     * @return string
     */
    static String permissionAllStatement(int size) {
        return "select resource_path as path from msop_scope_api where id in (select scope_id from msop_role_scope where scope_category = 2 and role_id in (" + buildHolder(size) + "))";
    }

    /**
     * 获取角色指定的权限编号
     *
     * @param size 数量
     * @return string
     */
    static String permissionStatement(int size) {
        return "select resource_code as code from msop_scope_api where resource__code = ? and id in (select scope_id from msop_role_scope where scope_category = 2 and role_id in (" + buildHolder(size) + "))";
    }

    /**
     * 获取SQL 占位符
     *
     * @param size 数量
     * @return String
     */
    static String buildHolder(int size) {
        StringBuilder builder = StringUtil.builder();
        for (int i = 0; i < size; i++) {
            builder.append("?,");
        }
        return StringUtil.removeSuffix(builder.toString(), ",");
    }

}
