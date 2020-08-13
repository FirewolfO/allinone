package com.firewolf.test.nbdoc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 描述：TODO
 *
 * @author: liuqingliang@megvii.com
 * Date:     2020/4/9
 */
@RequestMapping("")
public abstract class BaseRequestMappingController implements InitializingBean {

    private static final String URL_SEP = "/";

    @Override
    public void afterPropertiesSet() throws Exception {
            this.handleBaseUrl();
    }

    /**
     * 处理BaseUrl
     */
    private void handleBaseUrl() throws Exception {
        Annotation rm = null;
        Class currClass = this.getClass();
        while(rm == null && currClass != Object.class) {
            rm = currClass.getAnnotation(RequestMapping.class);
            currClass = currClass.getSuperclass();
        }
        if(rm == null) {
            return;
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(rm);
        Field field = invocationHandler.getClass().getDeclaredField("memberValues");
        field.setAccessible(true);
        Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);

        String[] values = (String[])memberValues.get("value");
        String value = values[0];
        String baseUrl = this.baseUrl();
        if(!baseUrl.startsWith(URL_SEP)) {
            baseUrl = URL_SEP + baseUrl;
        }
        if(baseUrl.endsWith(URL_SEP)) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if(value.startsWith(URL_SEP)) {
            values[0] = baseUrl + value;
        } else {
            values[0] = baseUrl + URL_SEP + value;
        }
    }

    /**
     * url前缀
     * @return
     */
    public abstract String baseUrl();
}
