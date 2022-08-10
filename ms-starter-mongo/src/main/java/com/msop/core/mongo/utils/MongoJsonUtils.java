package com.msop.core.mongo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * 处理mongo json 数据结构
 *
 * @author ruozhuliufeng
 */
public class MongoJsonUtils {

    /**
     * 获取多有的叶子节点和路径信息
     *
     * @param jsonNode jsonTree
     * @return tree 叶子信息
     */
    public static List<JsonNodeInfo> getLeafNodes(JsonNode jsonNode) {
        if (jsonNode == null || !jsonNode.isObject()) {
            return Collections.emptyList();
        }
        List<JsonNodeInfo> list = new ArrayList<>();
        // 双向的队列Deque 代替Stack，Stacke性能不佳
        LinkedList<String> deque = new LinkedList<>();
        // 递归获取叶子节点
        getLeafNodes(jsonNode, null, deque, list);
        return list;
    }

    private static void getLeafNodes(JsonNode jsonNode, JsonNode parentNode, LinkedList<String> deque, List<JsonNodeInfo> list) {
        Iterator<Map.Entry<String, JsonNode>> iterator;
        if (parentNode == null) {
            iterator = jsonNode.fields();
        } else {
            iterator = parentNode.fields();
        }
        // tree 子节点
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String fieldName = entry.getKey();
            JsonNode nextNode = entry.getValue();
            // 如果不是值节点
            if (nextNode.isObject()) {
                // 添加到队列尾，先进先出
                deque.addLast(fieldName);
                getLeafNodes(parentNode, nextNode, deque, list);
            }
            // 如果是值节点，也就是到叶子节点了，取叶子节点上级即可
            if (nextNode.isValueNode()) {
                // 封装节点列表
                LinkedList<String> elements = new LinkedList<>(deque);
                // tree 的叶子节点，此处为引用
                list.add(new JsonNodeInfo(elements, parentNode));
                break;
            }
            // 栈非空时弹出
            if (!deque.isEmpty()) {
                deque.removeLast();
            }
        }
    }

    /**
     * 构建树形节点
     *
     * @param jsonNode 父级节点
     * @param elements tree节点列表
     * @return JsonNode 叶子节点，返回用于塞数据
     */
    public static ObjectNode buildNode(ObjectNode jsonNode, List<String> elements) {
        ObjectNode newNode = jsonNode;
        for (String element : elements) {
            // 如果已经存在节点，这不生成新的
            if (newNode.has(element)) {
                newNode = (ObjectNode) newNode.get(element);
            } else {
                newNode = newNode.putObject(element);
            }
        }
        return newNode;
    }

    /**
     * 获取所有节点的值，并构建成mongodb update 语句
     *
     * @param prefix     前缀
     * @param nodeKeys   mongo keys
     * @param objectNode tree节点
     * @return tree 节点信息
     */
    public static Map<String, Object> getAllUpdate(String prefix, String nodeKeys, ObjectNode objectNode) {
        Map<String, Object> values = new HashMap<>(8);
        Iterator<String> iterator = objectNode.fieldNames();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            JsonNode valueNode = objectNode.get(fieldName);
            if (valueNode.isValueNode()) {
                Object value;
                if (valueNode.isShort()) {
                    value = valueNode.shortValue();
                } else if (valueNode.isInt()) {
                    value = valueNode.longValue();
                } else if (valueNode.isLong()) {
                    value = valueNode.longValue();
                } else if (valueNode.isBoolean()) {
                    value = valueNode.booleanValue();
                } else if (valueNode.isFloat()) {
                    value = valueNode.floatValue();
                } else if (valueNode.isDouble()) {
                    value = valueNode.doubleValue();
                } else if (valueNode.isMissingNode()) {
                    value = null;
                } else {
                    value = valueNode.textValue();
                }
                if (value != null) {
                    String valueKey = prefix + '.' + nodeKeys + '.' + fieldName;
                    values.put(valueKey, value);
                }
            }
        }
        return values;
    }
}
