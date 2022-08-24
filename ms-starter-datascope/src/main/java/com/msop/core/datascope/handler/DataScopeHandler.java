package com.msop.core.datascope.handler;

import com.msop.core.datascope.model.DataScopeModel;
import com.msop.core.secure.model.MsUser;

/**
 * 数据权限规则
 *
 * @author ruozhuliufeng
 */
public interface DataScopeHandler {

    /**
     * 获取过滤sql
     *
     * @param mapperId    数据查询类
     * @param dataScope   数据权限类
     * @param msUser      当前用户信息
     * @param originalSql 原始Sql
     * @return sql
     */
    String sqlCondition(String mapperId, DataScopeModel dataScope, MsUser msUser, String originalSql);

}
