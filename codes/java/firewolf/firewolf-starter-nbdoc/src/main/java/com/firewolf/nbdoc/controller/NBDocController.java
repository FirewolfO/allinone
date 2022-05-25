package com.firewolf.nbdoc.controller;

import com.firewolf.nbdoc.annotation.ApiClass;
import com.firewolf.nbdoc.annotation.ApiMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 10:24 上午
 */
@RequestMapping("/nb/doc")
@Controller
@Slf4j
public class NBDocController {

    @GetMapping("/ui")
    public String ui(HttpServletRequest request) {
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);//获取上下文对象
        RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            HandlerMethod handlerMethod = handlerMethods.get(rmi);
            if (handlerMethod.getBeanType().getAnnotation(ApiClass.class) == null) {
                // 类上面没有注解
                continue;
            }
            if (handlerMethod.getMethod().getAnnotation(ApiMethod.class) == null) {
                // 方法上面没有注解
                continue;
            }
            PatternsRequestCondition prc = rmi.getPatternsCondition();
            Set<String> patterns = prc.getPatterns();
            System.out.println("patterns = " + patterns);
//            Annotation[][] annotations = handlerMethod.getMethodParameters()[0].getExecutable().getParameterAnnotations();
//            System.out.println(annotations);

        }
        return "forward:/nbdoc.html";
    }
}
