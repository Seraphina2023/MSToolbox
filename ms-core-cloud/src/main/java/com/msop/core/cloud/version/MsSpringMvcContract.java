package com.msop.core.cloud.version;

import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.convert.ConversionService;

import java.util.List;

/**
 * 支持ms-boot的版本处理
 *
 * @author ruozhuliufeng
 * @see com.msop.core.cloud.annotation.UrlVersion
 * @see com.msop.core.cloud.annotation.ApiVersion
 */
public class MsSpringMvcContract extends SpringMvcContract {
    public MsSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors, ConversionService conversionService){
        super(annotatedParameterProcessors,conversionService);
    }
}
