package org.corefine.common.cache.redis;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.corefine.common.cache.CacheException;
import org.corefine.common.cache.CacheHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Fe 2017-08-11
 */
public class SerializeUtils {
    public static final Charset UTF8_CHARSET;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        UTF8_CHARSET = Charset.forName("utf-8");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    public static byte[] serializeValue(Object object) {
        if (object == null) {
            return CacheHandler.NULL;
        } else if (object instanceof String) {
            return ((String) object).getBytes();
        } else {
            try {
                return objectMapper.writeValueAsBytes(object);
            } catch (JsonProcessingException e) {
                throw new CacheException("转为json异常", e);
            }
        }
    }

    public static byte[] serializeKey(String key) {
        return key.getBytes();
    }

    public static <T> T deserializeValue(byte[] bytes, Class<T> type) {
        if (bytes == null || Arrays.equals(CacheHandler.NULL, bytes)) {
            return null;
        } else if (type == String.class) {
            return (T) new String(bytes, UTF8_CHARSET);
        } else {
            try {
                return objectMapper.readValue(bytes, type);
            } catch (IOException e) {
                throw new CacheException("转为对象异常:" + new String(bytes, UTF8_CHARSET), e);
            }
        }
    }

    public static <T> T deserializeValue(byte[] bytes, TypeReference<T> typeReference) {
        if (bytes == null || Arrays.equals(CacheHandler.NULL, bytes)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(bytes, typeReference);
            } catch (IOException e) {
                throw new CacheException("转为对象异常:" + new String(bytes, UTF8_CHARSET), e);
            }
        }
    }

    public static String deserializeKey(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            return new String(bytes);
        }
    }

    public static byte[] serializeObject(Object object) {
        if (object == null) {
            return CacheHandler.NULL;
        }
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new CacheException("实例化失败：" + object.getClass().getName() + "未实现java.io.Serializable", e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object deserializeObject(byte[] bytes) {
        if (bytes == null || Arrays.equals(CacheHandler.NULL, bytes)) {
            return null;
        }
        ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayOutputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException("实例化失败", e);
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }
}
