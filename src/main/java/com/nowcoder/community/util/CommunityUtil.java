package com.nowcoder.community.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * 有时候浏览器返回一个编号，有时候返回字符串提示，有时候返回map信息，需要统一处理为json
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key :map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    //重载方法，有时候没有参数
    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }


}
