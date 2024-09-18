package com.chood.usercenter.controller;

import com.chood.usercenter.common.ResultCode;
import com.chood.usercenter.exception.BusinessException;
import com.chood.usercenter.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件上传controller
 *
 * @author chood
 */
@RestController
@RequestMapping("/upload")
@Api(value = "文件上传", tags = "文件上传")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        String upload = uploadService.upload(multipartFile);
        if (StringUtils.isBlank(upload)) {
            throw new BusinessException(ResultCode.ERROR, "上传失败!");
        }

        return upload;
    }
}
