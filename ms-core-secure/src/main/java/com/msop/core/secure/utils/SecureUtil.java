package com.msop.core.secure.utils;

import com.msop.core.jwt.properties.JwtProperties;
import com.msop.core.jwt.utils.JwtUtil;
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

    private static IClientDetailsService clientDetailsService;
    private static JwtProperties jwtProperties;

    /**
     * 获取客户端服务类
     *
     * @return clientDetailsService
     */
    private static IClientDetailsService getClientDetailsService() {
        if (clientDetailsService == null) {
            clientDetailsService = SpringUtil.getBean(IClientDetailsService.class);
        }
        return clientDetailsService;
    }

    /**
     * 获取Jwt配置类
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
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
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
        // Token 状态配置，仅在生成AccessToken时执行
        if (getJwtProperties().getState() && TokenConstant.ACCESS_TOKEN.equals(tokenType)) {
            String tenantId = String.valueOf(user.get(TokenConstant.TENANT_ID));
            String userId = String.valueOf(user.get(TokenConstant.USER_ID));
            JwtUtil.addAccessToken(tenantId, userId, tokenInfo.getToken(), tokenInfo.getExpire());
        }
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
        return getClientDetailsService().loadClientByClientId(clientId);
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
