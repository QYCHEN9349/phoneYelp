package cn.zju.group5.phoneyelp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(public * cn.zju.group5.phoneyelp.controller..*.*(..))")
    public void logPointCut(){}

    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable{
        long beginTime=System.currentTimeMillis();
        Object result = point.proceed();
        long timeCost=System.currentTimeMillis()-beginTime;
        log.info("method: "+point.getSignature().getName()+"--timeCost: "+timeCost);
        return result;
    }


}
