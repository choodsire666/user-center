package com.chood.usercenter.config.upload;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 本地上传资源映射
 *
 * @author chood
 */
@Configuration
public class UploadLocalConfig implements WebMvcConfigurer {

    /**
     * 本地上传资源映射
     */
    @Resource
    private UploadLocalProperties uploadLocalProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadLocalProperties.getUrl() + "**")
                .addResourceLocations("file:/" + uploadLocalProperties.getPath());
    }
}
