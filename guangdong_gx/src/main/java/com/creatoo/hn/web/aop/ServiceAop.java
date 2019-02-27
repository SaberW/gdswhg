package com.creatoo.hn.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务类Aop
 * Created by wangxl on 2017/7/13.
 */
@Aspect
@Component
public class ServiceAop {
    /**
     * 日志配置
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    /**
     * 定义一个切入点：匹配com.creatoo.hn.services包或者子包下所有类的所有方法
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 定义在web包或者子包
     * ~ 第三个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(public * com.creatoo.hn.services..*.*(..))")
    public void ServiceAop(){}

    @Before("ServiceAop()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        log.info("["+joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+"]ServiceAop.doBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
        }

        //log.info("HTTP_METHOD : " + request.getMethod());
        //log.info("IP : " + request.getRemoteAddr());
        /*log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        //获取所有参数方法一：
        Enumeration<String> enu=request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName=(String)enu.nextElement();
            log.info(paraName+": "+request.getParameter(paraName));
        }*/
    }

    @AfterReturning("ServiceAop()")
    public void  doAfterReturning(JoinPoint joinPoint){
        // 处理完请求，返回内容
        log.info("["+joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+"]执行时间："+(System.currentTimeMillis()-startTime.get())+"毫秒");
        //log.info("controllerAop.doAfterReturning()");
    }
}
