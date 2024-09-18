package com.chood.usercenter.service;

/**
 * 邮件服务
 *
 * @author chood
 */
public interface EmailService {

    /**
     * 发送简单邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleEmail(String to, String subject, String content);
}
