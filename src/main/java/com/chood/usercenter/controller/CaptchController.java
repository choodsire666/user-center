package com.chood.usercenter.controller;

import com.chood.usercenter.common.Result;
import com.chood.usercenter.model.request.FindMethodRequest;
import com.chood.usercenter.service.CaptchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 验证码控制层
 *
 * @author chood
 */
@Api(value = "验证码控制器", tags = "验证码控制器")
@RestController
@RequestMapping("/kaptch")
public class CaptchController {

    @Resource
    private CaptchService captchService;

    @PostMapping("/generateAndSend")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    public Result<String> generateAndSend(@RequestBody FindMethodRequest findMethodRequest, HttpSession session) {
        captchService.generateAndSend(findMethodRequest, session);
        return Result.success("验证码获取成功");
    }
}
