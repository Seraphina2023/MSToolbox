package tech.msop.core.redis.serializer;

import tech.msop.core.tool.utils.ObjectUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Protostuff 序列化
 *
 * @author ruozhuliufeng
 */
public class ProtostuffSerializer implements RedisSerializer<Object> {

    private final Schema<BytesWrapper> schema;

    public ProtostuffSerializer() {
        this.schema = RuntimeSchema.getSchema(BytesWrapper.class);
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return null;
        }
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtostuffIOUtil.toByteArray(new BytesWrapper<>(object), schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (ObjectUtil.isEmpty(bytes)) {
            return null;
        }
        BytesWrapper<Object> wrapper = new BytesWrapper<>();
        ProtostuffIOUtil.mergeFrom(bytes, wrapper, schema);
        return wrapper.getValue();
    }
}
