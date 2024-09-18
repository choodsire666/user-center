package com.chood.usercenter.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import com.chood.usercenter.common.ResultCode;
import com.chood.usercenter.constant.consist.CaptchConsist;
import com.chood.usercenter.constant.enums.FindMethodEnums;
import com.chood.usercenter.exception.BusinessException;
import com.chood.usercenter.model.request.FindMethodRequest;
import com.chood.usercenter.service.CaptchService;
import com.chood.usercenter.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * 随机数字验证码实现类
 *
 * @author chood
 */
@Service("numberCaptchService")
@Slf4j
public class NumberCaptchServiceImpl implements CaptchService {

    @Resource
    private EmailService emailService;

    @Override
    public void generateAndSend(FindMethodRequest findMethodRequest, HttpSession session) {
        // 生成验证码
        String code = new RandomGenerator(RandomUtil.BASE_NUMBER, CaptchConsist.CAPTCHA_LEN).generate();
        session.setAttribute(CaptchConsist.CAPTCHA_CODE, code);

        // 定时清除验证码
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
        executorService.schedule(() -> session.removeAttribute(CaptchConsist.CAPTCHA_CODE), CaptchConsist.CAPTCHA_CODE_EXPIRE, TimeUnit.MINUTES);

        String type = findMethodRequest.getType();

        if (FindMethodEnums.EMAIL.getType().equals(type)) {
            // 发送到邮箱

            if (StringUtils.isBlank(findMethodRequest.getEmail())) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "邮箱不能为空");
            }

            emailService.sendSimpleEmail(findMethodRequest.getEmail(), "验证码", "用户中心密码重置验证码: " + code);
        } else if (FindMethodEnums.PHONE.getType().equals(type)) {
            // TODO: 发送到手机
        }
    }

    @Override
    public void generateAndGet(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 40, new RandomGenerator(RandomUtil.BASE_NUMBER, CaptchConsist.CAPTCHA_LEN), 1);
        session.setAttribute(CaptchConsist.CAPTCHA_CODE, lineCaptcha.getCode());

        try {
            lineCaptcha.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("图片验证码生成失败", e);
        }
    }
}
