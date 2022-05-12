package com.msop.core.tool.convert;

import com.msop.core.tool.support.Try;
import com.msop.core.tool.utils.ClassUtil;
import com.msop.core.tool.utils.ConvertUtil;
import com.msop.core.tool.utils.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 组合Spring cglib Converter 和 Spring ConversionService
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class MsConverter implements Converter {

    private static final ConcurrentMap<String, TypeDescriptor> TYPE_CACHE = new ConcurrentHashMap<>();

    private final Class<?> targetClazz;


    /**
     * cglib convert
     *
     * @param value     源对象属性
     * @param target    目标对象属性类
     * @param fieldName 目标的field名
     * @return Object
     */
    @Nullable
    @Override
    public Object convert(Object value, Class target, Object fieldName) {
        if (value == null) {
            return null;
        }
        // 类型一样，不需要转换
        if (ClassUtil.isAssignableValue(target, value)) {
            return value;
        }
        try {
            TypeDescriptor targetTypeDescriptor = MsConverter.getTypeDescriptor(targetClazz, (String) fieldName);
            return ConvertUtil.convert(value, targetTypeDescriptor);

        } catch (Throwable e) {
            log.warn("MsConverter error", e);
            return null;
        }
    }


    private static TypeDescriptor getTypeDescriptor(final Class<?> clazz, final String fieldName) {
        String srcCacheKey = clazz.getName() + fieldName;
        return TYPE_CACHE.computeIfAbsent(srcCacheKey, Try.of(k -> {
            // 这里 property 理论上不会为null
            Field field = ReflectUtil.getField(clazz, fieldName);
            if (field == null) {
                throw new NoSuchFieldException(fieldName);
            }
            return new TypeDescriptor(field);
        }));
    }
}
