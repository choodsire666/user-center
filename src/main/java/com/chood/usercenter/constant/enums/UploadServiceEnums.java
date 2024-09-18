package com.chood.usercenter.constant.enums;

import com.chood.usercenter.service.impl.LocalUploadServiceImpl;

/**
 * 文件上传服务枚举
 * <p>
 * 目前只支持local本地上传
 *
 * @author chood
 */
public enum UploadServiceEnums {
    LOCAL_UPLOAD("local", LocalUploadServiceImpl.class);

    private final String model;

    private final Class<?> serviceClass;

    UploadServiceEnums(String model, Class<?> serviceClass) {
        this.model = model;
        this.serviceClass = serviceClass;
    }

    public String getModel() {
        return model;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }
}
