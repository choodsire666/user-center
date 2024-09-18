package com.chood.usercenter.config.upload;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传本地参数
 *
 * @author chood
 */
@Configuration
@ConfigurationProperties(prefix = "upload.local")
@Data
public class UploadLocalProperties {

    /**
     * 文件上传路径，文件夹
     */
    @Value("${upload.local.path:./upload/}")
    private String path;

    /**
     * 文件获取请求路径，资源映射
     */
    @Value("${upload.local.url:upload/}")
    private String url;

    /**
     * 前端是否启用代理
     */
    @Value("${upload.local.enable-proxy:false}")
    private Boolean enableProxy;

    /**
     * 前端代理
     */
    @Value("${upload.local.proxy:/api/}")
    private String proxy;
}
