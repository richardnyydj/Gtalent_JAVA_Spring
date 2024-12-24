package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.demo.service.DemoService.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        System.out.println("A Method: " + joinPoint.getSignature().getName() + " is about to execute.");
    }
}