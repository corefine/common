package org.corefine.common.feign;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class FeignClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String name;
    private Class<T> clientInterface;
    private Environment environment;
    private ObjectMapper objectMapper;

    public FeignClientFactoryBean() {
    }

    public FeignClientFactoryBean(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    @Override
    public T getObject() {
        String propertyName = "feign." + name;
        String url = environment.getProperty(propertyName);
        if (!StringUtils.hasText(url)) {
            throw new BeanCreationException(propertyName + "未正确配置");
        }
        T t = builder(clientInterface, url);
        if (logger.isInfoEnabled()) {
            logger.info("create feign instance {}, config {}, url {}", clientInterface.getName(), name, url);
        }
        return t;
    }


    private <Instance> Instance builder(Class<Instance> clientInterface, String url) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .target(clientInterface, url);
    }

    @Override
    public Class<T> getObjectType() {
        return this.clientInterface;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientInterface(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
