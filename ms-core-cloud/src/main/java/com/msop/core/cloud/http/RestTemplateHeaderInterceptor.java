package com.msop.core.cloud.http;

import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.utils.ThreadLocalUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {
    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = ThreadLocalUtil.get(MsConstant.CONTEXT_KEY);
        if (headers != null && !headers.isEmpty()) {
            HttpHeaders httpHeaders = request.getHeaders();
            headers.forEach((key, values) -> values.forEach(value -> httpHeaders.add(key, value)));
        }
        return execution.execute(request, body);
    }
}
