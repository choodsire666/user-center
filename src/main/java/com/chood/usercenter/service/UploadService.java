package com.chood.usercenter.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 *
 * @author chood
 */
public interface UploadService {


    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件路径
     */
    String upload(MultipartFile file);
}
