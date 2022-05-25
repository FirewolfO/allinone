package com.firewolf.nbdoc;

import com.firewolf.nbdoc.controller.NBDocController;
import com.firewolf.nbdoc.core.NbDocApplicationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 10:29 上午
 */
@Configuration
@Slf4j
public class NBDocAutoConfiguration {

    @Bean
    public NBDocController controller() {
        log.info("Mapped URL path [/nb/doc/ui] onto method [scom.firewolf.nbdoc.controller.NBDocController.ui]");
        return new NBDocController();
    }

    @Bean
    public NbDocApplicationListener applicationListener(){
        return new NbDocApplicationListener();
    }
}
