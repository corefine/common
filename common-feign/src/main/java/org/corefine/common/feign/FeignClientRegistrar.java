package org.corefine.common.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class FeignClientRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware, EnvironmentAware {
    private final static Logger logger = LoggerFactory.getLogger(FeignClientRegistrar.class);
    private final static String DATE_FORMAT_NAME = "spring.jackson.date-format";
    private final static String TIME_ZONE_NAME = "spring.jackson.time-zone";
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;
    private ObjectMapper objectMapper;

    private void initObjectMapper(Environment environment) {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        if (environment.containsProperty(TIME_ZONE_NAME)) {
            String timeZone = environment.getProperty(TIME_ZONE_NAME);
            objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
            logger.info("jackson time zone " + timeZone);
        }
        if (environment.containsProperty(DATE_FORMAT_NAME)) {
            String dateFormat = environment.getProperty(DATE_FORMAT_NAME);
            if (dateFormat != null) {
                objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
                logger.info("jackson date format " + dateFormat);
            }
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        logger.debug("Searching @FeignClient");
        ClassPathClientScanner scanner = new ClassPathClientScanner(registry, objectMapper);
        try {
            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug("Using auto-configuration base package '{}'", pkg));
            }
            scanner.registerFilters();
            scanner.doScan(StringUtils.toStringArray(packages));
        } catch (IllegalStateException ex) {
            logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.", ex);
        }

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.initObjectMapper(environment);
    }
}
