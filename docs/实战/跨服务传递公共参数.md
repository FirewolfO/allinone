## 场景
在微服务架构中，有比较的服务，如果我们需要把一个参数在多个服务中传递，如：auth信息、语言信息、请求ID等等，那么我们可以通过下面的方式进行处理

## 相关技术
- ThreadLocal
- Filter
- RequestInterceptor

## 实战
### 一、封装请求相关数据保存工具
我们可以利用ThreadLocal来保存数据，这里推荐使用`InheritableThreadLocal`，因为这个类可以让子线程以及子子线程同样可以拿到数据，而且即使主线程已经做了移除，也不影响。
``` java
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class RequestDataThreadLocal {

    private static final InheritableThreadLocal<ThreadBindData> threadLocal = new InheritableThreadLocal<>();

    /**
     * 设置数据
     * @param data
     */
    public static void set(ThreadBindData data) {
        threadLocal.set(newData);
    }

    /**
     * 删除数据
     */
    public static void remove() {
        threadLocal.remove();
    }


    /**
     * 获取数据
     * @return
     */
    public static ThreadBindData get(){
        return threadLocal.get();
    }
}

```
其中ThreadBindData是用来保存数据的实体类，具体根据自己的需要来处理既可以，比如我这里要放入语言信息和auth信息，则内容如下：
``` java

/**
 * 线程绑定的数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThreadBindData {

    /**
     * 语言
     */
    private String lang;

    /**
     * auth信息
     */
    private String authorization;
}

```
### 二、获取并保存请求参数
我们可以通过Filter把所有请求的参数获取起来，然后保存在ThreadLocal之中，供后续使用。同时在请求结束的时候移除掉数据。
```java
package com.megvii.aipark.common.web;


import com.megvii.aipark.common.domain.ThreadBindData;
import com.megvii.aipark.common.domain.enums.LocaleEnum;
import com.megvii.aipark.common.tool.RequestDataThreadLocal;
import com.megvii.aipark.common.tool.TraceIdThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Slf4j
public class WebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 从Request获取数据然后保存起来。
        String langStr = req.getHeader("lang");
        String authorizationStr = req.getHeader("authorization");
        ThreadBindData threadBindData = new ThreadBindData();
        if(StringUtils.isNotEmpty(langStr)){
            threadBindData.setLang(langStr);
        }
        if(StringUtils.isNotEmpty(authorizationStr)){
            threadBindData.setAuthorization(authorizationStr);
        }
        RequestDataThreadLocal.set(threadBindData);
        try {
            chain.doFilter(request, response);
        } finally {
		    // 请求结束移除数据
            RequestDataThreadLocal.remove();
        }

    }

    @Override
    public void destroy() {

    }
}

```
接下来，我们把这个Filter注入到Spring容器中，
``` java 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
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
```

### 三、配置Feign调用参数传递
我们可以统一的对Feign进行配置，确保调用后续服务的时候把参数传递了下去。
``` java
package com.megvii.aipark.common.web;


import com.megvii.aipark.common.domain.ThreadBindData;
import com.megvii.aipark.common.tool.RequestDataThreadLocal;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {


    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ThreadBindData data = RequestDataThreadLocal.get();
            if (data != null) {
                template.header("lang", data.getLang());
                template.header("authorization", data.getAuthorization());
            }
        };
    }
}

```
通过上面的处理，我们就可以在项目的任何地方通过`RequestDataThreadLocal.get()`方式来获取数据。