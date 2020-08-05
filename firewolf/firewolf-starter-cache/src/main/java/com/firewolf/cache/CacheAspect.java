package com.firewolf.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-08-05
 */
@Slf4j
@Aspect
public class CacheAspect {


    private final String EXECUTION = "@annotation(com.firewolf.cache.LocalCache)";

    ExpressionParser parser = new SpelExpressionParser();
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();


    @Around(EXECUTION)
    public Object invoked(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        LocalCache localCache = method.getAnnotation(LocalCache.class);
        String spel = localCache.key();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Expression expression = parser.parseExpression(spel);
        String value = expression.getValue(context, String.class);
        if (StringUtils.isNotEmpty(value)) {
            System.out.println(localCache.desc() + ",在" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "执行方法，" + pjp.getTarget().getClass() + "." + method.getName()
                    + "(" + convertArgs(args) + "), cacheKey=" + value);
        }
        return pjp.proceed();
    }

    private String convertArgs(Object[] args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (null == arg) {
                builder.append("null");
            } else {
                builder.append(arg.toString());
            }
            builder.append(',');
        }
        builder.setCharAt(builder.length() - 1, ' ');
        return builder.toString();
    }


}
