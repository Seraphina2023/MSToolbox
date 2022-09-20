package tech.msop.core.context;

import tech.msop.core.context.properties.MsContextProperties;
import tech.msop.core.tool.constant.MsConstant;
import tech.msop.core.tool.utils.StringUtil;
import tech.msop.core.tool.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * Ms Servlet上下文，跨线程失效
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MsServletContext implements MsContext {
    private final MsContextProperties contextProperties;
    private final MsHttpHeadersGetter httpHeadersGetter;

    @Nullable
    @Override
    public String getRequestId() {
        return get(contextProperties.getHeaders().getRequestId());
    }

    @Nullable
    @Override
    public String getAccountId() {
        return get(contextProperties.getHeaders().getAccountId());
    }

    @Nullable
    @Override
    public String getTenantId() {
        return get(contextProperties.getHeaders().getTenantId());
    }

    @Nullable
    @Override
    public String get(String ctxKey) {
        HttpHeaders headers = ThreadLocalUtil.getIfAbsent(MsConstant.CONTEXT_KEY, httpHeadersGetter::get);
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        return headers.getFirst(ctxKey);
    }

    @Nullable
    @Override
    public <T> T get(String ctxKey, Function<String, T> function) {
        String ctxValue = get(ctxKey);
        if (StringUtil.isBlank(ctxValue)) {
            return null;
        }
        return function.apply(ctxKey);
    }
}
