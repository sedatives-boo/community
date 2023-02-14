package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.ssl.CookieExtension;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AlphaController {
    //测试cookie
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse httpServletResponse){
        //需要用到response对象，在方法参数传入
        Cookie cookie = new Cookie("code","123");//每个cookie只能有一对字符串
        //需要指定路径
        cookie.setPath("/community");
        //浏览器得到cookie默认存内存中，所以关闭浏览器cookie会消失，需要设置生效时间
        cookie.setMaxAge(60*10*60);//秒
        httpServletResponse.addCookie(cookie);
        return "set cookie";
    }
    //测试浏览器会发回cookie
    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        return "get cookie";
    }

    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession httpSession){
        httpSession.setAttribute("id",1);
        return "set session";
    }

    //AJAX 示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String textAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成果");
    }


}
