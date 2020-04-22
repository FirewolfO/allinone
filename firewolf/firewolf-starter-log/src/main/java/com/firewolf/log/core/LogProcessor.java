package com.firewolf.log.core;

import com.firewolf.log.config.properties.LogProperties;
import com.firewolf.log.entity.Log;
import com.firewolf.log.handler.LogHandler;
import com.firewolf.log.operator.LogOperator;
import com.firewolf.log.annotations.OpLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日志注解解析器，日志可以异步保存到数据库，这里只是打印了而已，
 */
@Aspect
@Slf4j
public class LogProcessor {

    @Autowired
    private List<LogHandler> logHandlerList;


    @Qualifier("lx_executor")
    @Resource
    private Executor executor;

    @Resource
    private LogOperator logOperator;

    @Resource
    private ParamParser paramParser;

    @Resource
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @Resource
    private LogProperties logProperties;
    /**
     * 切点
     */
    private final String EXECUTION = "@annotation(com.firewolf.log.annotations.OpLog)";

    @Around(EXECUTION)
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {


        Log logPO = new Log();
        LocalDateTime start = LocalDateTime.now();
        logPO.setStartTime(start);

        //执行方法
        Object result = null;
        LocalDateTime end = null;
        try {
            result = joinPoint.proceed();
            end = LocalDateTime.now();

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
            String[] argNames = parameterNameDiscoverer.getParameterNames(method);

            // 获取参数值
            Object[] argValues = joinPoint.getArgs();

            // 构造上下文并且将参数摄入进去进行赋值赋值
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < argValues.length; i++) {
                context.setVariable(argNames[i], argValues[i]);
            }

            //获取要执行的方法
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = String.format("%s.%s", className, method.getName());

            //获取注解信息
            OpLog operateLog = method.getAnnotation(OpLog.class);

            // 获取注解中的操作说明
            String operate = StringUtils.defaultIfEmpty(operateLog.value(), StringUtils.defaultIfEmpty(operateLog.operate(), method.getName()));
            operate = paramParser.parseStrWithSpEL(operate, context);


            // 整理参数字符串
            String[] params = operateLog.params();
            String paramStr = null;
            if (params.length != 0) {
                paramStr = Stream.of(params).map(parmaStr -> paramParser.parseStrWithSpEL(parmaStr, context)).collect(Collectors.joining(","));
            } else if (logProperties.isAllArgs()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < argNames.length; i++) {
                    String value = argValues[i] == null ? "null" : argValues[i].toString();
                    sb.append(argNames[i]).append("=").append(value).append(",");
                }
                paramStr = sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) + "]" : "";
            }

            String resultStr = null;
            if (result != null) {
                String resultSpEL = operateLog.result();
                if (StringUtils.isNotEmpty(resultSpEL)) {
                    resultStr = paramParser.parseStrWithSpEL(resultSpEL, context);
                } else if (logProperties.isResAll()) {
                    resultStr = result.toString();
                }
            }

            String operator = operateLog.operator();
            if (!StringUtils.isEmpty(operate)) {
                paramParser.parseStrWithSpEL(operator, context);
            } else {
                operator = logOperator.getOperator();
            }
            logPO.setMethod(methodName);
            logPO.setParams(paramStr);
            logPO.setOperate(operate);
            logPO.setOperator(operator);
            logPO.setResult(resultStr);
            logPO.setResultStatus(logProperties.getSuccessDesc());
        } catch (Exception e) {
            logPO.setError(e.getMessage());
            logPO.setResultStatus(logProperties.getErrorDesc());
        }
        logPO.setEndTime(LocalDateTime.now());
        long timeCost = end.toInstant(ZoneOffset.UTC).toEpochMilli() - start.toInstant(ZoneOffset.UTC).toEpochMilli();
        logPO.setTimeCost(timeCost);

        // 异步线程处理日志
        logHandlerList.forEach(logHandler -> CompletableFuture.runAsync(() -> {
            Object object = logHandler.transLog2SelfObj(logPO);
            logHandler.handle(object == null ? logPO : object);
        }, executor));
        return null;
    }
}
