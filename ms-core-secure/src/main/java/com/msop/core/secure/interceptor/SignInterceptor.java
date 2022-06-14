package com.msop.core.secure.interceptor;

import com.msop.core.secure.constant.SecureConstant;
import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.properties.SignSecure;
import com.msop.core.secure.provider.HttpMethod;
import com.msop.core.secure.provider.ResponseProvider;
import com.msop.core.secure.utils.SecureUtil;
import com.msop.core.tool.jackson.JsonUtil;
import com.msop.core.tool.utils.DateUtil;
import com.msop.core.tool.utils.DigestUtil;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * 自定义授权拦截器校验
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class SignInterceptor extends HandlerInterceptorAdapter {
    /**
     * 表达式匹配
     */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    /**
     * 授权集合
     */
    private final List<SignSecure> signSecures;
    /**
     * 请求时间
     */
    private final static String TIMESTAMP = "timestamp";
    /**
     * 随机数
     */
    private final static String NONCE = "nonce";
    /**
     * 时间随机数组合加密串
     */
    private final static String SIGNATURE = "signature";
    /**
     * sha1 加密
     */
    private final static String SHA1 = "sha1";
    /**
     * MD5 加密
     */
    private final static String MD5 = "md5";
    /**
     * 时间差最小值
     */
    private final static Integer SECOND_MIN = 0;
    /**
     * 时间差最大值
     */
    private final static Integer SECOND_MAX = 10;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        boolean check = signSecures.stream()
                .filter(signSecure -> checkAuth(request, signSecure))
                .findFirst()
                .map(signSecure -> checkSign(signSecure.getCrypto()))
                .orElse(Boolean.TRUE);
        if (!check) {
            log.warn("授权认证失败,请求接口:{},请求IP:{},请求参数:{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
            response.setHeader(SecureConstant.BASIC_REALM_HEADER_KEY, SecureConstant.BASIC_REALM_HEADER_VALUE);
            ResponseProvider.write(response);
            return false;
        }
        return true;
    }

    /**
     * 检测授权
     */
    private boolean checkAuth(HttpServletRequest request, SignSecure signSecure) {
        return checkMethod(request, signSecure.getMethod()) && checkPath(request, signSecure.getPattern());
    }

    /**
     * 检测请求方法
     */
    private boolean checkMethod(HttpServletRequest request, HttpMethod method) {
        return method == HttpMethod.ALL ||
                (method != null && method == HttpMethod.of(request.getMethod()));
    }

    /**
     * 检测路径匹配
     */
    private boolean checkPath(HttpServletRequest request, String pattern) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 0) {
            servletPath = servletPath + pathInfo;
        }
        return ANT_PATH_MATCHER.match(pattern, servletPath);
    }

    /**
     * 检测表达式
     */
    private boolean checkSign(String crypto) {
        try {
            HttpServletRequest request = WebUtil.getRequest();
            if (request == null) {
                return false;
            }
            // 获取头部动态签名信息
            String timestamp = request.getHeader(TIMESTAMP);
            // 判断是否在合法时间段
            long seconds = Duration.between(new Date(Func.toLong(timestamp)).toInstant(), DateUtil.now().toInstant()).getSeconds();
            if (seconds < SECOND_MIN || seconds > SECOND_MAX) {
                log.warn("授权认证失败，错误信息：{}", "请求时间戳非法");
                return false;
            }
            String nonce = request.getHeader(NONCE);
            String signature = request.getHeader(SIGNATURE);
            // 加密签名比对，可扩展其他加密规则
            String sign;
            if (crypto.equals(MD5)) {
                sign = DigestUtil.md5Hex(timestamp + nonce);
            } else if (crypto.equals(SHA1)) {
                sign = DigestUtil.sha1Hex(timestamp + nonce);
            } else {
                sign = DigestUtil.sha1Hex(timestamp + nonce);
            }
            return sign.equalsIgnoreCase(signature);
        } catch (Exception e) {
            log.warn("授权认证失败，错误信息：{}", e.getMessage());
            return false;
        }
    }
}
