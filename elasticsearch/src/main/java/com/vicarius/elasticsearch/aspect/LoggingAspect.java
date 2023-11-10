package com.vicarius.elasticsearch.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.vicarius.elasticsearch.service.ElasticsearchService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.vicarius.elasticsearch.service.ElasticsearchService.create*(..))")
    public void logAfterCreate(JoinPoint joinPoint) {
        logger.info("{} completed successfully with result: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.vicarius.elasticsearch.service.ElasticsearchService.find*(..))", returning = "result")
    public void logAfterFind(JoinPoint joinPoint, Object result) {
        logger.info("{} completed successfully", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.vicarius.elasticsearch.service.ElasticsearchService.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in {}.{}() with cause = {}\n ", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
}
