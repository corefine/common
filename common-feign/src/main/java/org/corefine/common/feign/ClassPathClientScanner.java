package org.corefine.common.feign;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class ClassPathClientScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathClientScanner.class);
    private ObjectMapper objectMapper;

    public ClassPathClientScanner(BeanDefinitionRegistry registry, ObjectMapper objectMapper) {
        super(registry, false);
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        addExcludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getClassMetadata().getClassName().endsWith("package-info"));
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        AnnotatedBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (AnnotatedBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
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
