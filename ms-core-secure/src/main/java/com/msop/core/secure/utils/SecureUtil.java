package com.msop.core.secure.utils;

import com.msop.core.secure.constant.SecureConstant;
import com.msop.core.secure.exception.SecureException;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.model.TokenInfo;
import com.msop.core.secure.provider.IClientDetails;
import com.msop.core.secure.provider.IClientDetailsService;
import com.msop.core.tool.constant.RoleConstant;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.launch.constant.TokenConstant;
import com.msop.core.tool.utils.*;
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
public class SecureUtil extends AuthUtil {

    private static final IClientDetailsService clientDetailsService;

    static {
        clientDetailsService = SpringUtil.getBean(IClientDetailsService.class);
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
        Claims claims = getClims(request);
        if (claims == null) {
            return null;
        }
        String clientId = Func.toStr(claims.get(TokenConstant.CLIENT_ID));
        Long userId = Func.toLong(claims.get(TokenConstant.USER_ID));
        String tenantId = Func.toStr(claims.get(TokenConstant.TENANT_ID));
        String roleId = Func.toStr(claims.get(TokenConstant.ROLE_ID));
        String deptId = Func.toStr(claims.get(TokenConstant.DEPT_ID));
        String account = Func.toStr(claims.get(TokenConstant.ACCOUNT));
        String roleName = Func.toStr(claims.get(TokenConstant.ROLE_NAME));
        String userName = Func.toStr(claims.get(TokenConstant.USER_NAME));
        MsUser user = new MsUser();
        user.setClientId(clientId);
        user.setUserId(userId);
        user.setTenantId(tenantId);
        user.setAccount(account);
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
        return (null == user) ? StringConstant.EMPTY : user.getAccount();
    }

    /**
     * 获取用户账户
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        MsUser user = getUser(request);
        return (null == user) ? StringConstant.EMPTY : user.getAccount();
    }

    /**
     * 获取用户名
     *
     * @return userAccount
     */
    public static String getUserName() {
        MsUser user = getUser();
        return (null == user) ? StringConstant.EMPTY : user.getAccount();
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
     * 获取Claims
     *
     * @param request request
     * @return claims
     */
    public static Claims getClims(HttpServletRequest request) {
        String auth = request.getHeader(TokenConstant.HEADER);
        if (StringUtil.isNotBlank(auth) && auth.length() > TokenConstant.AUTH_LENGTH) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(TokenConstant.BEARER) == 0) {
                auth = auth.substring(7);
                return SecureUtil.parseJWT(auth);
            }
        } else {
            String parameter = request.getParameter(TokenConstant.HEADER);
            if (StringUtil.isNotBlank(parameter)) {
                return SecureUtil.parseJWT(parameter);
            }
        }
        return null;
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
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECUREITY)).build()
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建令牌
     *
     * @param user      user
     * @param audience  audience
     * @param issuer    issuer
     * @param tokenType tokenType
     * @return jwt
     */
    public static TokenInfo createJWT(Map<String, String> user, String audience, String issuer, String tokenType) {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
        IClientDetails clientDetails = clientDetails(clientId);
        // 校验客户端信息
        if (!validateClient(clientDetails, clientId, clientSecret)) {
            throw new SecureException("客户端认证失败");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECUREITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // 添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("type", "JWT")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signingKey);

        // 设置JWT参数
        user.forEach(builder::claim);

        // 设置应用ID
        builder.claim(TokenConstant.CLIENT_ID, clientId);

        // 添加Token过期时间
        long expireMillis;
        if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
            expireMillis = clientDetails.getAccessTokenValidity() * 1000;
        } else if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
            expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
        } else {
            expireMillis = getExpire();
        }
        long expMillis = nowMillis + expireMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        // 组装Token信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire((int) (expireMillis / 1000));
        return tokenInfo;
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

    @SneakyThrows
    public static String[] extractAndDecodeHeader() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        header = Func.toStr(header).replaceAll(SecureConstant.BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
        if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
            throw new SecureException("No Client Information in Request Header");
        }
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoed;
        try {
            decoed = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }
        String token = new String(decoed, Charsets.UTF_8_NAME);
        int index = token.indexOf(StringConstant.COLON);
        if (index == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取请求头中的客户端ID
     *
     * @return 客户端ID
     */
    public static String getClientIdFromHeader() {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 获取客户端信息
     *
     * @param clientId 客户端ID
     * @return clientDetails
     */
    private static IClientDetails clientDetails(String clientId) {
        return clientDetailsService.loadClientByClientId(clientId);
    }

    /**
     * 校验Client
     *
     * @param clientDetails 客户端信息
     * @param clientId      客户端ID
     * @param clientSecret  客户端密钥
     * @return Boolean
     */
    private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
        if (clientDetails != null) {
            return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
        }
        return false;
    }
}
