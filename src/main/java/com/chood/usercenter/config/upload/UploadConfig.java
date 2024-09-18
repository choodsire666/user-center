package com.chood.usercenter.config.upload;

import com.chood.usercenter.constant.enums.UploadServiceEnums;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件上传配置
 *
 * @author chood
 */
@Configuration
public class UploadConfig implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, WebMvcConfigurer {

    /**
     * 文件上传方式，默认为local，本地上传
     */
    private String model;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();

        // 从枚举中获取对应的文件上传service实现类
        for (UploadServiceEnums uploadServiceEnums : UploadServiceEnums.values()) {
            if (uploadServiceEnums.getModel().equals(model)) {
                genericBeanDefinition.setBeanClass(uploadServiceEnums.getServiceClass());
            }
        }

        beanDefinitionRegistry.registerBeanDefinition("uploadService", genericBeanDefinition);
    }

    /**
     * 在Bean注入之前，获取配置信息
     */
    @Override
    public void setEnvironment(Environment environment) {
        model = Binder.get(environment).bind("upload", UploadProperties.class).map(UploadProperties::getModel).orElse("local");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // 无实现
    }
}
