package com.msop.core.tenant;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tenant.annotation.TableExclude;
import com.msop.core.tenant.properties.MsTenantProperties;
import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.SpringUtil;
import com.msop.core.tool.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 租户信息处理器
 *
 * @author ruozhuliufeng
 */
@Slf4j
@RequiredArgsConstructor
public class MsTenantHandler implements TenantLineHandler, SmartInitializingSingleton {
    /**
     * 匹配的多租户表
     */
    private final List<String> tenantTableList = new ArrayList<>();

    /**
     * 需要排除进行自定义的多租户表
     */
    private final List<String> excludeTableList = Arrays.asList("msop_user", "msop_dept", "msop_role", "msop_tenant");

    /**
     * 多租户配置
     */
    private final MsTenantProperties tenantProperties;

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    @Override
    public Expression getTenantId() {
        return new StringValue(Func.toStr(AuthUtil.getTenantId(), MsConstant.ADMIN_TENANT_ID));
    }

    /**
     * 获取租户字段名称
     *
     * @return 租户字段名称
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略拼接多租户条件 true：忽略，false：需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (MsTenantHolder.isIgnore()) {
            return true;
        }
        return !(tenantTableList.contains(tableName) && StringUtil.isNotBlank(AuthUtil.getTenantId()));
    }

    @Override
    public void afterSingletonsInstantiated() {
        ApplicationContext context = SpringUtil.getContext();
        if (tenantProperties.getAnnotationExclude() && context != null) {
            Map<String, Object> tables = context.getBeansWithAnnotation(TableExclude.class);
            List<String> excludeTableList = tenantProperties.getExcludeTables();
            for (Object o : tables.values()) {
                TableExclude annotation = o.getClass().getAnnotation(TableExclude.class);
                String value = annotation.value();
            }
        }
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        tableFor:
        for (TableInfo tableInfo : tableInfos) {
            String tableName = tableInfo.getTableName();
            if (tenantProperties.getExcludeTables().contains(tableName) ||
                    excludeTableList.contains(tableName.toLowerCase()) ||
                    excludeTableList.contains(tableName.toUpperCase())) {
                continue;
            }
            List<TableFieldInfo> fieldList = tableInfo.getFieldList();
            for (TableFieldInfo fieldInfo : fieldList) {
                if (tenantProperties.getColumn().equals(fieldInfo.getColumn())) {
                    tenantTableList.add(tableName);
                    continue tableFor;
                }
            }
        }

    }
}
