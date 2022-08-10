package com.msop.core.mongo.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.msop.core.tool.jackson.JsonUtil;
import org.bson.BasicBSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;

/**
 * mongo DBObject 转 jsonNode
 *
 * @author ruozhuliufeng
 */
@ReadingConverter
public enum DBObjectToJsonNodeConverter implements Converter<BasicBSONObject, JsonNode> {
    /**
     * 实例
     */
    INSTANCE;

    @Override
    public JsonNode convert(@Nullable BasicBSONObject source) {
        if (source == null){
            return null;
        }
        return JsonUtil.getInstance().valueToTree(source);
    }
}
