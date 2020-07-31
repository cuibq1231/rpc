package com.cbq.rpc.utils;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * json
 * Created by yangkun on 2017/5/22.
 */
public class JacksonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);
    private static volatile ObjectMapper objectMapper;

    private JacksonUtils() {
    }

    static ObjectMapper mapper() {
        if (objectMapper == null) {
            synchronized (JacksonUtils.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false);
                    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                }
            }
        }
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper();
    }

    /**
     * 解析json为map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> deserialize(String json) {
        try {
            return mapper().readValue(json, Map.class);
        } catch (IOException e) {
            LOGGER.error("deserialize json failed, json: {}", json, e);
            return null;
        }
    }

    /**
     * 解析json为map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> deserialize(byte[] json) {
        try {
            return mapper().readValue(json, Map.class);
        } catch (IOException e) {
            LOGGER.error("deserialize json failed", e);
            return null;
        }
    }

    /**
     * 解析bytes为java对象
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return mapper().readValue(bytes, clazz);
        } catch (IOException e) {
            LOGGER.error("deserialize bytes failed", e);
            return null;
        }
    }

    /**
     * 解析bytes为java对象
     */
    public static <T> T deserialize(byte[] bytes, TypeReference<T> valueTypeRef) {
        try {
            return mapper().readValue(bytes, valueTypeRef);
        } catch (IOException e) {
            LOGGER.error("deserialize bytes failed", e);
            return null;
        }
    }

    /**
     * 解析json为java对象
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return mapper().readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("deserialize json failed, json: {}", json, e);
            return null;
        }
    }

    /**
     * 解析json为java对象
     */
    public static <T> T deserialize(String json, TypeReference<T> valueTypeRef) {
        try {
            return mapper().readValue(json, valueTypeRef);
        } catch (IOException e) {
            LOGGER.error("deserialize json failed, json: {}", json, e);
            return null;
        }
    }

    /**
     * 序列化对象为json字符串
     */
    public static String serialize(Object object) {
        try {
            return mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("serialize json failed, object: {}", object, e);
            return null;
        }
    }

    /**
     * 序列化对象为json字符串
     */
    public static byte[] serialize2Bytes(Object object) {
        try {
            return mapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("serialize json failed, object: {}", object, e);
            return null;
        }
    }
}
