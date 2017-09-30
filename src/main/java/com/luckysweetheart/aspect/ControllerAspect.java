package com.luckysweetheart.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by yangxin on 2017/9/6.
 */
@Aspect
@Component
public class ControllerAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.luckysweetheart.web.pc..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerMethodPointcut() {
    }

    @Around("controllerMethodPointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) {

        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        Set<Object> allParams = new LinkedHashSet<>(); //保存所有请求参数，用于输出到日志中

        logger.info("请求开始，方法：{}", methodName);

        Object o = null;
        try {
            o = pjp.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
        long end = System.currentTimeMillis();
        logger.info("请求结束,cost {} ms", end - beginTime);
        return o;
    }
}