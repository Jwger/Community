package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Autowired
    //注入thymeleaf模板引擎
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        //调用方法
        mailClient.sendMail("fangzhen2000@163.com","Test","welcome");

    }
    @Test
    public void testHtmlMail(){
//        thymeleaf模板引擎传参
        Context context = new Context();
        context.setVariable("username","sunday");
        String content = templateEngine.process("/mail/demo.html", context);
        System.out.println(content);
        mailClient.sendMail("fangzhen2000@163.com","Html",content);
    }

}
