package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Component
//表示需要由Spring容器去管理 是通用的BEan 哪个层次都可以用
public class MailClient {
    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    //邮件是谁发送的 固定下来
    @Value("${spring.mail.username}")
    private String from;

    //邮件发送给谁，标题，内容
    public void sendMail(String to, String subject, String content) {
        try {
            //createMimeMessage可以创建对象
            MimeMessage message = mailSender.createMimeMessage();
            //spring给我们提供了帮助类 MimeMessageHelper 利用它就可以把邮件构建好
            MimeMessageHelper helper = new MimeMessageHelper(message);
            //传内容
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            //允许支持html文本
            helper.setText(content, true);
            //发送出去
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            //如果发生异常 就记日志
            logger.error("发送邮件失败:" + e.getMessage());
        }
    }
}
