package com.firewolf.lx.tools.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 日志注解解析器，日志可以异步保存到数据库，这里只是打印了而已，
 */
@Aspect
@Component
@Slf4j
@ConditionalOnBean(value = {LogHandler.class})
public class LogResolver {

    @Autowired
    private List<LogHandler> logHandlerList;


    @Qualifier("lx_executor")
    @Autowired
    private Executor executor;

    /**
     * 切点
     */
    private final String EXECUTION = "@annotation(com.firewolf.lx.tools.log.Log)";

    @Around(EXECUTION)
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {

        //获取要执行的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        String className = joinPoint.getSignature().getDeclaringTypeName();

        String methodName = String.format("%s.%s", className, method.getName());

        //获取注解信息
        Log upgradeProgress = method.getAnnotation(Log.class);

        String operate = upgradeProgress.operate(); // 当前操作

        //开始日志
        String startLog = StringUtils.defaultIfBlank(upgradeProgress.start(), StringUtils.isNotEmpty(operate) ? String.format("start %s", operate) : String.format("start execute %s", methodName));

        //结束日志
        String endLog = StringUtils.defaultIfBlank(upgradeProgress.end(), StringUtils.isNotEmpty(operate) ? String.format("execute %s successfully", operate) : String.format("execute %s successfully", methodName));

        //错误日志
        String errorLog = StringUtils.defaultIfBlank(upgradeProgress.error(), StringUtils.isNotEmpty(operate) ? String.format("execute %s error", operate) : String.format("execute %s error", methodName));


        //记录方法执行前日志
        log.info(startLog);

        //获取方法信息
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); //参数名

        // 参数值：
        final Object[] argValues = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < argNames.length; i++) {
            String value = argValues[i] == null ? "null" : argValues[i].toString();
            sb.append(argNames[i]).append("=").append(value).append(",");
        }
        String paramStr = sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) + "]" : "";
        log.info("参数信息为：[ {}", paramStr);


        LogEntity logEntity = new LogEntity();
        logEntity.setStart(startLog);
        logEntity.setStartTime(new Date());
        logEntity.setMethod(methodName);
        logEntity.setParms(paramStr);
        logEntity.setOperator(operate);

        //执行方法
        Object result = null;

        try {
            result = joinPoint.proceed();
            String resultStr = result == null ? "null" : result.toString();
            logEntity.setResult(resultStr);
            logEntity.setEnd(endLog);
        } catch (Exception e) {
            logEntity.setError(errorLog + e.getCause());
        } finally {
            logEntity.setEndTime(new Date());
        }

        // 异步线程处理日志
        logHandlerList.forEach(logHandler -> CompletableFuture.runAsync(() -> logHandler.handle(logEntity), executor));

        return result;
    }

}
