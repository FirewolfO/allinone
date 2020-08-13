package com.firewolf.nbdoc.core;

import com.firewolf.nbdoc.annotation.ApiClass;
import com.firewolf.nbdoc.annotation.ApiMethod;
import com.firewolf.nbdoc.controller.NBDocController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:38 上午
 */
public class NbDocApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    public NbDocApplicationListener() {
        System.out.println("NbDocApplicationListener");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Map<String, Object> beanMap = applicationReadyEvent.getApplicationContext().getBeansWithAnnotation(ApiClass.class);

        beanMap.entrySet().stream().forEach(entry -> {
            System.out.println("----------------");
            System.out.println(entry.getValue().getClass().getSimpleName());
            Method[] methods = entry.getValue().getClass().getMethods();
            List<Method> collect = Stream.of(methods).filter(method -> method.getAnnotation(ApiMethod.class) != null).collect(Collectors.toList());
            collect.forEach(m -> System.out.println(m.getName()));
        });

        System.out.println(beanMap);
    }

}
