package com.ljh.UserSystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 方法执行耗时统计切面类
 * 用于拦截带有@CostTime注解的方法，统计并记录方法执行耗时
 */
@Aspect
@Component
@Slf4j
public class CostTimeAspect {

    /**
     * 环绕通知方法，统计目标方法执行耗时
     * @param joinPoint 连接点对象，包含目标方法的信息
     * @return 目标方法执行后的返回结果
     * @throws Throwable 目标方法执行过程中抛出的异常
     */
    @Around("@annotation(com.ljh.UserSystem.annotation.CostTime)")
    public Object costTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            // 记录方法执行时间
            log.info("方法 {} 执行耗时: {} ms", joinPoint.getSignature().toShortString(), costTime);
        }
    }
}
