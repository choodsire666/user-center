package com.chood.usercenter.service.impl;

import com.chood.usercenter.config.upload.UploadLocalProperties;
import com.chood.usercenter.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件上传服务实现
 *
 * @author chood
 */
@Slf4j
public class LocalUploadServiceImpl implements UploadService {

    @Resource
    private UploadLocalProperties uploadLocalProperties;

    @Override
    public String upload(MultipartFile file) {
        // 解析文件名
        String originalFilename = file.getOriginalFilename();
        String path = uploadLocalProperties.getPath();
        String finalPath = path + originalFilename;
        try {
            File directory = new File(path);
            if (!directory.exists() && (!directory.mkdirs())) {
                log.error("本地文件夹创建失败:" + path);
                return null;

            }

            File finalFile = new File(finalPath);
            if (!finalFile.exists() && (!finalFile.createNewFile())) {
                log.error("本地文件创建失败:" + finalPath);
                return null;

            }
            file.transferTo(finalFile);
        } catch (IOException e) {
            log.error("本地文件上传失败:" + finalPath, e);
            return null;
        }

        if (Boolean.TRUE.equals(uploadLocalProperties.getEnableProxy())) {
            return uploadLocalProperties.getProxy() + uploadLocalProperties.getUrl() + originalFilename;
        }

        return "/" + uploadLocalProperties.getUrl() + originalFilename;
    }
}
