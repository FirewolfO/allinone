package com.firewolf.test.nbdoc;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述：Web应用Controller基类
 *
 * @author: liuqingliang@megvii.com
 * Date:     2020/3/30
 */
@RequestMapping("/")
public abstract class BaseWebController extends BaseRequestMappingController {

    @Override
    public String baseUrl() {
        return "/v1/web";
    }

}
