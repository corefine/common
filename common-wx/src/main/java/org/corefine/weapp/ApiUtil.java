package org.corefine.weapp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.corefine.common.web.service.ServiceException;
import org.corefine.weapp.result.Jscode2SessionResult;
import org.corefine.weapp.result.WeappErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
    private static final String JSCODE_2_SESSION = "https://mvc.weixin.qq.com/sns/jscode2session?" +
            "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

//        objectMapper.setDateFormat(new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'+08:00'"));
    }

    public static Jscode2SessionResult jscode2Session(String appId, String secret, String jsCode) {
        String url = String.format(JSCODE_2_SESSION, appId, secret, jsCode);
        return get(url, Jscode2SessionResult.class);
    }

    private static <T extends WeappErrorResult> T get(String uri, Class<T> responseType) {
        return request(uri, "GET", null, null, responseType);
    }

    private static <T extends WeappErrorResult> T request(String uri, String method, Map<String, String> header, Object params, Class<T> responseType) {
        logger.info(method + " " + uri);
        try {
            T result = null;
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (header != null) {
                header.forEach(connection::setRequestProperty);
            }
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            if (params != null && "PUT".equals(method) || "POST".equals(method)) {
                connection.setDoOutput(true);
                String data = objectMapper.writeValueAsString(params);
                objectMapper.writeValue(connection.getOutputStream(), params);
            }
            int status = connection.getResponseCode();
            if (status != 200) {
                throw new ServiceException("请求失败：" + status);
            }
            if (responseType == null) {
                connection.disconnect();
                return null;
            } else {
                try (InputStream in = connection.getInputStream()) {
                    result = objectMapper.readValue(in, responseType);
                } finally {
                    connection.disconnect();
                }
                if (result.getErrcode() != null) {
                    String errormsg = ErrorCodeUtil.getMessage(result.getErrcode());
                    if (errormsg == null) {
                        errormsg = "未知异常";
                    }
                    logger.error("调用微信API错误：" + result.getErrcode() + ":" + result.getErrmsg());
                    throw new ServiceException("小程序->" + result.getErrcode() + ":" + errormsg);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
