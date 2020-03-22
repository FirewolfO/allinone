package com.firewolf.tools.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WebConfig {

    @Bean
    public Filter webFilter() {
        return new WebFilter();
    }

    @Bean
    public FilterRegistrationBean filterProxy(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy httpBasicFilter = new DelegatingFilterProxy();
        registrationBean.setFilter(httpBasicFilter);
        Map<String,String> m = new HashMap<>();
        m.put("targetBeanName","webFilter");
        m.put("targetFilterLifecycle","true");
        registrationBean.setInitParameters(m);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
