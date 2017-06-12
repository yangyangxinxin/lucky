package com.luckysweetheart.config;

import com.luckysweetheart.web.utils.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxin on 2017/6/12.
 */
@Configuration
public class FreemarkerConfig {

    /*@Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(DomainService domainService){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        Map<String,Object> params = new HashMap<>();
        params.put("domainUtils",domainService);
        freeMarkerConfigurer.setFreemarkerVariables(params);
        return freeMarkerConfigurer;
    }*/

}
