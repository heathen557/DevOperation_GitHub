package onenet.DevOperation.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description Aop切面类
 * @Author ZF
 * @Time 2017/8/14 11:12
 */
@Aspect
@Slf4j
@Component
//@ComponentScan  //组件自动扫描
public class HttpAspect {

    String methodName;      // 方法名
    long startTime;         // 开始时间

    /**
     * 切入点
     */
    @Pointcut("execution( * onenet.DevOperation.controller.*.*(..))")
    public void aopPointCut() {}

    /**
     * 统计方法耗时环绕通知
     * @param joinPoint
     */
    // @Around("aopPointCut()")
    // public Object timeAround(ProceedingJoinPoint joinPoint) {
    //     long startTime;
    //     long E_time;
    //     Object obj;
    //     try {
    //         // 获取开始时间
    //         startTime = System.currentTimeMillis();
    //         // 获取返回结果集
    //         obj = joinPoint.proceed(joinPoint.getArgs());
    //         // 获取方法执行时间
    //         E_time = System.currentTimeMillis() - startTime;
    //     } catch (Throwable t) {
    //         throw new MethodRunningTimeException(ResultEnum.METHOD_RUNNING_TIME_ERROR, t);
    //     }
    //     String classAndMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    //     log.info("执行 " + classAndMethod + " 耗时为：" + E_time + "ms");
    //     return obj;
    // }

    @Before("aopPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        startTime = System.currentTimeMillis();
    }

    @After("aopPointCut()")
    public void doAfter() {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + " 耗时为：" + E_time + "ms");
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut()")
    public void doAfterReturning(Object object) {
        //log.info("response={}", object.toString());
    }
}