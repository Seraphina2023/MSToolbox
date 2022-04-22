package com.msop.core.secure.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.secure.constants.SecureConstant;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.token.CustomWebAuthenticationDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;

/**
 * 认证授权相关工具类
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class AuthUtils {
    private AuthUtils() {
        throw new IllegalStateException("Untity Class");
    }

    private static final String BASIC_ = "Basic ";

    /**
     * 获取用户信息
     *
     * @return 用户
     */
    public static MsUser getUser() {
        MsUser user = new MsUser();
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 获取请求中的token信息
        String token = extractToken(request);
        // 解析Token，获取Token信息
        JsonNode idToken = JwtUtils.decodeAndVerify(token);
        user.setUserId(Long.valueOf(idToken.get("sub").textValue()));
        user.setUsername(idToken.get("name").textValue());
        return user;
    }

    /**
     * 获取用户信息
     *
     * @param request 请求信息
     * @return 用户
     */
    public static MsUser getUser(HttpServletRequest request) {
        MsUser user = new MsUser();
        if (request == null) {
            return null;
        }
        // 获取请求中的token信息
        String token = extractToken(request);
        // 解析Token，获取Token信息
        JsonNode idToken = JwtUtils.decodeAndVerify(token);
        user.setUserId(Long.valueOf(idToken.get("sub").textValue()));
        user.setUsername(idToken.get("name").textValue());
        return user;
    }

    /**
     * 获取request(head/param)中的token
     *
     * @param request 请求
     * @return
     */
    public static String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null) {
                log.debug("参数中未携带Token!请检查是否符合OAuth2请求格式");
            }
        }
        return token;
    }

    /**
     * 解析header中的token
     *
     * @param request 请求
     * @return token
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(SecureConstant.BASIC_HEADER_KEY);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.startsWith(OAuth2AccessToken.BEARER_TYPE))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

    /**
     * 从header 请求中获取clientId:clientSecret
     *
     * @param request 请求
     * @return
     */
    public static String[] extractClient(HttpServletRequest request) {
        String header = request.getHeader(SecureConstant.BASIC_HEADER_KEY);
        if (header == null || !header.startsWith(BASIC_)) {
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空！");
        }
        return extractHeaderClient(header);
    }

    /**
     * header请求中的clientId:clientSecret
     *
     * @param header 请求参数
     * @return
     */
    public static String[] extractHeaderClient(String header) {
        byte[] base64Client = header.substring(BASIC_.length()).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Client);
        String clientStr = new String(decoded, StandardCharsets.UTF_8);
        String[] clientArr = clientStr.split(":");
        if (clientArr.length != 2) {
            throw new RuntimeException("授权信息无效！");
        }
        return clientArr;
    }

    public static String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof MsUser) {
            username = ((MsUser) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }
        return username;
    }


    /**
     * 获取登陆的帐户类型
     */
    public static String getAccountType(Authentication authentication) {
        String accountType = null;
        if (authentication != null) {
            Object details = authentication.getDetails();
            if (details != null) {
                if (details instanceof CustomWebAuthenticationDetails) {
                    CustomWebAuthenticationDetails detailsObj = (CustomWebAuthenticationDetails) details;
                    accountType = detailsObj.getAccountType();
                } else {
                    Map<String, String> detailsMap = (Map<String, String>) details;
                    if (detailsMap != null) {
                        accountType = detailsMap.get(SecureConstant.ACCOUNT_TYPE_PARAM_NAME);
                    }
                }
            }
        }
        return accountType;
    }
}
