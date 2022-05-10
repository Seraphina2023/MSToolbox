package com.msop.core.common.request;

import com.msop.core.common.utils.WebUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 全局Request封装
 *
 * @author ruozhuliufeng
 */
public class MsHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 没被包装过的HttpServletRequest(特殊场景，需自己过滤)
     */
    private final HttpServletRequest orgRequest;
    /**
     * 缓存根文，支持多次读取流
     */
    private byte[] body;

    public MsHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (super.getHeader(HttpHeaders.CONTENT_TYPE) == null) {
            return super.getInputStream();
        }
        if (super.getHeader(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            return super.getInputStream();
        }
        if (body == null){
            body = WebUtil.getRequestBody(super.getInputStream()).getBytes(StandardCharsets.UTF_8);
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * 获取初始request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取初始request
     *
     * @param request request
     * @return HttpServletRequest
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof MsHttpServletRequestWrapper) {
            return ((MsHttpServletRequestWrapper) request).getOrgRequest();
        }
        return request;
    }
}
