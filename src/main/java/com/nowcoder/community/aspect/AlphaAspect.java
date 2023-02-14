package com.nowcoder.community.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AlphaAspect {
    //方面组件需要定义两个内容：切点 ，定义void方法pointcut
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }
    //有了切点之之后，就可以定义通知了：开始 begin、结束 after、返回后 afterReturning 、异常afterThrowing 、环绕 around
    @Before("pointcut()")
    public void before(){
        System.out.println("before:");
    }
}
