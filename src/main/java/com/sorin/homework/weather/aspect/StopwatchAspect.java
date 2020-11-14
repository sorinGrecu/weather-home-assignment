package com.sorin.homework.weather.aspect;

import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Method;

@Log4j2
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class StopwatchAspect {
    @Around("@annotation(com.sorin.homework.weather.aspect.Stopwatch)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long t0 = Math.max(1, System.currentTimeMillis() - start);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Stopwatch stopwatch = method.getAnnotation(Stopwatch.class);
        String annotationMessage = stopwatch.message();
        if (Strings.isNullOrEmpty(annotationMessage))
            log.info("Method {} executed in {} ms", joinPoint.getSignature().toShortString(), t0);
        else
            log.info("{}: {} ms", annotationMessage, t0);
        return proceed;
    }
}