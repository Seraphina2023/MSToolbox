package com.msop.core.context.listener;

import com.msop.core.context.MsContext;
import com.msop.core.context.MsHttpHeadersGetter;
import com.msop.core.context.properties.MsContextProperties;
import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.utils.StringUtil;
import com.msop.core.tool.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet 请求监听器
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
public class MsServletRequestListener implements ServletRequestListener {
    private final MsContextProperties contextProperties;
    private final MsHttpHeadersGetter httpHeadersGetter;

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        // MDC 获取透传的变量
        MsContextProperties.Headers headers = contextProperties.getHeaders();
        String requestId = request.getHeader(headers.getRequestId());
        if (StringUtil.isNotBlank(requestId)){
            MDC.put(MsConstant.MDC_REQUEST_ID_KEY,requestId);
        }
        String accountId = request.getHeader(headers.getAccountId());
        if (StringUtil.isNotBlank(accountId)){
            MDC.put(MsConstant.MDC_ACCOUNT_ID_KEY,accountId);
        }
        String tenantId = request.getHeader(headers.getTenantId());
        if (StringUtil.isNotBlank(tenantId)){
            MDC.put(MsConstant.MDC_TENANT_ID_KEY,tenantId);
        }
        // 处理context,直接传递request，因为Spring中的尚未初始化完成
        HttpHeaders httpHeaders = httpHeadersGetter.get(request);
        ThreadLocalUtil.put(MsConstant.CONTEXT_KEY,httpHeaders);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 会话销毁时，清除上下文
        ThreadLocalUtil.clear();
        // 会话销毁时，清除MDC
        MDC.remove(MsConstant.MDC_REQUEST_ID_KEY);
        MDC.remove(MsConstant.MDC_ACCOUNT_ID_KEY);
        MDC.remove(MsConstant.MDC_TENANT_ID_KEY);
    }
}
