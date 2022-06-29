package com.msop.core.mp.resolver;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.ObjectUtil;
import com.msop.core.tool.utils.StringUtil;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解决Mybatis Plus page SQL注入问题
 *
 * @author ruozhuliufeng
 */
public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ORDER_ASC = "asc";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Page.class.equals(methodParameter.getParameterType());
    }

    /**
     * page 参数解析
     * @param methodParameter MethodParameter
     * @param modelAndViewContainer ModelAndViewContainer
     * @param nativeWebRequest NativeWebRequest
     * @param webDataBinderFactory WebDataBinderFactory
     * @return 检查后新的page对象
     */
    @Override
    public Object resolveArgument(@NonNull MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 分页参数 page: 0, size: 10, sort=id%2Cdesc
        String pageParam = nativeWebRequest.getParameter("page");
        String sizeParam = nativeWebRequest.getParameter("size");
        String[] sortParam = nativeWebRequest.getParameterValues("sort");
        Page<?> page = new Page<>();
        if (StringUtil.isNotBlank(pageParam)) {
            page.setCurrent(Long.parseLong(pageParam));
        }
        if (StringUtil.isNotBlank(sizeParam)) {
            page.setSize(Long.parseLong(sizeParam));
        }
        if (ObjectUtil.isEmpty(sortParam)) {
            return page;
        }
        for (String param : sortParam) {
            if (StringUtil.isBlank(param)) {
                continue;
            }
            String[] split = param.split(StringConstant.COMMA);
            // 清理字符串
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(split[0]);
            orderItem.setAsc(split[1].equals(ORDER_ASC));
            page.addOrder(orderItem);
        }

        return page;
    }

    private static boolean isOrderASC(String[] split){
        // 默认desc
        if (split.length < 2){
            return false;
        }
        return ORDER_ASC.equalsIgnoreCase(split[1]);
    }
}
