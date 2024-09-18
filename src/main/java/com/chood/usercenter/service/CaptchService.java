package com.chood.usercenter.service;

import com.chood.usercenter.model.request.FindMethodRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码服务
 *
 * @author chood
 */
public interface CaptchService {
    /**
     * 生成验证码并发送到别的地方，如邮箱，手机
     */
    void generateAndSend(FindMethodRequest findMethodRequest, HttpSession session);

    /**
     * 生成验证码，并直接返回
     */
    void generateAndGet(HttpServletResponse response, HttpSession session);
}
