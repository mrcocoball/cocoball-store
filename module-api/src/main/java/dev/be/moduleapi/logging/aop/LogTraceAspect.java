package dev.be.moduleapi.logging.aop;

import dev.be.moduleapi.logging.logtrace.LogTrace;
import dev.be.moduleapi.logging.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j(topic = "LOG-TRACER")
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Pointcut("execution(* dev.be..*Service.*(..))")
    private void allService(){}

    @Pointcut("execution(* dev.be..*Repository.*(..))")
    private void allRepository(){}

    @Pointcut("execution(* dev.be..*Controller.*(..))")
    private void allController(){}

    // 포인트컷
    @Around("allController() || allService() || allRepository()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        // 어드바이스 로직
        TraceStatus status = null;

        try {
            // 클래스명 + 메서드명
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);

            return result;

        } catch (Exception e){

            logTrace.exception(status, e);
            throw e;

        }
    }

}
