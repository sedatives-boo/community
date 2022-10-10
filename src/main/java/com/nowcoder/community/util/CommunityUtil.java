package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommunityUtil {
    //生成激活码，提供字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");//不想要有横线
    }
    //MD5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){//先简单判断下不为空，采用了commons lang包
            return null;
        }else {
            return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));//这是spring自带的加密方法
        }
    }
}
