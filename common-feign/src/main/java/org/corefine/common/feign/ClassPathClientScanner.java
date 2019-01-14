package org.corefine.common.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TimeZone;

public class ClassPathClientScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathClientScanner.class);
    private final static String DATE_FORMAT_NAME = "spring.jackson.date-format";
    private final static String TIME_ZONE_NAME = "spring.jackson.time-zone";
    private ObjectMapper objectMapper;


    public ClassPathClientScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        this.initObjectMapper();
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    private void initObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).configure(SerializationFeature.INDENT_OUTPUT, true);
        Environment environment = getEnvironment();
        if (environment.containsProperty(TIME_ZONE_NAME)) {
            objectMapper.setTimeZone(TimeZone.getTimeZone(environment.getProperty(TIME_ZONE_NAME)));
        }
        if (environment.containsProperty(DATE_FORMAT_NAME)) {
            objectMapper.setDateFormat(new SimpleDateFormat(environment.getProperty(DATE_FORMAT_NAME)));
        }
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        AnnotatedBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (AnnotatedBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName); // issue #59
            definition.setBeanClassName(FeignClientFactoryBean.class.getName());
            String name = (String) definition.getMetadata().getAnnotationAttributes(FeignClient.class.getCanonicalName()).get("value");
            definition.getPropertyValues().add("name", name);
            definition.getPropertyValues().add("objectMapper", objectMapper);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        return super.checkCandidate(beanName, beanDefinition);
    }
}
