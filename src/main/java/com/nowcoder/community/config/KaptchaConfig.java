package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public Producer kaptchaProducer(){//实现接口
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");//单位是pt
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textProducer.font.size","32");
        properties.setProperty("kaptcha.textProducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textProducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");//在字符串的范围内随机
        properties.setProperty("kaptcha.textProducer.char.length","3");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);//将参数封装到config对象中,需传入properties对象
        kaptcha.setConfig(config);
        return kaptcha;

    }
}
