package org.corefine.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

public class JsonUtil {
    public static final Charset UTF8_CHARSET;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        UTF8_CHARSET = Charset.forName("utf-8");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    public static <T> T toObject(InputStream in, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(in, typeReference);
        } catch (IOException e) {
            throw new JsonException("转为对象异常", e);
        }
    }

    public static <T> T toObject(InputStream in, Class<T> clazz) {
        try {
            return objectMapper.readValue(in, clazz);
        } catch (IOException e) {
            throw new JsonException("转为对象异常", e);
        }
    }

    public static Map<String, Object> toMap(InputStream in) {
        try {
            return objectMapper.readValue(in, Map.class);
        } catch (IOException e) {
            throw new JsonException("转为对象异常", e);
        }
    }

    public static String toString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(in, UTF8_CHARSET)) {
            char[] buffer = new char[8 * 1024];
            int a;
            while ((a = isr.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, a);
            }
        } catch (IOException e) {
            throw new RuntimeException("读取字符失败", e);
        }
        return sb.toString();
    }

    public static class JsonException extends RuntimeException {
        public JsonException(String message, Throwable e) {
            super(message, e);
        }
    }
}
