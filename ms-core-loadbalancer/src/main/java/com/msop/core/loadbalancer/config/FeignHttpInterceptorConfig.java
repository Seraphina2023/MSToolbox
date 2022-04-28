package com.msop.core.loadbalancer.config;

import com.msop.core.common.constant.MsConstant;
import feign.RequestInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Feign 拦截器，只包含http相关数据
 * @author ruozhuliufeng
 * @date 2021-09-18
 */
public class FeignHttpInterceptorConfig {
    protected List<String> reqeuestHeaders = new ArrayList<>();

    @PostConstruct
    public void initialize(){
        reqeuestHeaders.add(MsConstant.USER_ID_HEADER);
        reqeuestHeaders.add(MsConstant.USER_HEADER);
        reqeuestHeaders.add(MsConstant.TENANT_HEADER);
    }

    public RequestInterceptor httpFeignInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames !=null){
                    String headerName;
                    String headerValue;
                    while (headerNames.hasMoreElements()){
                        headerName = headerNames.nextElement();
                        if (reqeuestHeaders.contains(headerName)){
                            headerValue = request.getHeader(headerName);
                            requestTemplate.header(headerName,headerValue);
                        }
                    }
                }
                // 传递access_token，无网络隔离时需要传递
               /* String token = extractHeaderToken(request);
                if (StrUtil.isEmpty(token)){
                    token = request.getParameter(CommonConstant.ACCESS_TOKEN);
                }
                if (StrUtil.isNotEmpty(token)){
                    requestTemplate.header(CommonConstant.TOKEN_HEADER,CommonConstant.BEARER_TYPE +" "+token);
                }*/
            }
        };
    }

    /**
     * 解析header中的token
     * @param request 请求
     * @return token
     */
    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(MsConstant.BASIC_HEADER_KEY);
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(MsConstant.BEARER_TYPE.toLowerCase()))){
                String authHeaderValue = value.substring(MsConstant.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex>0){
                    authHeaderValue = authHeaderValue.substring(0,commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
