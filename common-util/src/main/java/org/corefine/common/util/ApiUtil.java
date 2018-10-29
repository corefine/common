package org.corefine.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class ApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString("");

            }
        });
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        //mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);
        //mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private static boolean hasRequestBody(String method) {
        return HttpPost.METHOD_NAME.equals(method) || HttpPut.METHOD_NAME.equals(method) || HttpPatch.METHOD_NAME.equals(method);
    }

    public static <T> T requestToJsonObject(String method, String uri, Map<String, String> headers, ApiRequest apiRequest,
                                            TypeReference<T> typeReference) {
        String data = requestToString(method, uri, headers, apiRequest);
        try {
            return mapper.readValue(data, typeReference);
        } catch (IOException e) {
            throw new HttpException(uri + "返回了一个错误的json", e);
        }
    }

    public static <T> T requestToJsonObject(String method, String uri, Map<String, String> headers, ApiRequest apiRequest, Class<T> clazz) {
        String data = requestToString(method, uri, headers, apiRequest);
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new HttpException(uri + "返回了一个错误的json", e);
        }
    }

    public static Map<String, Object> requestToJsonMap(String method, String uri, Map<String, String> headers, ApiRequest apiRequest) {
        String data = requestToString(method, uri, headers, apiRequest);
        try {
            return mapper.readValue(data, Map.class);
        } catch (IOException e) {
            throw new HttpException(uri + "返回了一个错误的json", e);
        }
    }

    public static String requestToString(String method, String uri, Map<String, String> headers, ApiRequest apiRequest) {
        return request(method, uri, headers, apiRequest, (d) -> d);
    }

    public static <T> T request(String method, String uri, Map<String, String> headers,
                                ApiRequest apiRequest, ApiResponse<T> apiResponse) {
        HttpUriRequestImpl httpRequest = new HttpUriRequestImpl(method);
        if (headers != null) {
            headers.forEach(httpRequest::setHeader);
        }
        if (apiRequest != null && hasRequestBody(method)) {
            httpRequest.setEntity(apiRequest.getEntity());
        }
        int status;
        String content = null;
        if (logger.isDebugEnabled()) {
            logger.debug("request: {}", uri);
        }
        try (CloseableHttpResponse httpResponse = httpclient.execute(httpRequest)) {
            status = httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    content = sb.toString();
                }
            }
        } catch (IOException e) {
            String message = "request error: " + uri;
            logger.error(message, e);
            throw new HttpException(message, e);
        }
        if (status == 200) {
            return apiResponse.parseData(content);
        } else {
            throw new HttpException("response code is " + status, null);
        }
    }

    public interface ApiRequest {
        HttpEntity getEntity();
    }

    public interface ApiResponse<T> {
        T parseData(String data);
    }

    public static class HttpUriRequestImpl extends HttpEntityEnclosingRequestBase implements HttpUriRequest {
        private String method;

        public HttpUriRequestImpl(String method) {
            this.method = method;
        }

        @Override
        public String getMethod() {
            return method;
        }
    }

    public static class HttpException extends RuntimeException {
        public HttpException(String message, Exception e) {
            super(message, e);
        }
    }
}
