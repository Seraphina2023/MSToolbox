package com.msop.core.log.trace;

import io.netty.util.internal.StringUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;


/**
 * Dubbo过滤器，传递traceId
 * @author ruozhuliufeng
 * @date 2021-07-17
 * 注解： @Activate 称为自动激活扩展点注解，主要使用在有多个扩展点实现、需要同时根据不同条件被激活的场景中，如Filter需要多个同时激活，因为每个Filter实现的是不同的功能。
 * String[] group():URL中的分组如果匹配则激活<br>
 * String[] value():URL中如果包含该key值，则会激活<br>
 * String[] before():填写扩展点列表，表示哪些扩展点要在本扩展点之前激活<br>
 * String[] after():表示哪些扩展点需要在本扩展点之后激活<br>
 * int order():排序信息<br>
 */
@Activate(group = {CommonConstants.PROVIDER,CommonConstants.CONSUMER},order = MDCTraceUtils.FILTER_ORDER)
public class DubboTraceFilter implements Filter {
    /**
     * 服务消费者：传递traceId给下游服务
     * 服务提供者：获取traceId并赋值给MDC
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isProviderSide = RpcContext.getContext().isProviderSide();
        if (isProviderSide){
            // 服务提供者逻辑
            String traceId = invocation.getAttachment(MDCTraceUtils.KEY_TRACE_ID);
            String spanId = invocation.getAttachment(MDCTraceUtils.KEY_SPAN_ID);
            if (StringUtils.isEmpty(traceId)){
                MDCTraceUtils.addTraceId();
            }else {
                MDCTraceUtils.putTraceId(traceId,spanId);
            }
        }else {
            // 服务消费者逻辑
            String traceId = MDCTraceUtils.getTraceId();
            if (!StringUtils.isEmpty(traceId)){
                invocation.setAttachment(MDCTraceUtils.KEY_TRACE_ID,traceId);
                invocation.setAttachment(MDCTraceUtils.KEY_SPAN_ID,MDCTraceUtils.getNextSpanId());
            }
        }
        try {
            return invoker.invoke(invocation);
        }finally {
            if (isProviderSide){
                MDCTraceUtils.removeTraceId();
            }
        }
    }
}
