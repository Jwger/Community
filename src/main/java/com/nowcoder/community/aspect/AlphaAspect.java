package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

//@Component
//@Aspect
public class AlphaAspect {


    //定义切点，描述哪些bean是我要处理的目标
    //第一个星是返回值，表示所有的返回值都可以，+包名+ 表示service下的所有的类所有的方法，
    // 里面的所有的组件所有的返回值
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    //定义通知，可以在连接点结束时，开始时做事情 演示：
    //开头
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    //后面
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    //方法在返回值以后再 处理逻辑
    @AfterReturning("pointcut()")
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    //抛异常时返回代码
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("AfterThrowing");
    }

    //在前面或者在后面都织入逻辑
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object proceed = joinPoint.proceed();
        System.out.println("around after");
        return proceed;
    }
}
