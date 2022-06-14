package com.msop.core.secure.handler;

import com.msop.core.cache.constant.CacheConstant;
import com.msop.core.cache.utils.CacheUtil;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.msop.core.secure.constant.PermissionConstant.permissionAllStatement;

/**
 * 默认授权校验类
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsPermissionHandler implements IPermissionHandler{

    private static final String SCOPE_CACHE_CODE = "apiScope:code:";

    private final JdbcTemplate jdbcTemplate;

    /**
     * 判断角色是否具有接口权限
     *
     * @return boolean
     */
    @Override
    public boolean permissionAll() {
        HttpServletRequest request = WebUtil.getRequest();
        MsUser user = AuthUtil.getUser();
        if (request == null || user == null){
            return false;
        }
        String uri = request.getRequestURI();
        List<String> paths = permissionPath(user.getRoleId());
        if (paths.size() == 0){
            return false;
        }
        return paths.stream().anyMatch(uri::contains);
    }



    /**
     * 判断角色是否具有接口权限
     *
     * @param permission 权限编号
     * @return boolean
     */
    @Override
    public boolean hasPermission(String permission) {
        HttpServletRequest request = WebUtil.getRequest();
        MsUser user = AuthUtil.getUser();
        if (request == null || user == null){
            return false;
        }
        List<String> codes = permissionCode(permission,user.getRoleId());
        return codes.size() != 0;
    }

    /**
     * 获取接口权限地址
     * @param roleId 角色id
     * @return permissions
     */
    private List<String> permissionPath(String roleId) {
        List<String> permissions = CacheUtil.get(CacheConstant.SYS_CACHE,SCOPE_CACHE_CODE,roleId,List.class,Boolean.FALSE);
        if (permissions == null){
            List<Long> roleIds = Func.toLongList(roleId);
            permissions = jdbcTemplate.queryForList(permissionAllStatement(roleIds.size()),roleIds.toArray(),String.class);
            CacheUtil.put(CacheConstant.SYS_CACHE,SCOPE_CACHE_CODE,roleId,permissions, Boolean.FALSE);
        }
        return permissions;
    }

    /**
     * 获取接口权限信息
     * @param permission 权限编号
     * @param roleId 角色ID
     * @return permissions
     */
    private List<String> permissionCode(String permission, String roleId) {
        List<String> permissions = CacheUtil.get(CacheConstant.SYS_CACHE,SCOPE_CACHE_CODE,permission + StringConstant.COLON + roleId,List.class,Boolean.FALSE);
        if (permissions == null){
            List<Object> args = new ArrayList<>(Collections.singletonList(permission));
            List<Long> roleIds = Func.toLongList(roleId);
            args.addAll(roleIds);
            permissions = jdbcTemplate.queryForList(permissionAllStatement(roleIds.size()),roleIds.toArray(),String.class);
            CacheUtil.put(CacheConstant.SYS_CACHE,SCOPE_CACHE_CODE,permission + StringConstant.COLON + roleId,List.class,Boolean.FALSE);
        }
        return permissions;
    }
}
