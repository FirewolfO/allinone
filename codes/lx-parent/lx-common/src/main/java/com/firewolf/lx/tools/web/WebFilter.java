package com.firewolf.lx.tools.web;


import com.firewolf.lx.domain.RequestData;
import com.firewolf.lx.tools.RequestThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class WebFilter implements Filter {

    private static String lock = "1";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 保存请求数据
        String lang = req.getHeader("lang");
        String requestId = req.getHeader("requestId");

        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        lang = new Random().nextInt(100) + "";
        RequestData requestData = new RequestData();

        requestData.setLang(lang);
        requestData.setRequestId(requestId);
        RequestThreadLocal.set(requestData);
        MDC.put("requestId", requestId);


        try {
            chain.doFilter(request, response);
        } finally {
//            RequestThreadLocal.remove();
        }

    }

    @Override
    public void destroy() {

    }


}
