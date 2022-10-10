package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){//用户发送注册表单时会有用户名，密码，邮箱，采用User接受，spring MVC会匹配
        Map<String,Object> map = userService.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已向你的邮箱发送了激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";//跳转到跳转页面
        }else{//携带信息，重新回到注册页面，把service层的三个信息都发回，如果是空的就不显示
            model.addAttribute("usernameMsg",map.get("usernameMag"));
            model.addAttribute("passwordMsg",map.get("passwordMag"));
            model.addAttribute("emailMsg",map.get("emailMag"));
            model.addAttribute("user",user);//测试加的
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if(result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功！");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已经激活过！");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){//由于传入的是图片因此返回值为void；验证码不能存在浏览器端，且需要跨请求，因此使用session
        //先在上面把bean注入
        String text = kaptchaProducer.createText();//根据配置会得到四位的字符串
        BufferedImage image = kaptchaProducer.createImage(text);
        //将验证码存入session
        session.setAttribute("kaptcha",text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            //这里就不要捕获异常了，
            logger.error("响应验证码失败",e.getMessage());
        }//不需要关闭os，spring会统一处理
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password ,String code,boolean rememberme,//这个rememberme是勾选记住我
                        Model model,HttpSession session,HttpServletResponse response){//model用于范围响应数据；getKaptcha存的验证码需要session获取；登入成功了，需要将ticket发给客户端用cookie保存
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //检查账号密码
        //如果勾选了“记住我”，则存的时间长一点
        //这里再次在CommunityConstant类添加常量
        int expiredSecond = rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSecond);
        //如果map总包含ticket，就是成功了
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);//表示整个项目下cookie都是有效的:注入properties中的值
            cookie.setMaxAge(expiredSecond);
            response.addCookie(cookie);//将cookie发给用户
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }
}
