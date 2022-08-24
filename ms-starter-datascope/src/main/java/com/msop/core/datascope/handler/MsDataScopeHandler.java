package com.msop.core.datascope.handler;

import com.msop.core.datascope.enums.DataScopeEnum;
import com.msop.core.datascope.model.DataScopeModel;
import com.msop.core.secure.model.MsUser;
import com.msop.core.tool.constant.RoleConstant;
import com.msop.core.tool.utils.BeanUtil;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.PlaceholderUtil;
import com.msop.core.tool.utils.StringUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 默认数据权限规则
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
public class MsDataScopeHandler implements DataScopeHandler{

    private final ScopeModelHandler scopeModelHandler;

    @Override
    public String sqlCondition(String mapperId, DataScopeModel dataScope, MsUser msUser, String originalSql) {

        //数据权限资源编号
        String code = dataScope.getResourceCode();

        //根据mapperId从数据库中获取对应模型
        DataScopeModel dataScopeDb = scopeModelHandler.getDataScopeByMapper(mapperId, msUser.getRoleId());

        //mapperId配置未取到则从数据库中根据资源编号获取
        if (dataScopeDb == null && StringUtil.isNotBlank(code)) {
            dataScopeDb = scopeModelHandler.getDataScopeByCode(code);
        }

        //未从数据库找到对应配置则采用默认
        dataScope = (dataScopeDb != null) ? dataScopeDb : dataScope;

        //判断数据权限类型并组装对应Sql
        Integer scopeRule = Objects.requireNonNull(dataScope).getScopeType();
        DataScopeEnum scopeTypeEnum = DataScopeEnum.of(scopeRule);
        List<Long> ids = new ArrayList<>();
        String whereSql = "where scope.{} in ({})";
        if (DataScopeEnum.ALL == scopeTypeEnum || StringUtil.containsAny(msUser.getRoleName(), RoleConstant.ADMIN)) {
            return null;
        } else if (DataScopeEnum.CUSTOM == scopeTypeEnum) {
            whereSql = PlaceholderUtil.getDefaultResolver().resolveByMap(dataScope.getScopeValue(), BeanUtil.toMap(msUser));
        } else if (DataScopeEnum.OWN == scopeTypeEnum) {
            ids.add(msUser.getUserId());
        } else if (DataScopeEnum.OWN_DEPT == scopeTypeEnum) {
            ids.addAll(Func.toLongList(msUser.getDeptId()));
        } else if (DataScopeEnum.OWN_DEPT_CHILD == scopeTypeEnum) {
            List<Long> deptIds = Func.toLongList(msUser.getDeptId());
            ids.addAll(deptIds);
            deptIds.forEach(deptId -> {
                List<Long> deptIdList = scopeModelHandler.getDeptAncestors(deptId);
                ids.addAll(deptIdList);
            });
        }
        return StringUtil.format("select {} from ({}) scope " + whereSql, Func.toStr(dataScope.getScopeField(), "*"), originalSql, dataScope.getScopeColumn(), StringUtil.join(ids));
    }
}
