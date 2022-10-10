package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import com.sun.org.apache.xerces.internal.parsers.XMLDocumentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 本拦截器实现功能：
 *      在页面头部，移到头像上展示用户信息，以及已登入用户不再显示”登录“，”注册“按钮
 * 1、客户端发送请求，将携带了ticket的cookie发给服务器（cookie的有效路径是整个项目）
 * 2、服务器得到ticket，在LoginTicket表中查询用户信息
 * 3、 得到用户信息
 * 4、将用户信息应用在模板上
 * 5、生成html的携带用户信息
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过request取得的cookie，先将此封装一下，方便以后服用-->util/CookieUtil
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket!=null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getId());
                //再本次请求中持有用户（注意，每个浏览器访问服务器都会创建新线程处理，因此需要考虑多线程下性能），考虑线程隔离-->ThreadLocal   util/HostHolder
                //将请求存在当前线程的map里
                hostHolder.setUser(user);
            }
        }
        return true;
    }
    //该方法在模板引擎前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    //在整个请求结束的时候，清理空间
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
