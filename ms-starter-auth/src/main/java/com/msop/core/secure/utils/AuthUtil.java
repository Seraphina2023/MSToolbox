package com.msop.core.secure.utils;

import com.msop.core.jwt.properties.JwtProperties;
import com.msop.core.launch.constant.TokenConstant;
import com.msop.core.secure.exception.SecureException;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.model.TokenInfo;
import com.msop.core.tool.constant.RoleConstant;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.SpringUtil;
import com.msop.core.tool.utils.StringUtil;
import com.msop.core.tool.utils.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

/**
 * Secure 工具类
 *
 * @author ruozhuliufeng
 */
public class AuthUtil {

    private static final String MS_USER_REQUEST_ATTR = "_MS_USER_REQUEST_ATTR_";

    private static JwtProperties jwtProperties;

    /**
     * 获取配置类
     *
     * @return jwtProperties
     */
    private static JwtProperties getJwtProperties() {
        if (jwtProperties == null) {
            jwtProperties = SpringUtil.getBean(JwtProperties.class);
        }
        return jwtProperties;
    }

    /**
     * 获取用户信息
     *
     * @return MsUser
     */
    public static MsUser getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从request中获取
        Object msUser = request.getAttribute(MS_USER_REQUEST_ATTR);
        if (msUser == null) {
            msUser = getUser(request);
            if (msUser != null) {
                // 设置到request中
                request.setAttribute(MS_USER_REQUEST_ATTR, msUser);
            }
        }
        return (MsUser) msUser;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return MsUser
     */
    public static MsUser getUser(HttpServletRequest request) {
        Claims claims = null;
        if (claims == null) {
            return null;
        }
        String clientId = Func.toStr(claims.get(TokenConstant.CLIENT_ID));
        Long userId = Func.toLong(claims.get(TokenConstant.USER_ID));
        String tenantId = Func.toStr(claims.get(TokenConstant.TENANT_ID));
        String oauthId = Func.toStr(claims.get(TokenConstant.OAUTH_ID));
        String roleId = Func.toStr(claims.get(TokenConstant.ROLE_ID));
        String deptId = Func.toStr(claims.get(TokenConstant.DEPT_ID));
        String account = Func.toStr(claims.get(TokenConstant.ACCOUNT));
        String roleName = Func.toStr(claims.get(TokenConstant.ROLE_NAME));
        String userName = Func.toStr(claims.get(TokenConstant.USER_NAME));
        MsUser user = new MsUser();
        user.setClientId(clientId);
        user.setUserId(userId);
        user.setTenantId(tenantId);
        user.setUserName(account);
        user.setRoleId(roleId);
        user.setDeptId(deptId);
        user.setRoleName(roleName);
        user.setUserName(userName);
        return user;
    }

    /**
     * 是否为超管
     *
     * @return boolean
     */
    public static boolean isAdministrator() {
        return StringUtil.containsAny(getUserRole(), RoleConstant.ADMIN);
    }

    /**
     * 获取用户ID
     *
     * @return userId
     */
    public static Long getUserId() {
        MsUser user = getUser();
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户ID
     *
     * @param request request
     * @return userId
     */
    public static Long getUserId(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户账户
     *
     * @return userAccount
     */
    public static String getUserAccount() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getUserName();
    }

    /**
     * 获取用户账户
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @return userAccount
     */
    public static String getUserName() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserName(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getUserName();
    }

    /**
     * 获取用户角色
     *
     * @return userAccount
     */
    public static String getUserRole() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getRoleName();
    }

    /**
     * 获取用户角色
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserRole(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getRoleName();
    }

    /**
     * 获取租户ID
     *
     * @return userAccount
     */
    public static String getTenantId() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getTenantId();
    }

    /**
     * 获取租户ID
     *
     * @param request request
     * @return userAccount
     */
    public static String getTenantId(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getTenantId();
    }

    /**
     * 获取客户端ID
     *
     * @return userAccount
     */
    public static String getClientId() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getClientId();
    }

    /**
     * 获取客户端ID
     *
     * @param request request
     * @return userAccount
     */
    public static String getClientId(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getClientId();
    }

    /**
     * 获取请求头
     *
     * @return header
     */
    public static String getHeader() {
        return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return header
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader(TokenConstant.HEADER);
    }
    /**
     * 获取过期时间 (次日凌晨3点)
     *
     * @return expire
     */
    public static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }
}
