package com.t1_academy.t1_repo.aspect;


import com.t1_academy.t1_repo.exceptions.TaskNotFoundException;
import com.t1_academy.t1_repo.repository.Task;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
     /* joinPoint - точка входа
     @Before("execution(public * com.t1_academy.t1_repo.service.TaskService.*(..))")
     public * com... - public метод, с ЛЮБЫМ типом возвращаемого значения
     .TaskController.* - любой метод в TaskController
     .TaskController.*(..) - любой метод с ЛЮБЫМИ параметрами в TaskController
     public * com.t1_academy.t1_repo.controller..*(..)) - можно не указывать конкретный класс,
     а только пакет - тоже будет работать
     НО лучше работать через аннотации, т.к дебажить сложно аспекты
     */

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(com.t1_academy.t1_repo.aspect.annotation.LogExecution)")
    public void loggingBefore(JoinPoint joinPoint) {
        logger.info("Вызван метод {} с параметрами {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(
            pointcut = "@annotation(com.t1_academy.t1_repo.aspect.annotation.LogException)",
            throwing = "exception"
    )
    public void loggingAfterThrowing(JoinPoint joinPoint, TaskNotFoundException exception) {
        logger.error("Ошибка в методе {} с параметрами {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        logger.error("Ошибка: {} с сообщением: {}",
                exception.getClass().getName(),
                exception.getMessage());
    }

    @Around("@annotation(com.t1_academy.t1_repo.aspect.annotation.LogTracking)")
    public Object loggingAround(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Around advice START: {}", methodName);

        Object proceeded;
        long start = System.nanoTime();

        try {
            proceeded = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Мой аспект сломался", throwable);
            throw new RuntimeException("Мой аспект сломался", throwable);
        }

        long end = System.nanoTime();
        long duration = end - start;

        logger.info("Around advice FINISH: {}", methodName);
        logger.info("Время выполнения: {} ns", duration);
        return proceeded;
    }

    @AfterReturning(
            pointcut = "@annotation(com.t1_academy.t1_repo.aspect.annotation.HandlingResult)",
            returning = "result"
    )
    public void handleResult(JoinPoint joinPoint, Task result) {
        logger.info("Был вызван метод: {}", joinPoint.getSignature().getName());
        logger.info("Результат метода: {}", result);
    }
}

