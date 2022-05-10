package com.msop.core.common.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.msop.core.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;

import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class StringToEnumConverter implements ConditionalGenericConverter {
    /**
     * 缓存Enum 类信息，提高性能
     */
    private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(8);

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T valueOf(Class<?> clazz, String value) {
        return Enum.valueOf((Class<T>) clazz, value);
    }

    @Nullable
    private static Object invoke(Class<?> clazz,
                                 AccessibleObject accessibleObject,
                                 String value)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (accessibleObject instanceof Constructor) {
            Constructor constructor = (Constructor) accessibleObject;
            Class<?> paramType = constructor.getParameterTypes()[0];
            // 类型转换
            Object object = ConvertUtil.convert(value, paramType);
            return constructor.newInstance(object);
        }
        if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            Class<?> paramType = method.getParameterTypes()[0];
            // 类型转换
            Object object = ConvertUtil.convert(value, paramType);
            return method.invoke(clazz, object);
        }
        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return true;
    }

    @Override
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Enum.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Class<?> sourceClazz = sourceType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(sourceClazz, StringToEnumConverter::getAnnotation);
        Class<?> targetClazz = targetType.getType();
        // 如果为null，走默认的转换
        if (accessibleObject == null) {
            if (String.class == targetClazz) {
                return ((Enum) source).name();
            }
            int ordinal = ((Enum) source).ordinal();
            return ConvertUtil.convert(ordinal, targetClazz);
        }
        try {
            return EnumToStringConverter.invoke(sourceClazz, accessibleObject, source, targetClazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
