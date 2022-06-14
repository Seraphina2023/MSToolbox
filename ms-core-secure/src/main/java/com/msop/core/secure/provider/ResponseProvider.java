package com.msop.core.secure.provider;

import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.jackson.JsonUtil;
import com.msop.core.tool.model.CodeEnum;
import com.msop.core.tool.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * ResponseProvider
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class ResponseProvider {

    public static void write(HttpServletResponse response) {
        Result result = Result.failed(CodeEnum.UN_AUTHORIZED);
        response.setCharacterEncoding(MsConstant.UTF_8);
        response.addHeader(MsConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
