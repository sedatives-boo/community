package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;//springboot中已管理了模板引擎，只需注入

    @Test
    public void testMail(){
        mailClient.sendMail("574524709@qq.com","test","test mail");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();//注意是thymeleaf的
        context.setVariable("username","sunday");
        String content = templateEngine.process("/mail/demo", context);//把模板地址，数据
        System.out.println(content);
        mailClient.sendMail("574524709@qq.com",  "HTML",content);
    }
}
