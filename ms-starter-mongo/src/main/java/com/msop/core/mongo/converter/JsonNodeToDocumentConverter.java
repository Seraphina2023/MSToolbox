package com.msop.core.mongo.converter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public enum JsonNodeToDocumentConverter implements Converter<ObjectNode, Document> {
    /**
     * 实例
     */
    INSTANCE;

    @Override
    public Document convert(@Nullable ObjectNode source) {
        return source == null ? null : Document.parse(source.toString());
    }
}
