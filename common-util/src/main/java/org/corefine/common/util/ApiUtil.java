package org.corefine.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T postFormToJsonObject(String uri, Map<String, String> params, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, null, getFormApiRequest(params), typeReference);
    }

    public static <T> T postFormToJsonObject(String uri, Map<String, String> params, Class<T> clazz) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, null, getFormApiRequest(params), clazz);
    }

    public static Map<String, Object> postFormToJsonMap(String uri, Map<String, String> params) {
        return requestToJsonMap(HttpPost.METHOD_NAME, uri, null, getFormApiRequest(params));
    }

    public static String postFormToString(String uri, Map<String, String> params) {
        return requestToString(HttpPost.METHOD_NAME, uri, null, getFormApiRequest(params));
    }

    public static <T> T postFormToJsonObject(String uri, Map<String, String> headers, Map<String, String> params, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, headers, getFormApiRequest(params), typeReference);
    }

    public static <T> T postFormToJsonObject(String uri, Map<String, String> headers, Map<String, String> params, Class<T> clazz) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, headers, getFormApiRequest(params), clazz);
    }

    public static Map<String, Object> postFormToJsonMap(String uri, Map<String, String> headers, Map<String, String> params) {
        return requestToJsonMap(HttpPost.METHOD_NAME, uri, headers, getFormApiRequest(params));
    }

    public static String postFormToString(String uri, Map<String, String> headers, Map<String, String> params) {
        return requestToString(HttpPost.METHOD_NAME, uri, headers, getFormApiRequest(params));
    }

    public static <T> T postToJsonObject(String uri, Object params, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, null, getJsonApiRequest(params), typeReference);
    }

    public static <T> T postToJsonObject(String uri, Object params, Class<T> clazz) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, null, getJsonApiRequest(params), clazz);
    }

    public static Map<String, Object> postToJsonMap(String uri, Object params) {
        return requestToJsonMap(HttpPost.METHOD_NAME, uri, null, getJsonApiRequest(params));
    }

    public static String postToString(String uri, Object params) {
        return requestToString(HttpPost.METHOD_NAME, uri, null, getJsonApiRequest(params));
    }

    public static <T> T postToJsonObject(String uri, Map<String, String> headers, Object params, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, headers, getJsonApiRequest(params), typeReference);
    }

    public static <T> T postToJsonObject(String uri, Map<String, String> headers, Object params, Class<T> clazz) {
        return requestToJsonObject(HttpPost.METHOD_NAME, uri, headers, getJsonApiRequest(params), clazz);
    }

    public static Map<String, Object> postToJsonMap(String uri, Map<String, String> headers, Object params) {
        return requestToJsonMap(HttpPost.METHOD_NAME, uri, headers, getJsonApiRequest(params));
    }

    public static String postToString(String uri, Map<String, String> headers, Object params) {
        return requestToString(HttpPost.METHOD_NAME, uri, headers, getJsonApiRequest(params));
    }

    public static <T> T getToJsonObject(String uri, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpGet.METHOD_NAME, uri, null, null, typeReference);
    }

    public static <T> T getToJsonObject(String uri, Class<T> clazz) {
        return requestToJsonObject(HttpGet.METHOD_NAME, uri, null, null, clazz);
    }

    public static Map<String, Object> getToJsonMap(String uri) {
        return requestToJsonMap(HttpGet.METHOD_NAME, uri, null, null);
    }

    public static String getToString(String uri) {
        return request(HttpGet.METHOD_NAME, uri, null, null, (d) -> d);
    }

    public static <T> T getToJsonObject(String uri, Map<String, String> headers, TypeReference<T> typeReference) {
        return requestToJsonObject(HttpGet.METHOD_NAME, uri, headers, null, typeReference);
    }

    public static <T> T getToJsonObject(String uri, Map<String, String> headers, Class<T> clazz) {
        return requestToJsonObject(HttpGet.METHOD_NAME, uri, headers, null, clazz);
    }

    public static Map<String, Object> getToJsonMap(String uri, Map<String, String> headers) {
        return requestToJsonMap(HttpGet.METHOD_NAME, uri, headers, null);
    }

    public static String getToString(String uri, Map<String, String> headers) {
        return request(HttpGet.METHOD_NAME, uri, headers, null, (d) -> d);
    }

    public static <T> T requestToJsonObject(String method, String uri, Map<String, String> headers, ApiRequest apiRequest, TypeReference<T> typeReference) {
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

    public static <T> T request(String method, String uri, Map<String, String> headers, ApiRequest apiRequest, ApiResponse<T> apiResponse) {
        HttpUriRequestImpl httpRequest = new HttpUriRequestImpl(method);
        httpRequest.setURI(URI.create(uri));
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

        public String getMethod() {
            return method;
        }
    }

    public static class HttpException extends RuntimeException {
        public HttpException(String message, Exception e) {
            super(message, e);
        }
    }

    private static boolean hasRequestBody(String method) {
        return HttpPost.METHOD_NAME.equals(method) || HttpPut.METHOD_NAME.equals(method) || HttpPatch.METHOD_NAME.equals(method);
    }

    public static ApiRequest getFormApiRequest(Map<String, String> params) {
        return () -> {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            entity.setContentEncoding(Consts.UTF_8.toString());
            return entity;
        };
    }

    private static ApiRequest getJsonApiRequest(Object params) {
        String data;
        try {
            data = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new HttpException("请求转json错误:" + params.toString(), e);
        }
        return () -> {
            StringEntity entity = new StringEntity(data, Consts.UTF_8);
            entity.setContentEncoding(Consts.UTF_8.toString());
            entity.setContentType("application/json");
            return entity;
        };
    }

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
}
