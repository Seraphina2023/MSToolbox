package tech.msop.core.log.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import tech.msop.core.tool.jackson.JsonUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 更改html 请求异常为ajax
 *
 * @author ruozhuliufeng
 */
public class MsErrorController extends BasicErrorController {
    public MsErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        boolean includeStackTrace = isIncludeStackTrace(request,MediaType.ALL);
        Map<String,Object> body = getErrorAttributes(request, (includeStackTrace)? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE):ErrorAttributeOptions.defaults());
        HttpStatus status = getStatus(request);
        response.setStatus(status.value());
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setObjectMapper(JsonUtil.getInstance());
        view.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return new ModelAndView(view, body);
    }
}
