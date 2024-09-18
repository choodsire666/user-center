package com.chood.usercenter.config.upload;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chood
 */
@Data
@ConfigurationProperties(prefix = "upload")
@Configuration
public class UploadProperties {

    @Value("${upload.model:local}")
    private String model;
}
