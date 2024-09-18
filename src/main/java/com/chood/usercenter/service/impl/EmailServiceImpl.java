package com.chood.usercenter.service.impl;

import com.chood.usercenter.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 邮件服务实现类
 *
 * @author chood
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        log.debug("------------------------发送邮件开始----------------------");
        log.debug("发送邮件给{}, 主题{}, 内容{}", to, subject, content);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
        simpleMailMessage.setFrom(Objects.requireNonNull(mailSender.getUsername()));
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
        log.debug("------------------------发送邮件结束----------------------");
    }
}
