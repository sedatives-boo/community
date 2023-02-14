package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.Parameter;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class sensitiveFilterTests {
    @Autowired
    public SensitiveFilter sensitiveFilter;

    @Test
    public void wordFilter(){
        String sentence = "我想杀人，赌博，和嫖娼！！";
        sentence = sensitiveFilter.filter(sentence);
        System.out.println(sentence);
    }
}
